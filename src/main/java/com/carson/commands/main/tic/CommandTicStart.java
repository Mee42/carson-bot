package com.carson.commands.main.tic;

import com.carson.commandManagers.Command;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.Random;

public class CommandTicStart extends Command{

	
	
	private TicObject t;
	
	public CommandTicStart(IDiscordClient c, TicObject t) {
		super(c);
		this.t = t;
	}

	@Override
	public boolean test(MessageReceivedEvent event,String content, String[] args) {
		return content.equalsIgnoreCase("~tic");
	}

	@Override
	public void run(MessageReceivedEvent event,String content, String[] args) {
		if(t.v.getActive()) {
			sendMessage(event.getChannel(),"Sorry, Someone is using the Tic Tac Toe game right now.");
			System.out.println("EVENT:" +t.v.getInitalEvent().getAuthor().getName() + " is using the ticTacToe game right now, so " + event.getAuthor().getName() + " can't");
			return;
		}
		
		
		t.v.set(event);
		if(new Random().nextBoolean()) {
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
