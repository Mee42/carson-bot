package com.carson.commands.gumbocoin;

import java.util.List;

import com.carson.commandManagers.ICommand;
import com.carson.commandManagers.Subregister;

import sx.blah.discord.api.IDiscordClient;

public class SubregisterGumboCoin extends Subregister{

	

	public SubregisterGumboCoin(IDiscordClient c) {
		super(c);
		//TODO add commands here
		commands.add(new CommandGumboGetBalance(c));
		commands.add(new CommandGumboGetPublicKey(c));
		commands.add(new CommandGumboSetPublicKey(c));
		commands.add(new CommandGumboGetJar(c));
		commands.add(new CommandGumboGetIP(c));
		commands.add(new CommandGumboManual(c));
		commands.add(new CommandGumboGetExe(c));
		commands.add(new CommandGumboGeneral(c));
		commands.add(new CommandGumboGetBlockchain(c));
	}
	
	
	@Override
	public List<ICommand> getCommands(){
		System.out.println("SubregisterGumboCoin getCommands called");
		return commands;
	}
	

}
