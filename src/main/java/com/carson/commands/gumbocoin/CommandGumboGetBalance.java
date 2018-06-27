package com.carson.commands.gumbocoin;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandGumboGetBalance extends Command implements ICommand{

	public CommandGumboGetBalance(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().toLowerCase().startsWith("~gumbo get_balance");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContent().split(" ");
		if(args.length != 3) {
			sendMessage(event, "incorrect args");
		}
		String publicKey = args[2];
		String amount = Client.sendMessage("get:amount_for:" + publicKey);
		System.out.println(event,"heres what I got:```" + amount + +"```");
	} 

	@Override
	public String getName() {
		return "~gumbo get_balance [public_key]";
	}

	@Override
	public String getDisciption() {
		return "gets the amount of GumboCoins [public_key] has";
	}

}
