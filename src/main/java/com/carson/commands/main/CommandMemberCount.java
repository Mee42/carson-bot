package com.carson.commands.main;

import com.carson.commandManagers.*;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandMemberCount extends Command implements ICommand{

	public CommandMemberCount(IDiscordClient c) {
		super(c);
	}


	@Override
	public boolean test(MessageReceivedEvent event) {
		if(event.getChannel().isPrivate()) return false;
		return event.getMessage().getContent().equals("~members");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		sendMessage(event.getChannel(), " Current Member count:**" + event.getGuild().getTotalMemberCount() + "**! ");
		return ;
	}

	@Override
	public String getName() {
		return "~members";
	}

	@Override
	public String getDisciption() {
		return "tells you how many members are in the current server";
	}

}
