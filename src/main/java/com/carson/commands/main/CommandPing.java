package com.carson.commands.main;


import com.carson.classes.Messanger;
import com.carson.commandManagers.Command;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

public class CommandPing extends Command {

	public CommandPing(IDiscordClient client) {
		super(client);
		
	}
	
	@Override
	public boolean test(MessageReceivedEvent event,String content, String[] args) {
		return content.equals("~ping");
	}

	@Override
	public void run(MessageReceivedEvent event,String content, String[] args) {
		long time = System.nanoTime();
		IMessage message = new Messanger().sendMessageAndGet(event.getChannel(), "pinging :ping_pong:");	
		long ping = System.nanoTime() - time;
		message.edit("pinged :ping_pong:   ping:  " + (ping/1000000) + "  ms");
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
