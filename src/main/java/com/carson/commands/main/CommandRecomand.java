package com.carson.commands.main;

import com.carson.classes.FileIO;
import com.carson.commandManagers.Command;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandRecomand extends Command{

	public CommandRecomand(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event, String content, String[] args) {
		return  content.startsWith("~recommend");
	}

	@Override
	public void run(MessageReceivedEvent event, String content, String[] args) {
		String[] text = event.getMessage().getContent().split(" ",2);
		if(text.length != 2) {
			sendMessage(event.getChannel(), "For some reason, I can't except this as an input. Remember, you can't use '-' in your recommendation. Please contact the dev if this continues to occur");
			return;
		}
		
		System.out.println("EVENT: recommended: " + text[1]);
		sendMessage(client.getOrCreatePMChannel(client.getUserByID(293853365891235841L)),text[1]);
	}

	@Override
	public String getName() {
		return "~recommend *text*";
	}

	@Override
	public String getDisciption() {
		return "recommend something to the dev. ";
	}

}
