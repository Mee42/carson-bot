package com.carson.commands.main;

import com.carson.commandManagers.*;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandXKCD extends Command implements ICommand{

	public CommandXKCD(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().startsWith("~xkcd");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		sendMessage(event, "sorry, XKCD is not set up yet. please check back later");
		return ;
	}

	@Override
	public String getName() {
		return  "~xkcd";
	}

	@Override
	public String getDisciption() {
		return "currently being developed. if this is a command you want, reach out to the dev and see if he just forgot to do it. He probably did";
	}

}
