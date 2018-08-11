package com.carson.commands.main;

import com.carson.classes.Googler;
import com.carson.commandManagers.Command;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandGoogle extends Command  {

	public CommandGoogle(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
		return content.startsWith("google");
	}

	@Override
	public void run(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
		Googler googler = new Googler();
		sendMessage(event.getChannel(),"Done! Heres the first result:");
		sendMessage(event.getChannel(), googler.Google(rawContent.substring(8,content.length())));

	}

	@Override
	public String getCommandId() {
		return "google";
	}
}
