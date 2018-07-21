package com.carson.commands.main.hangman;

import com.carson.commandManagers.Command;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandHangmanStart extends Command{

	private HangmanObject h;
	
	public CommandHangmanStart(IDiscordClient c,HangmanObject h) {
		super(c);
		this.h = h;
	}

	@Override
	public void run(MessageReceivedEvent event, String content, String[] args) {
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
	public boolean test(MessageReceivedEvent event, String content, String[] args) {
		return content.equals("~hangman");
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
