package com.carson.commands.gg;

import com.carson.commandManagers.Subregister;

import sx.blah.discord.api.IDiscordClient;

public class GGSubregister extends Subregister {
	
	public GGSubregister(IDiscordClient c) {
		super(c);
		addCommand(new GGHandler(c));
	}
	
	
}
