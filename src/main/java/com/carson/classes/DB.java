package com.carson.classes;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

public class DB {
    public static void createOrReplace(Document document, MongoCollection<Document> collection){
        FindIterable<Document> documents = collection.find(Filters.eq("_id", document.get("_id")));
        if(documents.first() == null){
            collection.insertOne(document);
        }else{
            collection.replaceOne(Filters.eq("_id", document.get("_id")),document);
        }
    }

    public static Document getById(Object id, MongoCollection<Document> collection){
        return collection.find(Filters.eq("_id", id)).first();
    }
    public static void print(MongoCollection<Document> collection){
        System.out.println("----------");
        for(Document d :collection.find()){
            print(d);
        }
        System.out.println("----------");
    }


    public static void print(Document d){
        System.out.println(toString(d));
    }
    public static String toString(Document d){
        String str = "";
        str+="{\n";
        for(String key : d.keySet()){
            str+="\t" + "\"" + key + "\" : \"" + d.get(key) + "\"\n";
        }
        str+="}";
        return str;
    }
    public static boolean delete(Object id,MongoCollection<Document> collection){
        Document d = collection.find(Filters.eq("_id",id)).first();
        if(d == null){
            return false;
        }
        collection.deleteOne(Filters.eq("_id", id));
        return true;
    }

}
