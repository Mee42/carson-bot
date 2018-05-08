package com.carson.commands.main;

import com.carson.commandManagers.Subregister;
import com.carson.commands.main.dnd.*;
import com.carson.commands.main.dnd.DndObject;
import com.carson.commands.main.lavaplayer.*;
import com.carson.commands.main.ps.CommandPS;

import sx.blah.discord.api.IDiscordClient;

public class SubregisterMain extends Subregister{


	
	public Subregister build(IDiscordClient c) {
		
		LavaplayerMain m = new LavaplayerMain();
		DndObject d = new DndObject();
		
		return new SubregisterMain(c)
				.addCommand(new CommandPing(c))
				.addCommand(new CommandMemberCount(c))
				.addCommand(new CommandRecomand(c))
				.addCommand(new CommandIFunny(c))
				.addCommand(new CommandGoogle(c))
				.addCommand(new CommandReddit(c))
				.addCommand(new CommandXKCD(c))
				.addCommand(new CommandPS(c))
				.addCommand(new CommandPurge(c))
				
				.addCommand(new CommandJoin(c))
				.addCommand(new CommandLeave(c))
				.addCommand(new CommandPlayLocal(c, m))
				.addCommand(new CommandSkip(c,m))
				.addCommand(new CommandYoutubeKeywords(c,m))
				.addCommand(new CommandYoutubeLink(c,m))
				
				.addCommand(new CommandDndStart(c,d))
				.addCommand(new CommandDndTwo(c,d))
				;
		
	}

	public SubregisterMain(IDiscordClient c) {
		super(c);
	}
	public SubregisterMain() {
		super();
	}
	
	
	
}
