package com.carson.commands.cb;

import java.util.ArrayList;
import java.util.List;

import com.carson.classes.SendHelp;
import com.carson.commandManagers.ICommand;
import com.carson.commandManagers.Subregister;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class SubregisterCB extends Subregister{

	
	@Override
	public Subregister build(IDiscordClient c) {
		return new SubregisterCB()
				.addCommand(new CommandStatus(c))
				.addCommand(new CommandSetPlaying(c))
				.addCommand(new CommandNick(c))
				.addCommand(new CommandRestart(c))
				;
	}
	
	
	public  SubregisterCB(IDiscordClient c) {
		super(c);
	}
	
	public SubregisterCB() {
		super();
	}
	
	@Override
	public List<ICommand> getCommands(SendHelp.HelpTypes h) {
		if(h.equals(SendHelp.HelpTypes.ADMIN)) {
			return commands;
		}
		return new ArrayList<ICommand>();
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
