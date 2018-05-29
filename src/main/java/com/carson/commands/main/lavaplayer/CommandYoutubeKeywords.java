package com.carson.commands.main.lavaplayer;

import com.carson.classes.Googler;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandYoutubeKeywords extends MusicCommand implements ICommand{

	public CommandYoutubeKeywords(IDiscordClient c, LavaplayerMain m) {
		super(c,m);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().startsWith("v~youtube");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		String text = event.getMessage().getContent();

		System.out.println("EVENT: youtube played with keywords");
		String url = new Googler().GoogleYoutube(text.substring(10,text.length()));
		System.out.println(url);
		m.playLink(event.getChannel(),url);
	}

	@Override
	public String getName() {
		return "v~youtube *keywords*";
	}

	@Override
	public String getDisciption() {
		// TODO Auto-generated method stub
		return null;
	}

}
