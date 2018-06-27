package com.carson.commands.gumbocoin;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;
import com.carson.dataObject.DataGetter;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandGumboSetPublicKey extends Command implements ICommand{

	public CommandGumboSetPublicKey(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().toLowerCase().startsWith("~gumbo set_key");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContent().split(" ");
		if(args.length != 3) {
			sendMessage(event, "it looks like you have incorrect arguments");
			return;
		}
		String pKey = args[2];
		String result = Client.sendMessage("get:user_exists:" + pKey);
		if(!result.equals("true")) {
			sendMessage(event, "looks like your public key may not have been register to the server (with the jar), or there was an error");
			return;
		}
		DataGetter.getInstance().getUserData(event.getAuthor().getLongID()).publicKey = pKey;
		
		sendMessage(event,"Sucsess! your public key was registered as:" + pKey);
	}

	@Override
	public String getName() {
		return "~gumbo set_key [publicKey]";
	}

	@Override
	public String getDisciption() {
		return "set your public key so others can get it by @Mentioning you";
	}

}
