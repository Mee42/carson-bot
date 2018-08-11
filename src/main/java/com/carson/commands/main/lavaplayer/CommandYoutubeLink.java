package com.carson.commands.main.lavaplayer;


import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandYoutubeLink extends MusicCommand {

	public CommandYoutubeLink(IDiscordClient c, LavaplayerMain m) {
		super(c, m);
	}

	@Override
	public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
		return content.startsWith("link");
	}

	@Override
	public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
		String url = content.substring("link".length());
		m.playLink(event.getChannel(),url);
	}//TODO check substring index


	@Override
	public String getCommandId() {
		return "youtube_link";
	}
}
