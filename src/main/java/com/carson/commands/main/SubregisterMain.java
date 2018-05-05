package com.carson.commands.main;

import com.carson.commandManagers.Subregister;
import com.carson.commands.main.*;

import sx.blah.discord.api.IDiscordClient;

public class SubregisterMain extends Subregister{

	public void buildSubregister(IDiscordClient c) {
		
	}
	
	@Override
	public Subregister build(IDiscordClient c) {
		return new SubregisterMain(c)
				.addCommand(new CommandPing(c))
				.addCommand(new CommandMemberCount(c))
				
				;
		
	}

	public SubregisterMain(IDiscordClient c) {
		super(c);
	}
	public SubregisterMain() {
		super();
	}
	
	
	
}
