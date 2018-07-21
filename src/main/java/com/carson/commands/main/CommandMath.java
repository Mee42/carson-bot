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
	public boolean test(MessageReceivedEvent event, String content, String[] args) {
		return content.startsWith("~math");
	}

	@Override
	public void run(MessageReceivedEvent event, String content, String[] args) {
		if(args.length == 1) {
			sendMessage(event,"you need to have an equation");
		}
		String command = event.getMessage().getContent().replaceFirst("~math ", "");
		try {
			sendMessage(event,"`" + command + "` = `" + Math.format(new Math().calculate(command)) + "`");
		}catch(MathException e) {
			sendMessage(event,"could not be processed :eyes:");
		}
	}

	@Override
	public String getName() {
		return "~math *equation*";
	}

	@Override
	public String getDisciption() {
		return "solve a math equation! you can use ()*/-+^, uses order of operations.Try and fuck it up with a still-valid equation!";
	}


}
