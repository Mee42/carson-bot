package com.carson.commands.db;

import com.carson.classes.DB;
import com.carson.classes.FileIO;
import com.carson.commandManagers.Command;
import com.carson.dataObject.DBHandler;
import org.bson.Document;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandDb extends Command {
    public CommandDb(IDiscordClient c) {
        super(c);
    }

    @Override
    public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        throw new AssertionError();//should never be called
    }

    @Override
    public boolean test(MessageReceivedEvent event) {
        return event.getMessage().getContent().equals("db") ||
                event.getMessage().getContent().startsWith("get-db");
    }

    @Override
    public PermissionLevel getWantedPermissionLevel() {
        return PermissionLevel.BOT_ADMIN;
    }

    @Override
    public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        sendMessage(event, "generating....");
        String message = "";
        if(rawContent.startsWith("get-db")){
            for(String collection : args){
                if(collection.equals("get-db"))continue;

                message+="============================================================\n";
                message+="============================================================\n";
                message+="============================================================\n";
                message+="===================" + collection + "=======================\n";
                message+="============================================================\n";
                message+="============================================================\n";
                message+="============================================================\n";
                for(Document document : DBHandler.get().getDB(collection).find()){
                    message+= DB.toString(document) + "\n";
                }
            }
        }else{
            message = DBHandler.get().toString();
        }
        sendMessage(event, "length:" + message.length());
        sendMessage(event, "entrys:" + getEntryCount());
        if(!(message.length() > 10_000) && !message.contains("-file-")) {
            if (message.length() > 2000) {
                for (String segment : getParts(message, 1990)) {
                    sendMessage(event, "```" + segment + "```");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                sendMessage(event, "```" + message + " ```");
            }
        }else{
            sendMessage(event, "to much text for me to send. sending a file");
            File db = new File("db" + UUID.randomUUID().toString().replace("-","").substring(0,5) + ".txt");
            try {
                db.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileIO.use(db).write(message);
            final String finalMessage = message;
            RequestBuffer.request(() -> {
                try {
                    event.getChannel().sendFile(db);
                } catch (FileNotFoundException e) {
//                        e.printStackTrace();
                    try {
                        FileIO.use(db).write(finalMessage);//rewrite if failed
                        event.getChannel().sendFile(db);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            db.delete();
        }
    }

    private long getEntryCount() {
        long base = 0;
        for (String collection : DBHandler.get().getDatabase().listCollectionNames()){
            base += DBHandler.get().getDB(collection).countDocuments();
        }
        return base;
    }

    private List<String> getParts(String string, int partitionSize) {
        List<String> parts = new ArrayList<>();
        int len = string.length();
        for (int i=0; i<len; i+=partitionSize)
            parts.add(string.substring(i, Math.min(len, i + partitionSize)));
        return parts;
    }


    @Override
    public String getCommandId() {
        return "db";
    }
}
