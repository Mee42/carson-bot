package com.carson.commands.main;

import com.carson.commandManagers.Command;

import com.carson.dataObject.DBHandler;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandSetLeaveMessage extends Command{

	public CommandSetLeaveMessage(IDiscordClient c) {
		super(c);
	}

    @Override
    public boolean test(MessageReceivedEvent event, String content,String[] args) {
		return content.startsWith("~setleave");
	}

	@Override
	public void run(MessageReceivedEvent event, String content, String[] args) {
		String message = event.getMessage().getContent().substring("~setleave".length(), event.getMessage().getContent().length());
		DBHandler db = DBHandler.get();
		DBHandler.GuildData guild = db.getGuildDataBy(event.getGuild().getLongID());
		System.out.println("leave message set to: " + message);
		guild.setLeaveMessage(message);
		sendMessage(event, "the leave message was sent to " + message);
		guild.setLeaveChannel(event.getChannel().getLongID());
		db.update(guild);
	} 

	@Override
	public String getName() {
		return "~setleave *leave text*";
	}

	@Override
	public String getDisciption() {
		return "sets the message that is displayed every time someone leaves the server. use [name] to use their name. you must be the owner of the guild to use this. The channel you send this is will be the channel leave messages will be sent in.";
	}

	@Override
	public PermissionLevel getWantedPermissionLevel() {
		return PermissionLevel.MOD;
	}

}
