package com.carson.classes;

import com.carson.dataObject.DataGetter;

//import java.io.File;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class Logger { //TODO WRITE TO STORE IN JSON (SEE MOBILE-WRITTING-OFFICAL
	
	
	
	public static void log(MessageReceivedEvent event) {
		IUser author = event.getAuthor();
		int xp = (int)(Math.random()*15+1);
		DataGetter.getInstance().increaseXPForUser(author.getLongID(), xp);
		IChannel channel = event.getChannel();
		String text = event.getMessage().getContent();
		text = text.replace(System.getProperty("line.separator"), "\\n");
		
		if(!channel.isPrivate()) {
			System.out.print("MESSAGE:" + author.getName() + ":" + text +"(" + event.getGuild().getName() +" - " +channel.getName() + ")\n");	
		}else {
			System.out.print("MESSAGE:" + author.getName() + ":" + text +"(dm - " + event.getChannel().getUsersHere().get(0).getName() + ")\n");
		}
	}
	
	public static void logBot(IChannel channel, String text) {
		text = text.replace(System.getProperty("line.separator"), "\\n");
		
		if(!channel.isPrivate()) {
			System.out.println("BOT_MESSAGE:"  + text +"(" + channel.getGuild().getName() +" - " +channel.getName() + ")\n");
		}else {
			System.out.println("BOT_MESSAGE:"  + text +"(dm - " + channel.getUsersHere().get(0).getName() + ")\n");
		}
	
	}
	
	
	
}
