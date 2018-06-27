package com.carson.commands.gumbocoin;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandGumboManual extends Command implements ICommand {

	public CommandGumboManual(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().toLowerCase().startsWith("~gumbo manual");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		String command = event.getMessage().getContent().replaceAll("~gumbo manual ", "");
		String result = Client.sendMessage(command);
		if(result.length() > 1999) {
			sendMessage(event, "the result was bigger then 2,000 characters, so heres the first 2k");
			result = result.substring(0, 1998);
		}
		sendMessage(event,"result:```" + result + "```");
	
	}

	@Override
	public String getName() {
		return "~gumbo maunal [manual code]";
	}

	@Override
	public String getDisciption() {
		return "for manually interfacing with the server. Not recommended for normal people";
	}

}
