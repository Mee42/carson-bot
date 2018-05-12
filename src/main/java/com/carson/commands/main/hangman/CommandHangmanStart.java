package com.carson.commands.main.hangman;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandHangmanStart extends Command implements ICommand {  //TODO NEEDS TO BE REGISTARED

	private HangmanObject h;
	
	public CommandHangmanStart(IDiscordClient c,HangmanObject h) {
		super(c);
		this.h = h;
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().equals("~hangman");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		if(h.hangman.set(event)) {
			System.out.println("EVENT:starting hangman game with" + event.getAuthor().getName());
			sendMessage(event.getChannel(), "Nice! Now go check your DM's");
			sendMessage(event.getAuthor().getOrCreatePMChannel(), "What do you want your word to be?");
		}else {
			sendMessage(event.getChannel(), "Sorry, someone is already using hangman right now. ");
			System.out.println(event.getAuthor().getName() + " tryed to use the hangman bot, but " + h.hangman.getInitalEvent().getAuthor().getName()+ " is already using it");
		}
		
	}

	@Override
	public String getName() {
		return "~hangman";
	}

	@Override
	public String getDisciption() {
		return "starts a hangman game";
	}

}
