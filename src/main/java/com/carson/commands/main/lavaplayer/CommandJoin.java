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
	public boolean test(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
		return content.toLowerCase().equals("join");
	}

	@Override
	public void run(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
		event.getGuild().getVoiceChannels().get(0).join();//join the first channel
		for(IVoiceChannel c : event.getGuild().getVoiceChannels()){//attempt to join every channel called music. overrides channel 0 (if found)
			if(c.getName().toLowerCase().matches("(?=.*m)(?=.*u)(?=.*s)(?=.*i)(?=.*c).+")){
				c.join();
			}
		}
		try {
			event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel().join();//try and join the channel the user is in. overrides the other joiners if found
		}catch(NullPointerException e){

		}
	}

	@Override
	public String getCommandId() {
		return "join";
	}
}
