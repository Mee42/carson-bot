package com.carson.commands.main;

import com.carson.commandManagers.Subregister;

import sx.blah.discord.api.IDiscordClient;

public class SubregisterMain extends Subregister{


	
	@Override
	public Subregister build(IDiscordClient c) {
		return new SubregisterMain(c)
				.addCommand(new CommandPing(c))
				.addCommand(new CommandMemberCount(c))
				.addCommand(new CommandRecomand(c))
				.addCommand(new CommandIFunny(c))
				.addCommand(new CommandGoogle(c))
				.addCommand(new CommandReddit(c))
				.addCommand(new CommandXKCD(c))
				;
		
	}

	public SubregisterMain(IDiscordClient c) {
		super(c);
	}
	public SubregisterMain() {
		super();
	}
	
	
	
}
