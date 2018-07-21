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
	public boolean test(MessageReceivedEvent event,String content, String[] args) {
		return content.equals("v~leave");
	}

	@Override
	public void run(MessageReceivedEvent event,String content, String[] args) {
		System.out.println("EVENT:leaving voice channel");
		List<IVoiceChannel> channels = event.getGuild().getVoiceChannels();
		for(IVoiceChannel channel : channels) {
			if(channel.isConnected()) {
				channel.leave();
			}
		}

	}

	@Override
	public String getName() {
		return "v~leave";
	}

	@Override
	public String getDisciption() {
		return "leaves the voice channel";
	}

}
