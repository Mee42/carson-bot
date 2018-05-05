
package com.carson.commandManagers;

import java.util.ArrayList;
import java.util.List;


import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class Subregister {

	
	protected List<ICommand> commands = new ArrayList<ICommand>();
	protected IDiscordClient client;
	
	public Subregister(IDiscordClient c) {
		client = c ;
	}



	public Subregister() {
		//used to create built busregisters
	}



	public Subregister build(IDiscordClient c) {
		return null;
	
	}
	
	
	
	public Subregister addCommand(ICommand c) {
		commands.add(c);
		return this;
	}
	
	
	
	public List<ICommand> getCommands(){
		return commands;
	}
	
	public void testCommands(MessageReceivedEvent  event) {
		for(ICommand c : commands) {
			if(c.test(event)) {
				c.run(event);
			}
		}
	}
	
	
	
	
	
	
}
