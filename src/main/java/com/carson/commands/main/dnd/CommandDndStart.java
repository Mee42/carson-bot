package com.carson.commands.main.dnd;

import com.carson.commandManagers.Command;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandDndStart extends Command {

	private DndObject d;
	
	public CommandDndStart(IDiscordClient c, DndObject d) {
		super(c);
		this.d = d;
	}

	@Override
	public boolean test(MessageReceivedEvent event,String content, String[] args) {
		if(!content.equals("~dnd"))return false;
		return !d.v.getActive();
	}

	@Override
	public void run(MessageReceivedEvent event,String content, String[] args) {
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
