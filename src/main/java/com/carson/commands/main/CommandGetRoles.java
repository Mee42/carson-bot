package com.carson.commands.main;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;

public class CommandGetRoles extends Command implements ICommand{

	public CommandGetRoles(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().equals("~getroles");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		sendMessage(event, "use ~getrole *rolename* to get a role.\nroles:```" + new Object() { public String getStr() {String str = "";for(IRole role : CommandGetRole.getRoles(client)) {str+="\n" + role.getName();} return str;}}.getStr() + "```");
	}

	@Override
	public String getName() {
		return "~getroles";
	}

	@Override
	public String getDisciption() {
		return "gets all avalible roles for the Brass Players' Discord";
	}

}
