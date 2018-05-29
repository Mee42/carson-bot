
package com.carson.commandManagers;

import java.util.ArrayList;
import java.util.List;

import com.carson.classes.SendHelp;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class Subregister {

	
	protected List<ICommand> commands = new ArrayList<ICommand>();
	protected IDiscordClient client;
	
	public Subregister(IDiscordClient c) {
		client = c ;
	}



	public Subregister() {
		//used to create built subregisters
	}



	public Subregister build(IDiscordClient c) {
		return null; //used to build subregisters
	}
	
	
	
	public Subregister addCommand(ICommand c) {
		commands.add(c);
		return this; //shouldn't really be used, as of right now, everthing should be hardcoded
	}
	
	
	
	public List<ICommand> getCommands(SendHelp.HelpTypes h){
		return commands;
	}
	
	public void testCommands(MessageReceivedEvent  event) {
		for(ICommand c : commands) {
			if(c.test(event)) {
				System.out.println("EVENT: running " + c.getName());
				c.run(event);
				return;
			}
		}
	}
	
	
	
	
	
	
}
