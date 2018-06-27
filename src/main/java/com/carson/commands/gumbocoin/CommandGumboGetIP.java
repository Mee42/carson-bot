package com.carson.commands.gumbocoin;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandGumboGetIP extends Command implements ICommand{

	public CommandGumboGetIP(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().toLowerCase().equals("~gumbo get_ip");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		sendMessage(event,"ip should be:`192.168.10.202`");
	}

	@Override
	public String getName() {
		return "~gumbo get_ip";
	}

	@Override
	public String getDisciption() {
		return "get the ip of the blockchain server to connect to";
	}

}
