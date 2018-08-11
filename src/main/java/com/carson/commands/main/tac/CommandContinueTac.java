package com.carson.commands.main.tac;

import com.carson.commandManagers.Command;
import com.carson.dataObject.DataGetter;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandContinueTac extends Command{

	public CommandContinueTac(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
		long id = event.getAuthor().getLongID();
		
		if(!event.getMessage().getContent().matches("[0-9]")) {return false;}
		
		RunningTacGame game = DataGetter.getInstance().getGameWithUser(id);
		
		if(game == null) {
			return false;
		}
		return game.canPlay(id);
		
	}

	@Override
	public void run(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
		int move = Integer.parseInt(Character.toString(event.getMessage().getContent().charAt(0)));
		DataGetter data = DataGetter.getInstance();
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
	public String getCommandId() {
		return "continuetac";
	}
}
