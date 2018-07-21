package com.carson.commands.main;


import com.carson.commandManagers.Command;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandGetEmoji extends Command {

	public CommandGetEmoji(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event, String content, String[] args) {
		return 	content.equals("~emoji");
	}

	@Override
	public void run(MessageReceivedEvent event, String content, String[] args) {

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
