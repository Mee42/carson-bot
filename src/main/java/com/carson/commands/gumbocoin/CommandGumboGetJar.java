package com.carson.commands.gumbocoin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandGumboGetJar extends Command implements ICommand{
	public static String location = "/home/carson/java/files/";
	
	public CommandGumboGetJar(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().toLowerCase().equals("~gumbo get_jar");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		try {
			File f =download("https://github.com/Mee42/GumboCoin/blob/master/GumboCoin-all.jar?raw=true",location + "GumboCoinMiner.jar");
			event.getChannel().sendFile(f);
			f.delete();
			sendMessage(event,"run it from the command line with `java -jar *jar file*`");
		} catch (IOException e) {
			e.printStackTrace();
			sendMessage(event,"sorry, couldn't get the file for you. You can download it on github, however, at: https://github.com/Mee42/GumboCoin");
		}
		
		
		
	}

	@Override
	public String getName() {
		return "~gumbo get_jar";
	}

	@Override
	public String getDisciption() {
		return"gets the jar so you can start MINING";
	}

	
	public static File download(String downloadURL, String fileName) throws IOException{//DONNLOADS AND RETURNS THE FILE
	    URL website = new URL(downloadURL);
	    try (InputStream inputStream = website.openStream())
	    {
	        Files.copy(inputStream, Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
	    }
	    return new File(fileName);
	}
	
	
	
}


