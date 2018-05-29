package com.carson.commands.main.lavaplayer;

import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandYoutubeLink extends MusicCommand implements ICommand{

	public CommandYoutubeLink(IDiscordClient c, LavaplayerMain m) {
		super(c, m);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().toLowerCase().startsWith("v~link");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		String text = event.getMessage().getContent();
		String url = text.substring(7,text.length());
		m.playLink(event.getChannel(),url);
	}

	@Override
	public String getName() {
		return "v~link *link*";
	}

	@Override
	public String getDisciption() {
		return "starts playing the link. works with youtube, NO ADS! might also work for soundcloud";
	}

	
	
	
}
