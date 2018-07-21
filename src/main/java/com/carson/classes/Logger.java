package com.carson.classes;

import com.carson.dataObject.DBHandler;

//import java.io.File;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

import java.io.File;

public class Logger { 

    public static final String messPath = "/home/carson/java/files/messages/";
	
	public static void log(MessageReceivedEvent event) {
		IUser author = event.getAuthor();
		int xp = (int)(java.lang.Math.random()*15+1);

        DBHandler db = DBHandler.get();
        DBHandler.GuildUserData user;
        Document doc = db.getUsersDB().find(Filters.eq(event.getAuthor().getLongID() + "" + event.getGuild().getLongID())).first();
        if(doc == null){
        	doc = db.fromGuildUserData(db.new GuildUserData(event.getAuthor().getLongID(),event.getGuild().getLongID(),0));
		}
		user = db.toGuildUserData(doc);
		user.setXp(user.getXp() + xp);
        db.update(user);

		IChannel channel = event.getChannel();
		String text = event.getMessage().getContent();
		text = text.replace(System.getProperty("line.separator"), "\\n");
		
		if(!channel.isPrivate()) {
			System.out.println("MESSAGE:" + author.getName() + ":" + text +"(" + event.getGuild().getName() +" - " +channel.getName() + ")");	
		}else {
			System.out.println("MESSAGE:" + author.getName() + ":" + text +"(dm - " + event.getChannel().getUsersHere().get(0).getName() + ")");
		}


		Message m = new Message();
        m.userId = event.getAuthor().getLongID();
        m.messageId = event.getMessageID();
        m.channelId = event.getChannel().getLongID();
	    m.guildId = (event.getGuild() == null)?-1:event.getGuild().getLongID();
	    m.message = event.getMessage().getContent();
	    m.timestamp = event.getMessage().getTimestamp().toEpochMilli();
	    Gson g = new GsonBuilder().setPrettyPrinting().create();
	    String json = g.toJson(m);
        new File(messPath + m.guildId + "/").mkdir();
        new File(messPath + m.guildId + "/" + m.channelId+ "/").mkdir();
        FileIO.use(messPath + m.guildId + "/" + m.channelId + "/" + m.messageId + ".json").write(json);
    }
	
	public static void logBot(IChannel channel, String text) {
		text = text.replace(System.getProperty("line.separator"), "\\n");
		
		if(!channel.isPrivate()) {
			System.out.println("BOT_MESSAGE:"  + text +"(" + channel.getGuild().getName() +" - " +channel.getName() + ")");
		}else {
			System.out.println("BOT_MESSAGE:"  + text +"(dm - " + channel.getUsersHere().get(0).getName() + ")");
		}
	
	}
	


	static class Message{
	    public long userId;
	    public long messageId;
        public long channelId;
        public long guildId;

        public String message;

        public long timestamp;
    }
	
}
