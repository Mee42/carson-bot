package com.carson.dataObject;

import com.mongodb.MongoClient;

public class Mongo {
    private static Mongo ourInstance = new Mongo();

    public static Mongo getInstance() {
        return ourInstance;
    }

    private Mongo() {}


    private MongoClient mongoClient = new MongoClient();

    public MongoClient getMongoClient() {
        return mongoClient;
    }
}
