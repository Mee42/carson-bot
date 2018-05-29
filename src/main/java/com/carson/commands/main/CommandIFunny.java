package com.carson.commands.main;

import java.io.IOException;
import java.util.List;

import com.carson.classes.PhotoStream;
import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandIFunny extends Command implements ICommand{

	public CommandIFunny(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().equals("~ifunny");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		
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
		return 	"~ifunny";
	}
	
	@Override
	public String getDisciption() {
		return "gets the latest and greatest ifunny memes. Don't bother using this command more then once, as it pulls the top 10, and they only update every hour or so";
	}

}
