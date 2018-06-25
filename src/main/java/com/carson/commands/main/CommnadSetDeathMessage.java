package com.carson.commands.main;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;
import com.carson.dataObject.DataGetter;
import com.carson.dataObject.GuildData;
import com.carson.dataObject.GuildDataOrginizer;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommnadSetDeathMessage extends Command implements ICommand{

	public CommnadSetDeathMessage(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		if(!event.getMessage().getContent().toLowerCase().startsWith("~setleave")) {
			return false;
		}
		if(event.getAuthor().getLongID() != event.getGuild().getOwner().getLongID()) {
			return false;
		}
		return true;
	}

	@Override
	public void run(MessageReceivedEvent event) {
		String message = event.getMessage().getContent().substring("~setleave".length(), event.getMessage().getContent().length());
		GuildDataOrginizer data = DataGetter.getInstance();
		GuildData guild = data.getGuild(event.getGuild().getLongID());
		System.out.println("death message set to: " + message);
		guild.setDeathMessage(message);
		sendMessage(event, "the leave message was sent to " + message);
		guild.setDeathChannel(event.getChannel().getLongID());
	} 

	@Override
	public String getName() {
		return "~setleave *leave text*";
	}

	@Override
	public String getDisciption() {
		return "sets the message that is displayed every time someone leaves the server. use [name] to use their name. you must be the owner of the guild to use this. The channel you send this is will be the channel leave messages will be sent in.";
	}

}
