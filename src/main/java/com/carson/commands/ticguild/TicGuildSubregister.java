package com.carson.commands.ticguild;

import com.carson.commandManagers.Subregister;

import sx.blah.discord.api.IDiscordClient;

public class TicGuildSubregister extends Subregister {
	
	public TicGuildSubregister(IDiscordClient c) {
		super(c);
		addCommand(new TicGuildGeneralHandler(c));
	}
	
	
}
