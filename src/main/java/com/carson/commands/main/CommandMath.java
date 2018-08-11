package com.carson.commands.main;

import com.carson.commandManagers.Command;
import com.carson.classes.Math;
import com.carson.classes.Math.MathException;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandMath extends Command {

	public CommandMath(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
		return content.startsWith("math");
	}

	@Override
	public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
		if(args.length == 1) {
			sendMessage(event,"you need to have an equation");
		}
		String command = content.replaceFirst("math ", "");
		try {
			sendMessage(event,"`" + command + "` = `" + Math.format(new Math().calculate(command)) + "`");
		}catch(MathException e) {
			sendMessage(event,"could not be processed :eyes:");
		}
	}

	@Override
	public String getCommandId() {
		return "math";
	}
}
