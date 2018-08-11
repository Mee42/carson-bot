package com.carson.commands.main;

import com.carson.commandManagers.Command;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandMemberCount extends Command {

	public CommandMemberCount(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
		if(event.getChannel().isPrivate()) return false;
		return content.equals("members");

	}

	@Override
	public void run(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
		sendMessage(event.getChannel(), " Current Member count:**" + event.getGuild().getTotalMemberCount() + "**! ");
		return ;
	}

	@Override
	public String getCommandId() {
		return "membercount";
	}
}
