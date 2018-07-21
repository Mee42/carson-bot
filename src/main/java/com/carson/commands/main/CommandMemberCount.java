package com.carson.commands.main;

import com.carson.commandManagers.Command;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandMemberCount extends Command {

	public CommandMemberCount(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event, String content, String[] args) {
		if(event.getChannel().isPrivate()) return false;
		return content.equals("~members");

	}

	@Override
	public void run(MessageReceivedEvent event, String content, String[] args) {
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
