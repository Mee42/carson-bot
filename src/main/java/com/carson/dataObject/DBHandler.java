package com.carson.dataObject;

import com.carson.classes.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.print.Doc;


public class DBHandler {
    public DBHandler(){}

    public void sterilize(){
        MongoClient mongoClient = Mongo.getInstance().getMongoClient();
        GuildDataOrginizer data = DataGetter.getInstance();
        MongoDatabase database = mongoClient.getDatabase("carson-bot");
        MongoCollection<Document> xpCollection = database.getCollection("xp");

        for(GuildDataOrginizer.UserDataNoGuild user : data.getUsers()){
            Document document = new Document()
                    .append("_id",user.id)
                    .append("xp",user.getXP());
            DB.createOrReplace(document, xpCollection);
        }

    }
}
