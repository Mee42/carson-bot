package com.carson.commands.main;

import java.util.List;

import com.carson.commandManagers.ICommand;
import com.carson.commandManagers.Subregister;
import com.carson.commands.main.dnd.*;
import com.carson.commands.main.hangman.*;
import com.carson.commands.main.lavaplayer.*;
import com.carson.commands.main.tac.CommandContinueTac;
import com.carson.commands.main.tac.CommandTac;
import com.carson.commands.main.tic.*;

import sx.blah.discord.api.IDiscordClient;

public class SubregisterMain extends Subregister{


	
	public SubregisterMain(IDiscordClient c) {
		super(c);
		
		LavaplayerMain m = new LavaplayerMain();
		DndObject d = new DndObject();
		TicObject v = new TicObject();
		HangmanObject h = new HangmanObject();
		
				addCommand(new CommandPing(client));
				addCommand(new CommandMemberCount(client));
				addCommand(new CommandRecomand(client));
				addCommand(new CommandIFunny(client));
				addCommand(new CommandGoogle(client));
				addCommand(new CommandReddit(client));
				addCommand(new CommandXKCD(client));
				addCommand(new CommandPurge(client));
								
				addCommand(new CommandJoin(client));
				addCommand(new CommandLeave(client));
				addCommand(new CommandPlayLocal(client, m));
				addCommand(new CommandSkip(client,m));
				addCommand(new CommandYoutubeKeywords(client,m));
				addCommand(new CommandYoutubeLink(client,m));
				
				addCommand(new CommandDndStart(client,d));
				addCommand(new CommandDndTwo(client,d));
				
				addCommand(new CommandTicStart(client,v));
				addCommand(new CommandTicTwo(client,v));
				
				addCommand(new CommandHangmanStart(client,h));
				addCommand(new CommandHangmanOne(client,h));
				addCommand(new CommandHangmanTwo(client,h));
				
				addCommand(new CommandGetEmoji(client));
				
				addCommand(new CommandGetID(client));
								
				addCommand(new CommandGetRoles(client));
				
				addCommand(new CommandGetRole(client));
				
				addCommand(new CommandAddRole(client));
				
				addCommand(new CommandRank(client));
				addCommand(new CommandLeaderboard(client));
				addCommand(new CommandGetJson(client));
		
				addCommand(new CommandTac(client));
				addCommand(new CommandContinueTac(client));
				
				addCommand(new CommnadSetDeathMessage(client));
				
				addCommand(new CommandMath(client));
				addCommand(new CommandPrime(client));

				addCommand(new CommandSoftBan(client));

				addCommand(new CommandMuseScore(client));
	}


	
	@Override
	public List<ICommand> getCommands(){
		return commands;
	}
	
	
}
