package com.carson.commands.cb;

import com.carson.commandManagers.Command;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IVoiceChannel;

import java.util.List;

public class CommandStatus extends Command{

	
	private long bootTime = System.currentTimeMillis();
	
	public CommandStatus(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event,String content, String[] args) {
		return content.equals("cb-status");
	}

	@Override
	public void run(MessageReceivedEvent event,String content, String[] args) {
		String temp = "voice channels:\n";
		List<IVoiceChannel> vc = client.getConnectedVoiceChannels();
		for(IVoiceChannel v : vc) {
			temp+=v.getName() + " (in " + v.getGuild().getName() +" ) \n";
		}
		sendMessage(event, temp);
		sendMessage(event, "minutes sense boot time: " + ((System.currentTimeMillis() - bootTime) / 60000) + "  hours:" + (((System.currentTimeMillis() - bootTime) / 60000)/60));
		return;
	}

	@Override
	public String getName() {
		return "cb-status";
	}

	@Override
	public String getDisciption() {
		return "gets the current status of the bot";
	}

	@Override
	public PermissionLevel getWantedPermissionLevel() {
		return PermissionLevel.BOT_ADMIN;
	}
}
