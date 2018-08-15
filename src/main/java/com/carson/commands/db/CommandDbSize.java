package com.carson.commands.db;

import com.carson.commandManagers.Command;
import com.carson.dataObject.DBHandler;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.RequestBuffer;

public class CommandDbSize extends Command {



    public CommandDbSize(IDiscordClient c) {
        super(c);
    }

    @Override
    public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        throw new RuntimeException();
    }

    @Override
    public boolean test(MessageReceivedEvent event) {
        return event.getMessage().getContent().equals("db-size");
    }

    @Override
    public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        int entrys = 0;
        final RequestBuffer.RequestFuture<IMessage> request = RequestBuffer.request(() -> event.getChannel().sendMessage("processing...."));
        final MongoDatabase database = DBHandler.get().getDatabase();
        final MongoIterable<String> names = database.listCollectionNames();
        for(String name : names){
            entrys+=DBHandler.get().getDB(name).countDocuments();
        }
        String str = "{ dbStats: 1, scale: 1 }";
        Bson command = BsonDocument.parse(str);
        //db.runCommand({ dbStats: 1, scale: 1024 })
        final Document document = DBHandler.get().getDatabase().runCommand(command);
        String bytes = "?";
        String megabytes = "?";
        for(String key : document.keySet()){
            if(key.equals("dataSize")){
                double info = (Double) document.get(key);
                bytes = "" + (int)info;
                megabytes = "" + (info / 1000000);
            }
        }
        sendMessage(event, "db size:" + DBHandler.get().toString().length() + " chars\n" +
                "entrys:" + entrys + "\nbytes:" + bytes + "\nmegabytes:" + megabytes);
        RequestBuffer.request(()->request.get().delete());

    }

    @Override
    public String getCommandId() {
        return "dbsize";
    }
}
