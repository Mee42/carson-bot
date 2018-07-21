package com.carson.commands.main.lavaplayer;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandSkip extends MusicCommand {

	
	public CommandSkip(IDiscordClient c, LavaplayerMain m) {
		super(c,m);
	}




	@Override
	public boolean test(MessageReceivedEvent event, String content, String[] args) {
		return content.startsWith("v~skip");
	}

	@Override
	public void run(MessageReceivedEvent event, String content, String[] args) {
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

	@Override
	public PermissionLevel getWantedPermissionLevel() {
		return PermissionLevel.USER;
	}

}
