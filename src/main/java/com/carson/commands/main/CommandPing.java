package com.carson.commands.main;


import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

public class CommandPing extends Command implements ICommand {

	public CommandPing(IDiscordClient client) {
		super(client);
		
	}
	
	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().equals("~ping");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		long time = System.nanoTime();
		IMessage message = sendMessage(event.getChannel(), "pinging :ping_pong:");	
		long ping = System.nanoTime() - time;
		message.edit("pinged :ping_pong:   ping:" + ping + "nanoseconds");
		return ;
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
