package com.carson.commands.main;

import com.carson.classes.Googler;
import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandGoogle extends Command implements ICommand {

	public CommandGoogle(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().startsWith("~google");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		//Google
		
		
			String text = event.getMessage().getContent();
			Googler googler = new Googler();
			sendMessage(event.getChannel(),"Done! Heres the first result:");
			sendMessage(event.getChannel(), googler.Google(text.substring(8,text.length())));
			
			
		return;
	}

	@Override
	public String getName() {
		return "~google *text*";
	}

	@Override
	public String getDisciption() {
		return "returns the first link you would have found if you googled *text*";
	}

}
