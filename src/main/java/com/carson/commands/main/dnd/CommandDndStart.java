package com.carson.commands.main.dnd;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandDndStart extends Command implements ICommand{

	private DndObject d;
	
	public CommandDndStart(IDiscordClient c, DndObject d) {
		super(c);
		this.d = d;
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		if(!event.getMessage().getContent().equals("~dnd")) 
			return false;
		
		return !d.v.getActive();
	}

	@Override
	public void run(MessageReceivedEvent event) {
		sendMessage(event,"what dice do you want to role?(format: XdY, X and Y can be multi-digit");
		d.v.set(event);
		
	}

	@Override
	public String getName() {
		return "~dnd";
	}

	@Override
	public String getDisciption() {
		return "use dnd dies";
	}

}
