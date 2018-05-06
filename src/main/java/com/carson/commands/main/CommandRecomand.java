package com.carson.commands.main;

import com.carson.classes.FileIO;
import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandRecomand extends Command implements ICommand{

	public CommandRecomand(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		try {
			return event.getMessage().getContent().split("-")[0].equals("~recommend");
		}catch (ArrayIndexOutOfBoundsException e) { //this is thrown if there is no text in the message
			return false;
		}
		
	}

	@Override
	public void run(MessageReceivedEvent event) {
		String[] text = event.getMessage().getContent().split("-");
		if(text.length > 2) {
			sendMessage(event.getChannel(), "For some reason, I can't exept this as an input. Remember, you can't use '-' in your recommendation. Please contact the dev if this continues to occur");
		}
		
		System.out.println("EVENT: recommended: " + text[1]);
		
		FileIO fio = new FileIO("recommendations.txt");
		fio.create();
		fio.add("\n"+event.getAuthor().getName() + ":" + text[1]);
		
		
		
		return ;
	}

	@Override
	public String getName() {
		return "~recommend-*text*";
	}

	@Override
	public String getDisciption() {
		return "recommend something to the dev. "
				+ "Will take a while to respond, dm'ing is much more effective. "
				+ "Make sure to use a hyphen between *~recommend* and *text*, and to not use any hyphens in *text*.";
	}

}
