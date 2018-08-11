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
    public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
		return content.startsWith("setleave");
	}

	@Override
	public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
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
	public PermissionLevel getWantedPermissionLevel() {
		return PermissionLevel.MOD;
	}

	@Override
	public String getCommandId() {
		return "setleave";
	}
}
