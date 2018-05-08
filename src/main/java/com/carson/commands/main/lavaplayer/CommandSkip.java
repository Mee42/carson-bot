package com.carson.commands.main.lavaplayer;

import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandSkip extends MusicCommand implements ICommand {

	
	public CommandSkip(IDiscordClient c, LavaplayerMain m) {
		super(c,m);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().equals("v~skip");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		m.skipTrack(event.getChannel());

	}

	@Override
	public String getName() {
		return "v~skip";
	}

	@Override
	public String getDisciption() {
		return "skips the current song. voting system coming soon";
	}

}
