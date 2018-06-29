package com.carson.commands.cb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandRestart extends Command implements ICommand{

	public CommandRestart(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().equals("cb-reboot");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		List<String> commands=new ArrayList<>();  
		commands.add("/bin/bash");  
		commands.add("-c");  		
		commands.add("java -jar /home/carson/java/jars/*");
		commands.add("");
		
		ProcessBuilder p=new ProcessBuilder(commands);  
		try {
			p.start();
		} catch (IOException e) {
			sendMessage(event,"didn't work. threw an io execption");
			e.printStackTrace();
		} 
		System.exit(0);
	}

	@Override
	public String getName() {
		return "cb-reboot";
	}

	@Override
	public String getDisciption() {
		return "reboots the bot into a background task. use tail -f console.txt in jars";
	}

}
