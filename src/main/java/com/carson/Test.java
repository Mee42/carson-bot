package com.carson;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.Register;
import com.carson.dataObject.DBHandler;
import com.mongodb.client.model.Filters;
import org.bson.Document;

public class Test {
    public static void main(String[] args){
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
}
