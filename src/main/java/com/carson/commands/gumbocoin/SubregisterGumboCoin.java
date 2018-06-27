package com.carson.commands.gumbocoin;

import com.carson.commandManagers.Subregister;

import sx.blah.discord.api.IDiscordClient;

public class SubregisterGumboCoin extends Subregister{

	

	public SubregisterGumboCoin(IDiscordClient c) {
		super(c);
		//TODO add commands here
		commands.add(new CommandGumboGetBalance(c));
	}

	

}
