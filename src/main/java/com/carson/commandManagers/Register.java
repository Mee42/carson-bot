package com.carson.commandManagers;

import java.util.ArrayList;
import java.util.List;

import com.carson.commands.cb.SubregisterCB;
import com.carson.commands.gumbocoin.SubregisterGumboCoin;
import com.carson.commands.main.SubregisterMain;
import com.carson.commands.gg.GGSubregister;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;


public class Register {
	
	private List<Subregister> subregisters = new ArrayList<Subregister>();
	
	public Register buildRegister(IDiscordClient c) {
		return new Register()
				.addSubregister(new SubregisterMain(c))
				.addSubregister(new SubregisterCB(c))
				.addSubregister(new SubregisterGumboCoin(c))
				.addSubregister(new GGSubregister(c))
				;
		
		
	}
	
	private Register addSubregister(Subregister s) {
		subregisters.add(s);
		return this;
	}

	public static Register build(IDiscordClient c) {
		Register r = new Register();
		return r.buildRegister(c);
	}
	
	
	
	public List<Subregister> getSubregisters(){
		return subregisters;
	}
	
	public List<ICommand> getCommands(){
		List<ICommand> commands = new ArrayList<ICommand>();
		
		for(Subregister r : subregisters) {
			System.out.println("calling subregister");
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
