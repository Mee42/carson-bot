package com.carson.commands.cb;

import com.carson.classes.TimeManager;
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
	public boolean test(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
		return content.equals("status");
	}



	@Override
	public void run(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
		String temp = "voice channels:\n";
		List<IVoiceChannel> vc = client.getConnectedVoiceChannels();
		for(IVoiceChannel v : vc) {
			temp+=v.getName() + " (in " + v.getGuild().getName() +" ) \n";
		}
		temp+="been up " + TimeManager.getSecondsUp() + " seconds in the last " + TimeManager.secondsSinceTime() + " seconds\n";
		temp+="uptime: " + (100*TimeManager.getUptime()) + "%\n";
		temp+="uptime today: " + TimeManager.getDay(TimeManager.getTodaysDay(),TimeManager.getTodaysYear()).uptime + "\n";
		temp+="uptime yesterday: " + TimeManager.getDay(TimeManager.getTodaysDay()-1,TimeManager.getTodaysYear()).uptime + "\n";

		sendMessage(event, temp);
		sendMessage(event, "minutes sense boot time: " + ((System.currentTimeMillis() - bootTime) / 60000) + "  hours:" + (((System.currentTimeMillis() - bootTime) / 60000)/60));
	}

	@Override
	public String getCommandId() {
		return "status";
	}

}
