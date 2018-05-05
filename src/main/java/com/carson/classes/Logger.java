package com.carson.classes;

import java.io.File;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class Logger {
	
	
	
	public static void log(MessageReceivedEvent event) {
		IUser author = event.getAuthor();
		IChannel channel = event.getChannel();
		String text = event.getMessage().getContent();
		text = text.replace(System.getProperty("line.separator"), "\\n");
		
		if(!channel.isPrivate()) {
			FileIO f = new FileIO("/home/carson/discord/files/logger/" + event.getGuild().getName() + "/" + channel.getName() + ".txt");
			new File("/home/carson/discord/files/logger/" + event.getGuild().getName()).mkdirs();
			f.create();
			f.addln(author.getName() + ":" + text );
			System.out.print("MESSAGE:" + author.getName() + ":" + text +"(" + event.getGuild().getName() +" - " +channel.getName() + ")");
		
			
			new File("/home/carson/discord/files/ranks/" + event.getGuild().getName()).mkdirs();
			FileIO fR = new FileIO("/home/carson/discord/files/ranks/" + event.getGuild().getName() + "/" + author.getName() + ".txt");
			if(fR.exists()) {
				long xp = Long.valueOf(fR.readList().get(0));
				xp+= (int)(Math.random()*15+1);
				fR.write(String.valueOf(xp));
				System.out.println("   xp:" + xp);
			}else {
				fR.create();
				fR.write("1");
				System.out.println();
			}
			
			
			
		}else {
			FileIO f = new FileIO("/home/carson/discord/files/logger/dm/" + channel.getName() + ".txt");
			new File("/home/carson/discord/files/logger/dm/").mkdirs();
			f.create();
			f.addln(author.getName() + ":" + text );
			System.out.print("MESSAGE:" + author.getName() + ":" + text +"(" + event.getGuild().getName() +" - " +channel.getName() + ")");
		}
	}
	
	public static void logBot(IChannel channel, String text) {
		text = text.replace(System.getProperty("line.separator"), "\\n");
		
		if(!channel.isPrivate()) {
			FileIO f = new FileIO("/home/carson/discord/files/logger/" + channel.getGuild().getName() + "/" + channel.getName() + ".txt");
			new File("/home/carson/discord/files/logger/" + channel.getGuild().getName()).mkdirs();
			f.create();
			
			f.addln("BOT_MESSAGE::" + text );
			System.out.println("BOT_MESSAGE:"  + text +"(" + channel.getGuild().getName() +" - " +channel.getName() + ")");
			
		}else {
			FileIO f = new FileIO("/home/carson/discord/files/logger/dm/" + channel.getName() + ".txt");
			new File("/home/carson/discord/files/logger/dm/").mkdirs();
			f.create();
			f.addln("BOT_MESSAGE::" + text );
			System.out.println("BOT_MESSAGE:"  + text +"(" + channel.getGuild().getName() +" - " +channel.getName() + ")");
		}
	
	}
	
	public static void logBotClean(IChannel channel, String text) {
		text = text.replace(System.getProperty("line.separator"), "\\n");
		
		if(!channel.isPrivate()) {
			FileIO f = new FileIO("/home/carson/discord/files/logger/" + channel.getGuild().getName() + "/" + channel.getName() + ".txt");
			new File("/home/carson/discord/files/logger/" + channel.getGuild().getName()).mkdirs();
			
			f.create();
			
			f.addln("BOT_MESSAGE::" + text );
			
		}else {
			FileIO f = new FileIO("/home/carson/discord/files/logger/dm/" + channel.getName() + ".txt");
			new File("/home/carson/discord/files/logger/dm/").mkdirs();
			f.create();
			f.addln("BOT_MESSAGE::" + text );
			System.out.println("BOT_MESSAGE:"  + text +"(" + channel.getGuild().getName() +" - " +channel.getName() + ")");
		}
	}
	
	
}
