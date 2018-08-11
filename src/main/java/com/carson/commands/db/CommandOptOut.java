package com.carson.commands.db;

import com.carson.commandManagers.Command;
import com.carson.dataObject.DBHandler;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandOptOut extends Command {
    public CommandOptOut(IDiscordClient c) {
        super(c);
    }

    @Override
    public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        return content.equals("optout");
    }

    @Override
    public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        DBHandler.get().getDB("opt").deleteOne(Filters.eq("_id",event.getAuthor().getLongID()));//maybe we don't need to have this
        DBHandler.get().getDB("opt").insertOne(new Document().append("_id",event.getAuthor().getLongID()).append("opt",false));
        sendMessage(event, "you have been opted out");
    }

    @Override
    public String getCommandId() {
        return "optout";
    }
}
