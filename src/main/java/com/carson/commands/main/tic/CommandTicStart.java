package com.carson.commands.main.tic;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandTicStart extends Command implements ICommand{

	
	
	private TicObject t;
	
	public CommandTicStart(IDiscordClient c, TicObject t) {
		super(c);
		this.t = t;
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().equalsIgnoreCase("~tic");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		if(t.v.getActive()) {
			sendMessage(event.getChannel(),"Sorry, Someone is using the Tic Tac Toe game right now.");
			System.out.println("EVENT:" +t.v.getInitalEvent().getAuthor().getName() + " is using the ticTacToe game right now, so " + event.getAuthor().getName() + " can't");
			return;
		}
		
		
		t.v.set(event);
		if((int)(Math.random()*2+1) == 2) {
			t.messageID = event.getChannel().sendMessage(t.tac.start(0)).getLongID();
		}else{
			t.messageID = event.getChannel().sendMessage(t.tac.start(-1)).getLongID();
		}
		t.moves = "";
		
	}

	@Override
	public String getName() {
		return "~tic";
	}	

	@Override
	public String getDisciption() {
		return "starts a tic tac toe game. try and win!";
	}
}
