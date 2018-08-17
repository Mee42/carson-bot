package com.carson;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.Register;
import com.carson.dataObject.DBHandler;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.io.*;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        long id = 293853365891235841L;
        final DBHandler db = DBHandler.get();
        Map<String,Integer> words = new HashMap<>();
        for(Document doc : db.getDB("messages").find(/*Filters.eq("user_id",id)*/)){
            String content = (String)doc.get("content");
            content = content.toLowerCase();
            String[] split = content.split("\\W+");
            for(String word : split){
                if(word.equals(" ") || word.equals("")){
                    continue;
                }
                if(words.containsKey(word)){
                    words.replace(word, words.get(word)+1);
                }else {
                    words.put(word,1);
                }
            }
        }
        words = sortByValue(words);
        List<String> mostCommon = readWords(100);
        int total = 0;
        for(String key : words.keySet()){
            if(mostCommon.contains(key))continue;
            System.out.println(key +":" + words.get(key));
            total+=words.get(key);
        }
        System.out.println("total words:" + total);

    }

    public static List<String> readWords(int size){
        try {
            BufferedReader r = new BufferedReader(new FileReader(new File("most_common_words")));
            List<String> strs = new ArrayList<>();
            String line = r.readLine();
            while(line != null && strs.size() < size){
                strs.add(line);
                line = r.readLine();
            }
            return strs;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new AssertionError();
    }

    public static void runX(){
        new Test().run();
    }

    private static Register register;
    public static void setRegister(Register register){
        Test.register = register;
    }


    private void resetCommands() {
        Register reg = Test.register;
        for(Command c : reg.getCommands()){
            System.out.println(c.getCommandId());
            if(c.getCommandId() == null) {
                System.err.println(c.getClass().getName() + " DOESN'T HAVE A COMMAND ID");
                continue;
            }

            Document doc = DBHandler.get().getDB("commands").find(Filters.eq("_id",c.getCommandId())).first();
            if(doc == null){
                DBHandler.get().getDB("commands").insertOne(new Document().append("_id",c.getCommandId() ));
            }
        }

        for(Document d : DBHandler.get().getDB("commands").find()){
            String id = (String)d.get("_id");
            boolean exists = false;
            for(Command c : reg.getCommands()){
                if(c.getCommandId().equals(id)){
                    exists = true;
                    break;
                }
            }
            if(!exists){
                System.err.println("DELETING COMMAND ID:" + id);
                DBHandler.get().getDB("commands").deleteOne(Filters.eq("_id",id));
            }
        }
    }


    private void run(){
        Register reg = Test.register;
        for(Document doc : DBHandler.get().getDB("commands").find()){
            String name = (String)doc.get("name");
            if(name == null){
                System.err.println("ERROR could not update " + doc.get("_id"));
                continue;
            }
            String trigger = name;
            try {
                name = name.split(" ")[0];//should never throw a AIOOBE right?
            }catch(ArrayIndexOutOfBoundsException e){
                //just in case, don't screw up anything else
                System.err.println("ERROR could not update " + doc.get("_id"));
                continue;
            }
            doc.append("trigger",trigger);
            doc.append("name",name);
            DBHandler.get().getDB("commands").replaceOne(Filters.eq("_id", doc.get("_id")),doc);
            System.out.println("updated " + doc.get("_id"));
            System.out.println("\tname:" + name);
            System.out.println("\ttrigger:" + trigger);
        }
    }

    private static <K, V> Map<K, V> sortByValue(Map<K, V> map) {
    List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
    Collections.sort(list, (Comparator<Object>) (o1, o2) -> ((Comparable<V>) ((Map.Entry<K, V>) (o1)).getValue()).compareTo(((Map.Entry<K, V>) (o2)).getValue()));

    Map<K, V> result = new LinkedHashMap<>();
    for (Iterator<Map.Entry<K, V>> it = list.iterator(); it.hasNext();) {
        Map.Entry<K, V> entry = (Map.Entry<K, V>) it.next();
        result.put(entry.getKey(), entry.getValue());
    }

    return result;
}

}
