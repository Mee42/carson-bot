package com.carson.dataObject;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;


public class DBHandler {
    public DBHandler(){}

    public MongoDatabase getDatabase(){
        MongoClient mongoClient = Mongo.getInstance().getMongoClient();
        GuildDataOrginizer data = DataGetter.getInstance();
        MongoDatabase database = mongoClient.getDatabase("carson-bot");
        return database;
    }

    abstract class UserData{
        abstract int getXp();
        abstract long getId();
    }
    public List<UserData> getUserData(){
        MongoCollection<Document> xpCollection = getDatabase().getCollection("xp");
        List<UserData> userData = new ArrayList<>();
        for(Document document : xpCollection.find()){
            userData.add(new UserData() {
                private long id;//effectivly final
                private int xp;// ^
                private UserData enterData(long id, int xp){
                    this.id = id;
                    this.xp = xp;
                    return this;
                }
                @Override
                int getXp() {
                    return 0;
                }

                @Override
                long getId() {
                    return 0;
                }
            }.enterData((long)document.get("_id"),(int)document.get("xp")));
        }
        return userData;
    }
}
