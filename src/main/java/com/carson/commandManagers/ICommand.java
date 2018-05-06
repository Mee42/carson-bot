package com.carson.commandManagers;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public interface ICommand {

	public boolean test(MessageReceivedEvent event); //returns true if the command should be called
	public void run(MessageReceivedEvent event); //runs the command
	public String getName(); //gets the name of the command
	public String getDisciption(); //gets the discription of the command
	
}
