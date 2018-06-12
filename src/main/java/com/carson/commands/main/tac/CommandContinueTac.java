package com.carson.commands.main.tac;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;
import com.carson.dataObject.DataGetter;
import com.carson.dataObject.GuildDataOrginizer;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandContinueTac extends Command implements ICommand {

	public CommandContinueTac(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		long id = event.getAuthor().getLongID();
		
		if(!event.getMessage().getContent().matches("[0-9]")) {System.err.println("TACDEBUG: regex failure");return false;}
		
		GuildDataOrginizer data = DataGetter.getInstance();
		RunningTacGame game = data.getGameWithUser(id);
		
		if(game == null) {
			return false;
		}
		return game.canPlay(id);
		
	}

	@Override
	public void run(MessageReceivedEvent event) {
		int move = Integer.parseInt(Character.toString(event.getMessage().getContent().charAt(0)));
		GuildDataOrginizer data = DataGetter.getInstance();
		RunningTacGame game = data.getGameWithUser(event.getAuthor().getLongID());
		if(!game.play(move)) {
			sendMessage(event, "you couldn't go. remember - sending an invalid location will skip your turn it is now " +
					client.getUserByID(		(game.getTurn() == 1)? game.getIdP1() : game.getIdP2()).mention()
					+ "'s turn"
					);
			sendMessage(event, game.getPlayingMessage());

		}else {
			if(game.hasBeenWon() == 0) {
				sendMessage(event,  "it is now " +
					client.getUserByID(		(game.getTurn() == 1)? game.getIdP1() : game.getIdP2()).mention()
					+ "'s turn"
					);
				sendMessage(event, game.getPlayingMessage());
			}else if(game.hasBeenWon() == 1) {
				sendMessage(event, client.getUserByID(game.getIdP1()).mention() + " WON!!!!!" );
				data.endGame(game);
			}else if(game.hasBeenWon() == 2) {
				sendMessage(event, client.getUserByID(game.getIdP2()).mention() + " WON!!!!!" );
				data.endGame(game);
			}else if(game.hasBeenWon() == -1) {
				sendMessage(event, "The board is full. It was a tie :*(");
				data.endGame(game);
			}
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

}
