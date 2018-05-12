package com.carson.commands.main.hangman;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandHangmanOne  extends Command implements ICommand{

	private HangmanObject h;
	
	public CommandHangmanOne(IDiscordClient c,HangmanObject h) { //TODO add to registaer
		super(c);
		this.h = h;
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return h.hangman.verify(event);
	}

	@Override
	public void run(MessageReceivedEvent event) {
		System.out.println("EVENT: hangman word found");
		h.hangmanText = event.getMessage().getContent();
		sendMessage(event.getChannel(), "Great!");
		h.hangmanBoolean = true;
		String temp = "";
		String text = event.getMessage().getContent();
		
		for(int i = 0;i<text.length();i++) {
			h.hangmanDone.add('-');
		}
		
		temp = "";
		for(int q = 0;q<h.hangmanDone.size();q++) {
			temp = temp +" " + h.hangmanDone.get(q);
		}
		
		sendMessage(h.hangman.getInitalEvent().getChannel(),"the word is:```" + temp + "```");
		
		
	}

	@Override
	public String getName() {return null;}

	@Override
	public String getDisciption() {return null;}

}
