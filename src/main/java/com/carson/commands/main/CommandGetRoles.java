package com.carson.commands.main;

import com.carson.commandManagers.Command;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;

public class CommandGetRoles extends Command{

	public CommandGetRoles(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {

		return content.equals("getroles");
	}

	@Override
	public void run(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
		sendMessage(event, "use ~getrole *rolename* to get a role.\nroles:```" + new Object() { public String getStr() {String str = "";for(IRole role : CommandGetRole.getRoles(client)) {str+="\n" + role.getName();} return str;}}.getStr() + "```");
		//why did i do this in one line
	}

	@Override
	public String getCommandId() {
		return "getroles";
	}
}
