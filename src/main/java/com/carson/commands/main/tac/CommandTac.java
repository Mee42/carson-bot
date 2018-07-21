package com.carson.commands.main.tac;

import java.util.List;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;
import com.carson.dataObject.DataGetter;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

public class CommandTac extends Command implements ICommand {

	public CommandTac(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().toLowerCase().startsWith("~tac");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		List<IUser> mentions = event.getMessage().getMentions();
		if(mentions.size() != 1) {
			sendMessage(event, "make sure you are only mentioning one person");
			return;
		}
		
		
		long player2 = mentions.get(0).getLongID();
		long id = event.getAuthor().getLongID();
		DataGetter data = DataGetter.getInstance();
		RunningTacGame game = data.getGameWithUser(id);
		if(game != null) {
			if(game.getIdP1() == id || game.getIdP2() == id) {
				sendMessage(event, "uh oh. It looks like you're already in a game!");
			}else if(game.getIdP1() == id || game.getIdP2() == id) {
				sendMessage(event, "uh oh. It looks like that person is already in a game!");
			}
			return;
		}
		game = new RunningTacGame(id,player2); //we create a new game for the 2 players
		data.registerGame(game);
		sendMessage(event, game.getPlayingMessage());
		
	}

	@Override
	public String getName() {
		return "~tac @Mention";
	}

	@Override
	public String getDisciption() {
		return "play a game of tic tac toe with someone!";
	}
	

}
