package com.carson.commands.cb;

import com.carson.commandManagers.Subregister;

import sx.blah.discord.api.IDiscordClient;

public class SubregisterCB extends Subregister{

	
	@Override
	public Subregister build(IDiscordClient c) {
		return new SubregisterCB()
				.addCommand(new CommandStatus(c))
				.addCommand(new CommandSetPlaying(c))
				;
	}
	
	
	public  SubregisterCB(IDiscordClient c) {
		super(c);
	}
	
	public SubregisterCB() {
		super();
	}
}
