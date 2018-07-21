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
	public boolean test(MessageReceivedEvent event, String content, String[] args) {
		return content.equals("~getroles");
	}

	@Override
	public void run(MessageReceivedEvent event, String content, String[] args) {
		sendMessage(event, "use ~getrole *rolename* to get a role.\nroles:```" + new Object() { public String getStr() {String str = "";for(IRole role : CommandGetRole.getRoles(client)) {str+="\n" + role.getName();} return str;}}.getStr() + "```");
	}

	@Override
	public String getName() {
		return "~getroles";
	}

	@Override
	public String getDisciption() {
		return "gets all available roles for the Brass Players' Discord";
	}

}
