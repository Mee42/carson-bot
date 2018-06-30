package com.carson.commands.ticguild;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;
import com.carson.commands.ticguild.TicData.Game;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class TicGuildGeneralHandler extends Command implements ICommand{

	public TicGuildGeneralHandler(IDiscordClient c) {
		super(c);
		t = new TicData();
	}
	TicData t;

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getGuild().getLongID() == 462681259370610689L;
	}

	@Override
	public void run(MessageReceivedEvent event) {
		
		String message = event.getMessage().getContent();
		
		
		switch(message) {
			case "get_games":
				String str1 = "_ _\n";
				for(Game g : t.games) {
					str1+=g.p1.getName() + "v" + g.p2.getName() + " turn:" + (g.p1Turn?"p1":"p2") + "\n";
				}
				sendMessage(event,str1);
		}
		
		if(event.getAuthor().getRolesForGuild(event.getGuild()).contains(event.getGuild().getRolesByName("Mod").get(0))) {
			mod(event);
		}
		
		//for playing
//		t.s.contains(event.getAuthor()) &&
		if(		t.getGameWith(event.getAuthor()) != null &&
				event.getChannel().getName().equals(String.valueOf(event.getAuthor().getLongID()))&&
					message.length() == 1) {
			
			if(message.matches("[0-9]")) {
//				sendMessage(event,"you said:`" + message + "`");
				Game g = t.getGameWith(event.getAuthor());
				if(g.isTurn(event.getAuthor())){
					IUser playing = g.getTurn();//the person playing
					IUser oof = g.p1Turn?g.p2:g.p1;//the person not playing
					int player = g.p1Turn?1:2;
					if(g.board.set(Integer.parseInt(message), g.p1Turn?1:2)) {
						sendMessage(event,"Worked!");
					}else {
						sendMessage(event,"oof you can't go there. skiping ur turn -_-");
					}
					sendMessage(event,g.board.printBoard());
					if(g.board.boardWin() == player) {
						sendMessage(event,"YOU WINNNNNN!!!!!");
						sendMessage(getChannel(oof),"you lost :*(\n" + g.board.printBoard());
						t.games.remove(g);
					}else if(g.board.full()) {
						sendMessage(getChannel(playing), "The game ended in a draw. :*(");
						sendMessage(getChannel(oof), "The game ended in a draw. :*(");
					}else {
						g.p1Turn = !g.p1Turn;
						sendMessage(
								getChannel(oof),
								"ur turn!\n" + g.board.printBoard()
										);
					}
					return;
				}else {
					sendMessage(event,"not your turn");
				}
				
			}
			
		}
	}

	private boolean mod(MessageReceivedEvent event) {
		if(event.getMessage().getContent().startsWith("start_game")){
			IUser p1 = event.getMessage().getMentions().get(0);
			IUser p2 = event.getMessage().getMentions().get(1);
			System.out.println(p1.getName() + p2.getName());
			if(!(t.getGameWith(p1) == null || t.getGameWith(p2) == null)) {
				sendMessage(event,"One or both of them is already in a game");
				return true;
			}
			Game g = t.new Game();
			g.p1 = p1;
			g.p2 = p2;
			t.addGame(g);
			sendMessage(event,"done");
			sendMessage(getChannel(g.getTurn()),"Its your turn!\n" + g.board.printBoard());
			return true;
		}
		return false;
	}

	@Override
	public String getName() {
		return "ticguild game";
	}

	@Override
	public String getDisciption() {
		return null;
	}
	
	private IChannel getChannel(IUser user) {
		return client.getGuildByID(462681259370610689L).getChannelsByName( //
				String.valueOf(user.getLongID())).get(0);
	}

}
