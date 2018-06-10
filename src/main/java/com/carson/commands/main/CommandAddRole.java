package com.carson.commands.main;

import com.carson.classes.FileIO;
import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandAddRole extends Command implements ICommand {

	public CommandAddRole(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		if(event.getAuthor().getRolesForGuild(event.getGuild()).contains(client.getRoleByID(449914019550330901L)) | event.getAuthor().getLongID() == 293853365891235841L) {
			if(event.getMessage().getContent().startsWith("~addrole")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void run(MessageReceivedEvent event) {
		try {
			String roleStr = event.getMessage().getContent().split(" ")[1];
			event.getGuild().getRolesByName(roleStr).get(0);
			
			FileIO.use("/home/carson/java/files/brass-roles").add("\n" + roleStr);
			sendMessage(event, "added");
		}catch(Exception e) {
			sendMessage(event, "there was an error. try using ~addrole *role name*");
		}
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getDisciption() {
		return null;
	}

}
