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
	public boolean test(MessageReceivedEvent event,String content, String[] args) {
		return content.startsWith("~google");
	}

	@Override
	public void run(MessageReceivedEvent event,String content, String[] args) {
		Googler googler = new Googler();
		sendMessage(event.getChannel(),"Done! Heres the first result:");
		sendMessage(event.getChannel(), googler.Google(content.substring(8,content.length())));

	}

	@Override
	public String getName() {
		return "~google *text*";
	}

	@Override
	public String getDisciption() {
		return "returns the first link you would have found if you googled *text*";
	}

}
