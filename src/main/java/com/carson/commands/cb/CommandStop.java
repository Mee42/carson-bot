package com.carson.commands.cb;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandStop extends Command implements ICommand{

	public CommandStop(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return (event.getMessage().getContent().equals("cb-s") || event.getMessage().getContent().equals("cb-stop"));
	}

	@Override
	public void run(MessageReceivedEvent event) {
		sendMessage(event, "Shuting down");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisciption() {
		// TODO Auto-generated method stub
		return null;
	}

}
