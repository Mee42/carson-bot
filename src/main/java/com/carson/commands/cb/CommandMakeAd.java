package com.carson.commands.cb;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;

public class CommandMakeAd extends Command implements ICommand{

	public CommandMakeAd(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().startsWith("cb-ad");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		String args[] = event.getMessage().getContent().split("&");
		if(args.length != 3) {
			sendMessage(event,"Incorrect args");
			for(String arg : args) {
				System.out.println(arg);
			}
			return;
		}
		for(IUser user : event.getGuild().getUsers()) {
			try {
				if(user.isBot()) {
					continue;
				}
				sendMessage(user.getOrCreatePMChannel(),args[1] + "\n\t" + args[2]);
			}catch(DiscordException e) {
				
			}
		}
	}

	@Override
	public String getName() {
		return "cb-ad^message^link";
	}

	@Override
	public String getDisciption() {
		return "set something to be advertised";
	}

}
