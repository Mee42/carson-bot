package com.carson.commands.main.lavaplayer;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandSkip extends MusicCommand {

	
	public CommandSkip(IDiscordClient c, LavaplayerMain m) {
		super(c,m);
	}




	@Override
	public boolean test(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
//		return rawContent.startsWith("skip");
		return content.equals("skip");
	}

	@Override
	public void run(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
		m.skipTrack(event.getChannel());
	}

	@Override
	public String getCommandId() {
		return "skip";
	}
}
