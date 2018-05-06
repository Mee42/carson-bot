package com.carson.commands.main.lavaplayer;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandJoin extends Command implements ICommand{

	public CommandJoin(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().equals("v~join");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		System.out.println("EVENT:Joining voice channel");
		event.getGuild().getVoiceChannels().get(0).join();
		event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel().join();
	}

	@Override
	public String getName() {
		return "v~join";
	}

	@Override
	public String getDisciption() {
		return "joins a voice channel";
	}

}
