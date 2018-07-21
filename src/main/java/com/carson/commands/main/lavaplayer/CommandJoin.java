package com.carson.commands.main.lavaplayer;

import com.carson.commandManagers.Command;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IVoiceChannel;

public class CommandJoin extends Command {

	public CommandJoin(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event,String content, String[] args) {
		return content.toLowerCase().startsWith("v~join");
	}

	@Override
	public void run(MessageReceivedEvent event,String content, String[] args) {
		System.out.println("EVENT:Joining voice channel");
		event.getGuild().getVoiceChannels().get(0).join();
		for(IVoiceChannel c : event.getGuild().getVoiceChannels()){
			if(c.getName().toLowerCase().matches("(?=.*m)(?=.*u)(?=.*s)(?=.*i)(?=.*c).+")){
				c.join();
			}
		}
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
