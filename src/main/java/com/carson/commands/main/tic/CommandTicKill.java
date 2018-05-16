package com.carson.commands.main.tic;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandTicKill extends Command implements ICommand {

	public CommandTicKill(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return (event.getAuthor().getStringID() == "293853365891235841" && event.getMessage().getContent().equals("cb-tic-kill"));
	}

	@Override
	public void run(MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		return "not done";
	}

	@Override
	public String getDisciption() {
		return "needs to be done";
	}

}
