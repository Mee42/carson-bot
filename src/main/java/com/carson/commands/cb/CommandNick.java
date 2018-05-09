package com.carson.commands.cb;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RateLimitException;

public class CommandNick extends Command implements ICommand{
	  

	public CommandNick(IDiscordClient c) {
		super(c);
		
	}

	
	//run
	
	@Override
	public String getName(){
	return "cb-nick *text*";
	}

	public String getDiscription(){
	    return "changes the nickname of the bot to *text* in the current guild";
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		System.out.println("testing for:" + event.getMessage().getContent());
		return event.getMessage().getContent().startsWith("cb-nick");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		try {
			String text = event.getMessage().getContent().split(" ")[1];
			event.getGuild().setUserNickname(client.getOurUser(), text);
		}catch (ArrayIndexOutOfBoundsException e) {
			sendMessage(event,"you need an argument");
		}catch (RateLimitException ef) {
			sendMessage(event,"you are updating it to fast");
		}
	}

	@Override
	public String getDisciption() {
		return "changes the nickname of the bot";
	}
}