package com.carson.commands.main.lavaplayer;

import com.carson.classes.Googler;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandYoutubeKeywords extends MusicCommand{

	public CommandYoutubeKeywords(IDiscordClient c, LavaplayerMain m) {
		super(c,m);
	}


	@Override
	public boolean test(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
		return content.startsWith("youtube");
	}

	@Override
	public void run(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
		String url = new Googler().GoogleYoutube(content.replaceFirst("youtube","").replace(" ",""));
		System.out.println(url);
		m.playLink(event.getChannel(),url);
	}

	@Override
	public String getCommandId() {
		return "youtubekeyword";
	}
}
