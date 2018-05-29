package com.carson.commands.cb;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;

public class CommandSetPlaying extends Command implements ICommand{

	public CommandSetPlaying(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().startsWith("cb-setPlaying");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		try {
			String text = event.getMessage().getContent().split("$")[1];
			if(text.equals("watching")) {
				client.changePresence(StatusType.ONLINE, ActivityType.WATCHING," your every move");
				System.out.println("EVENT: presence changed to Watching your every move");
			}else {
				client.changePresence(StatusType.ONLINE, ActivityType.PLAYING," " + text);
				System.out.println("EVENT: presence changed to Playing " + text);
			}
			sendMessage(event, "changed presence");
			
			
		}catch( ArrayIndexOutOfBoundsException e) {
			sendMessage(event, "incorrect args. use $ in front of *text*");
		}
		
		
	}

	@Override
	public String getName() {
		return "cb-setPlayer$*text*";
	}

	@Override
	public String getDisciption() {
		return "sets the now playing message to *text*. use *watching* for **now watching your every move**";
	}

}
