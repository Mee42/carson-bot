package com.carson.commands.main.lavaplayer;

import java.util.List;

import com.carson.commandManagers.Command;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IVoiceChannel;

public class CommandLeave extends Command {

	public CommandLeave(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
		return content.equals("leave");
	}

	@Override
	public void run(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
		List<IVoiceChannel> channels = event.getGuild().getVoiceChannels();
		for(IVoiceChannel channel : channels) {
			if(channel.isConnected()) {
				channel.leave();
			}
		}

	}

	@Override
	public String getCommandId() {
		return "leavevoice";
	}
}
