package com.carson.commands.main;

import java.io.File;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.Image;

public class CommandGetEmoji extends Command implements ICommand{

	public CommandGetEmoji(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().equals("cb-emoji");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		//makes the emoji
//		Image i = Image.forFile(new File("/home/carson/discord/files/carson-bot-emoji"));
		
//		client.getGuildByID(432560125299916810L).createEmoji("carson_bot", i, new IRole[0]);
		
		
		sendMessage(event, client.getGuildByID(432560125299916810L).getEmojiByName("carson_bot").toString());
	}

	@Override
	public String getName() {
		return "~emoji";
	}

	@Override
	public String getDisciption() {
		return "sends the carsonbot emoji";
	}
	
	
	

}
