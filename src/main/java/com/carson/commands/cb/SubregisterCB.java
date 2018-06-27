package com.carson.commands.cb;


import java.util.List;

import com.carson.commandManagers.ICommand;
import com.carson.commandManagers.Subregister;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class SubregisterCB extends Subregister{

	
	
	
	public  SubregisterCB(IDiscordClient c) {
		super(c);
		addCommand(new CommandStatus(client));
		addCommand(new CommandSetPlaying(client));
		addCommand(new CommandNick(client));
		addCommand(new CommandRestart(client));
		addCommand(new CommandShutDown(client));
	}
	

	@Override
	public List<ICommand> getCommands(){
		System.out.println("SubregisterCB getCommands called");
		return commands;
	}
	
	
	@Override
	public void testCommands(MessageReceivedEvent  event) {
		
		long id = event.getAuthor().getLongID();
		
		if(!(id == 317104272405823489L || id == 317104272405823489L || id == 293853365891235841L)) {
			return;
		}
		
		for(ICommand c : commands) {
			if(c.test(event)) {
				System.out.println("EVENT: running " + c.getName());
				c.run(event);
				return;
			}
		}		
	}
}
