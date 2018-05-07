package com.carson.commands.cb;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandNick extends Command implements ICommand{
	  

	public CommandNick(IDiscordClient c) {
		super(c);
		// TODO Auto-generated constructor stub
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
		return event.getMessage().getContent().startsWith("cb-changeNick");

	}

	@Override
	public void run(MessageReceivedEvent event) {
		String text = event.getMessage().getContent().split(" ")[1];
		event.getGuild().setUserNickname(client.getOurUser(), text);
	}

	@Override
	public String getDisciption() {
		return "changes the nickname of the bot";
	}
}