package com.carson.commands.main;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandPing extends Command implements ICommand {

	
	
	public CommandPing(IDiscordClient client) {
		super(client);
		
	}
	
	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().equals("~ping");
	}

	@Override
	public boolean run(MessageReceivedEvent event) {
		sendMessage(event.getChannel(), "ping is:" + client.getShards().get(0).getResponseTime());		
		return true;
	}

	@Override
	public String getName() {
		return "~ping";
	}

	@Override
	public String getDisciption() {
		return "says the current shard ping";
	}

	
	
	
	
}
