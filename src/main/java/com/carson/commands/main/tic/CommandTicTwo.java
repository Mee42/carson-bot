package com.carson.commands.main.tic;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.RequestBuffer;

public class CommandTicTwo extends Command implements ICommand{

	private TicObject t;
	
	public CommandTicTwo(IDiscordClient c, TicObject t) {
		super(c);
		this.t = t;
	}

	@Override
	public boolean test(MessageReceivedEvent event) {

		if(isInteger(event.getMessage().getContent()) && 
				Integer.valueOf(event.getMessage().getContent()) < 10) {
			return t.v.getActive();
		}
		return false;
	}

	@Override
	public void run(MessageReceivedEvent event) {
		String text = event.getMessage().getContent();
		
			
			String next = t.tac.moveNext(Integer.valueOf(text));
			client.getMessageByID(t.messageID).edit(next);
			
			if(t.tac.checkWin() == 1) {
				sendMessage(event.getChannel(), "I Won");
				t.v.setActive(false);
				t.tac = new Tac();
				t.moves = "";
			}else if(t.tac.checkWin() == 2) {
				sendMessage(event.getChannel(), "You Won! time to recode the entire stupid bot *fuckingsmarthuman*");
				t.v.setActive(false);
				t.tac = new Tac();
				System.out.println("ERROR: someone won tictactoe. shit. take a look at there sick moves:" + t.moves);
				sendMessage(client.getOrCreatePMChannel(client.getUserByID(293853365891235841L)), "ERROR: someone won tictactoe. shit. take a look at there sick moves:" + t.moves);
			}else if(t.tac.checkWin() == -1) {
				sendMessage(event.getChannel(), "It was a draw. Better luck next time");
				t.v.setActive(false);
				t.tac = new Tac();
				t.moves = "";
			}else {
				t.moves+=text;
				
				if(t.moves.length() >= 3 ) {
					sendMessage(event,next);
				}
			
				sendMessage(event.getChannel(), "What is your next move?");
				
			}
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisciption() {
		// TODO Auto-generated method stub
		return null;
	}
	private static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
	

}
