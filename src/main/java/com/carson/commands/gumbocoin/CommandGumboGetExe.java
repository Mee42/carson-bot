package com.carson.commands.gumbocoin;

import java.io.File;
import java.io.IOException;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandGumboGetExe extends Command implements ICommand{

	public CommandGumboGetExe(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().toLowerCase().equals("~gumbo start");

	}

	@Override
	public void run(MessageReceivedEvent event) {
		try {
			File f = CommandGumboGetJar.download("https://github.com/Mee42/GumboCoin/blob/master/GumboCoin-all.exe?raw=true",CommandGumboGetJar.location + "GumboCoinMiner.exe");
			event.getChannel().sendFile(f);
			f.delete();
		} catch (IOException e) {
			e.printStackTrace();
			sendMessage(event,"sorry, couldn't get the file for you. You can download it on github, however, at: https://github.com/Mee42/GumboCoin");
		}
	}

	@Override
	public String getName() {
		return "~gumbo start";
	}

	@Override
	public String getDisciption() {
		return "download the miner and start mining!";
	}

}
