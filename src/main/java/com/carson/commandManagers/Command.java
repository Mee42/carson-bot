package com.carson.commandManagers;

import com.carson.classes.Messanger;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

public class Command{
	
	 protected IDiscordClient client;

	
	public Command(IDiscordClient c) {
		client = c;
	}
	
	
	protected IMessage sendMessage(IChannel c, String t) {
		return new Messanger(client).sendMessage(c, t);
	}
	
	protected void sendMessageClean(IChannel c, String t) {
		new Messanger(client).sendMessageClean(c, t);
	}
	protected IMessage sendMessage(MessageReceivedEvent event, String text) {
		return sendMessage(event.getChannel(), text);
	}
	
}
