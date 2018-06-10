package com.carson.commands.main;

import java.util.ArrayList;
import java.util.List;

import com.carson.classes.FileIO;
import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;

public class CommandGetRole extends Command implements ICommand{

	public CommandGetRole(IDiscordClient c) {
		super(c);
		
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		if(event.getGuild().getLongID() != 449905910807003147L){return false;}
		if(event.getMessage().getContent().startsWith("~getrole")){
			return true;
		}
		return false;
	}

	@Override
	public void run(MessageReceivedEvent event) {
		if(event.getChannel().getLongID() != 449912350267539456L) {
			sendMessage(event, "please be in <#449912350267539456>");
			return;
		}
			String[] messageSplit = event.getMessage().getContent().split(" ",2);
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

	@Override
	public String getName() {
		return "v~getrole *role_name*";
	}

	@Override
	public String getDisciption() {
		return "assigns yourself a role from a list. only on Brass Players' Discord";
	}

}
