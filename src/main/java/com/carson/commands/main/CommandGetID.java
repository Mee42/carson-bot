package com.carson.commands.main;

import java.util.ArrayList;
import java.util.List;

import com.carson.commandManagers.Command;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class CommandGetID extends Command {

	public CommandGetID(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
		return content.startsWith("id");
	}

	@Override
	public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
		try {
			String message = "";
			List<IUser> users = event.getMessage().getMentions();
			List<IRole> roles = event.getMessage().getRoleMentions();
			List<IUser> allUsers = event.getGuild().getUsers();

			List<IUser> toDelete = new ArrayList<IUser>();

			for (IUser user : users) {
				for (IUser allUser : allUsers) {
					if (user.equals(allUser)) {
						toDelete.add(allUser);
					}
				}
			}
			allUsers.removeAll(toDelete);


			for (IUser user : allUsers) {
				for (IRole role : roles) {
					if (user.hasRole(role)) {
						users.add(user);
						toDelete.add(user);
						break;
					}
				}

			}

			sendMessage(event, users.size() + " users found");
			for (IUser user : users) {
				message += user.getDisplayName(event.getGuild()) + " has an id of: `" + user.getStringID() + "`\n";
				if (message.length() > 1800) {
					sendMessage(event, message);
					message = ".";
				}
			}
			if (!message.equals("")) {
				sendMessage(event, message);
			} else {
				sendMessage(event, "no users found");
			}
		} catch (IndexOutOfBoundsException e) {
			sendMessage(event, "yeah... no. that doesn't work");
		}
	}

	@Override
	public String getCommandId() {
		return "id";
	}
}
