package com.carson.commands.db;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.Register;
import com.carson.dataObject.DBHandler;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandAdd extends Command {
    public CommandAdd(IDiscordClient c) {
        super(c);
    }

    @Override
    public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        throw new AssertionError();
    }

    @Override
    public boolean test(MessageReceivedEvent event) {
        if(!hasPermission(getWantedPermissionLevel(), Register.getPermissionLevel(event)))return false;
        return event.getMessage().getContent().startsWith("cb-add");
    }

    @Override
    public PermissionLevel getWantedPermissionLevel() {
        return PermissionLevel.BOT_ADMIN;
    }

    @Override
    public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        if(args.length < 5){
            System.out.println("error");
            sendMessage(event, "insufficient args  cb-add collection id key value");
            return;
        }
        String collection = args[1];
        String strId = args[2];
        String key = args[3];
        String value = args[4];
        for(int i =  5;i<args.length;i++){
            value+=" " + args[i];
        }
        Object id = strId;
        try{
            long l = Long.parseLong(strId);
            id = l;//set id to a long if possible
        }catch (NumberFormatException e){}

        Document doc = DBHandler.get().getDB(collection).find(Filters.eq("_id", id)).first();
        if(doc == null){
            doc = new Document().append("_id", id);
            doc.append(key,value);
            DBHandler.get().getDB(collection).insertOne(doc);
            sendMessage(event, "made new object");
        }
        doc.append(key,value);
        DBHandler.get().getDB(collection).findOneAndReplace(Filters.eq("_id",id), doc);
        sendMessage(event, "success");
    }

    @Override
    public String getCommandId() {
        return "add";
    }
}
