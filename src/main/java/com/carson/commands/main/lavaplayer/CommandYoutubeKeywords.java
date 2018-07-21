package com.carson.commands.main.lavaplayer;

import com.carson.classes.Googler;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandYoutubeKeywords extends MusicCommand{

	public CommandYoutubeKeywords(IDiscordClient c, LavaplayerMain m) {
		super(c,m);
	}


	@Override
	public boolean test(MessageReceivedEvent event, String content, String[] args) {
		return content.startsWith("v~youtube");
	}

	@Override
	public void run(MessageReceivedEvent event, String content, String[] args) {
		System.out.println("EVENT: youtube played with keywords");
		String url = new Googler().GoogleYoutube(content.substring(10,content.length()));
		System.out.println(url);
		m.playLink(event.getChannel(),url);
	}

	@Override
	public String getName() {
		return "v~youtube *keywords*";
	}

	@Override
	public String getDisciption() {
		return "play a song from youtube";
	}

}
