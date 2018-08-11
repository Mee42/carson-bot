package com.carson.commands.main;

import com.carson.classes.FileIO;
import com.carson.commandManagers.Command;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;

import java.util.ArrayList;
import java.util.List;

public class CommandGetRole extends Command{



	public CommandGetRole(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
		if(event.getChannel().isPrivate()) return false;
		if(event.getGuild().getLongID() != 449905910807003147L)return false;
		return content.startsWith("getrole");
	}

	@Override
	public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {

		if(event.getChannel().getLongID() != 449912350267539456L) {
			sendMessage(event, "please be in <#449912350267539456>");
			return;
		}

		String[] messageSplit = content.split(" ",2);
		if(messageSplit.length != 2) {
			sendMessage(event,"there was an error. try ~getroles to see avalible roles");
			return;
		}

		String roleRequest= messageSplit[1];
		for(IRole role : getRoles(client)) {
			if(role.getName().equals(roleRequest)) {
				event.getAuthor().addRole(role);
				sendMessage(event, "added your role:" + roleRequest);
				return;
			}
		}
		sendMessage(event, "we could not find that role");
	}

	@Override
	public String getCommandId() {
		return "getrole";
	}

	public static List<IRole> getRoles(IDiscordClient client){
		
		List<String> roleStr = FileIO.use("/home/carson/java/files/brass-roles").readList();
		List<IRole> roles = new ArrayList<>();
		for(String str : roleStr) {
			for(IRole role : client.getGuildByID(449905910807003147L).getRolesByName(str)) {
				roles.add(role);
			}
		}
		return roles;
	}


}
