package com.carson.commands.cb;

import com.carson.classes.DB;
import com.carson.commandManagers.Command;
import com.carson.dataObject.DBHandler;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandNotDone extends Command {
    public CommandNotDone(IDiscordClient c) {
        super(c);
    }

    @Override
    public boolean test(MessageReceivedEvent event) {
        return event.getMessage().getContent().startsWith("cb-todo");
    }

    @Override
    public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        throw new AssertionError();
    }

    @Override
    public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        final MongoCollection<Document> commands = DBHandler.get().getDB("commands");
        String str = "```";
        for(Document d : commands.find()){
            if(d.containsKey("name") && d.containsKey("desc")){
                continue;
            }
            if(d.containsKey("nohelp") && d.get("nohelp").equals("true")){
                continue;
            }
            if(d.containsKey("broken") && d.get("broken").equals("true")){
                str+="\nyou need to finish " + d.get("_id") + "\n";
                continue;
            }
            str+="\n";
            str+= DB.toString(d);
        }
        sendMessage(event, str + "```");
    }

    @Override
    public String getCommandId() {
        return "todo";
    }



}
