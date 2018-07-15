package com.carson.commands.cb;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import com.carson.main.CleanThread;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandShutDown extends Command implements ICommand{

	public CommandShutDown(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return (event.getMessage().getContent().equals("cb-s") || event.getMessage().getContent().equals("cb-stop"));
	}

	@Override
	public void run(MessageReceivedEvent event) {
//		CleanThread.systemExit(0);
		sendMessage(event,"shutting down");
        new CleanThread(client).run();
		System.exit(0);
	}

	@Override
	public String getName() {
		return "cb-s, cb-stop";
	}

	@Override
	public String getDisciption() {
		return "shuts down the bot";
	}

}
