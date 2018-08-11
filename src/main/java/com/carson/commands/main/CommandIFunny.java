package com.carson.commands.main;

import com.carson.classes.PhotoStream;
import com.carson.commandManagers.Command;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.List;

public class CommandIFunny extends Command{

	public CommandIFunny(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
		return content.equals("ifunny");
	}

	@Override
	public void run(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
		List<String> pics;
		try {
			pics = PhotoStream.getUrl("https://www.ifunny.co");
		        
				for(String pic : pics) {
					if(pic.startsWith("https://img.ifcdn.com")){
						
						sendMessage(event.getChannel(), pic);
			        	
					}
		        	
				}
				
		} catch (IOException e1) {
			System.out.println("ERROR: running ~IFunny threw a IOExecption");
			e1.printStackTrace();
		}
		return ;
	}

	@Override
	public String getCommandId() {
		return "ifunny";
	}
}
