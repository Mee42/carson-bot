package com.carson.classes;

import com.carson.dataObject.DBHandler;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//import java.io.File;

public class Logger { 

    public static final String messPath = "/home/carson/java/files/messages/";
	
	public static void log(MessageReceivedEvent event) {

		IUser author = event.getAuthor();

		IChannel channel = event.getChannel();
		String text = event.getMessage().getContent();
		text = text.replace(System.getProperty("line.separator"), "\\n");
		
		if(!channel.isPrivate()) {
			int xp = (int)(java.lang.Math.random()*15+1);

			DBHandler db = DBHandler.get();
			DBHandler.GuildUserData user;
			Document doc = db.getUsersDB().
					find(Filters.eq(event.getAuthor().getLongID() + "" + event.getGuild().getLongID())).first();
			if(doc == null){
				doc = db.fromGuildUserData(db.new GuildUserData(event.getAuthor().getLongID(),event.getGuild().getLongID(),0));
			}
			user = db.toGuildUserData(doc);
			user.setXp(user.getXp() + xp);
			db.update(user);

			System.out.println("MESSAGE:" + author.getName() + ":" + text +"(" + event.getGuild().getName() +" - " +channel.getName() + ")");	
		}else {
			System.out.println("MESSAGE:" + author.getName() + ":" + text +"(dm - " + event.getChannel().getUsersHere().get(0).getName() + ")");
		}



		Date date = new Date();
		Integer hour= Integer.parseInt(new SimpleDateFormat("HH").format(date));

		LocalDate now = LocalDate.now();
		LocalDate epoch = LocalDate.ofEpochDay(0);

		long day = ChronoUnit.DAYS.between(epoch, now);
		long month = ChronoUnit.MONTHS.between(epoch,now);

		Document doc = new Document()
				.append("_id",event.getMessageID())
				.append("user_id",event.getAuthor().getLongID())
				.append("channel_id",event.getChannel().getLongID())
				.append("hour",hour)
				.append("day",day)
				.append("month",month);

		List<String> messageIds = new ArrayList<>();
		for(IMessage.Attachment a : event.getMessage().getAttachments()){
			Document d = new Document()
					.append("_id", UUID.randomUUID().toString())
					.append("message_id",event.getMessageID())
					.append("name",a.getFilename())
					.append("url",a.getUrl());
			messageIds.add((String)d.get("_id"));
			DBHandler.get().getAttachmentsDB().insertOne(d);
		}
		if(messageIds.size() != 0){
			doc.append("attachments", messageIds);
		}
		DBHandler.get().getMessagesDB().insertOne(doc);


    }
	
	public static void logBot(IChannel channel, String text) {
		text = text.replace(System.getProperty("line.separator"), "\\n");
		
		if(!channel.isPrivate()) {
			System.out.println("BOT_MESSAGE:"  + text +"(" + channel.getGuild().getName() +" - " +channel.getName() + ")");
		}else {
			System.out.println("BOT_MESSAGE:"  + text +"(dm - " + channel.getUsersHere().get(0).getName() + ")");
		}
	
	}
	


}
