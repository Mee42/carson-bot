package com.carson.commands.main;

import java.io.IOException;
import java.util.List;

import com.carson.classes.PhotoStream;
import com.carson.commandManagers.Command;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandIFunny extends Command{

	public CommandIFunny(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event, String content, String[] args) {
		return content.equals("~ifunny");
	}

	@Override
	public void run(MessageReceivedEvent event, String content, String[] args) {
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
	public String getName() {
		return "~ifunny";
	}
	
	@Override
	public String getDisciption() {
		return "gets the latest and greatest ifunny memes. Don't bother using this command more then once, as it pulls the top 10, and they only update every hour or so";
	}

}
