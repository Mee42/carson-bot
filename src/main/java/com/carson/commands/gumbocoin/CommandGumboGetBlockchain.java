package com.carson.commands.gumbocoin;

import java.io.File;
import java.io.FileNotFoundException;

import com.carson.classes.Messanger;
import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.RequestBuffer;

public class CommandGumboGetBlockchain extends Command implements ICommand{

	public CommandGumboGetBlockchain(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().toLowerCase().equals("~gumbo blockchain");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		File blockchain = new File("/home/carson/blockchain");
		File tempFile = new File("/home/carson/blockchain.txt");
		blockchain.renameTo(tempFile);
		IMessage temp = new Messanger().sendMessageAndGet(event.getChannel(), "wait a second while I get it...");
		RequestBuffer.request(() -> {
			try {
				event.getChannel().sendFile(tempFile);
			} catch (FileNotFoundException e) {
				sendMessage(event,"there was a problem getting it");
			}
		}).get();
		temp.delete();
		tempFile.renameTo(blockchain);
		
	}

	@Override
	public String getName() {
		return "~gumbo blockchain";
	}

	@Override
	public String getDisciption() {
		return "get the current blockchain";
	}

}
