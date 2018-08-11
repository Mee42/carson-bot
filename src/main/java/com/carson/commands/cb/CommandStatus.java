package com.carson.commands.cb;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.Register;
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
	public boolean test(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
		throw new RuntimeException();//should never happen
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		if(!hasPermission(getWantedPermissionLevel(), Register.getPermissionLevel(event)))return false;
		return event.getMessage().getContent().toLowerCase().equals("cb-status");
	}

	@Override
	public void run(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
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
	public String getCommandId() {
		return "status";
	}

	@Override
	public PermissionLevel getWantedPermissionLevel() {
		return PermissionLevel.BOT_ADMIN;
	}
}
