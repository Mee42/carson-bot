package com.carson.commands.main.lavaplayer;

import java.util.List;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IVoiceChannel;

public class CommandLeave extends Command implements ICommand{

	public CommandLeave(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().equals("v~leave");
	}

	@Override
	public void run(MessageReceivedEvent event) {
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
