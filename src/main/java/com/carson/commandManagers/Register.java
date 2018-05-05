package com.carson.commandManagers;

import java.util.ArrayList;
import java.util.List;

import com.carson.commands.main.SubregisterMain;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;


public class Register {
	
	private List<Subregister> subregisters = new ArrayList<Subregister>();
	
	public void buildRegister(IDiscordClient c) {
		subregisters.add(new SubregisterMain().build(c));
		
		
	}
	
	public static Register build(IDiscordClient c) {
		Register r = new Register();
		r.buildRegister(c);
		return r;
	}
	
	
	
	public List<Subregister> getSubregisters(){
		return subregisters;
	}
	
	public List<ICommand> getCommands(){
		List<ICommand> commands = new ArrayList<ICommand>();
		
		for(Subregister r : subregisters) {
			commands.addAll(r.getCommands());
		}
		return commands;
		
		
		
		
	}
	
	public void testCommands(MessageReceivedEvent  event) {
		for(Subregister r : subregisters) {
			r.testCommands(event);
		}
	}
	
	
	
	
	
	
	
}
