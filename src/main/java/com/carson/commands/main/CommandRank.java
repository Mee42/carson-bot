package com.carson.commands.main;

import java.util.List;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;
import com.carson.dataObject.DataGetter;
import com.carson.dataObject.GuildDataOrginizer;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

public class CommandRank extends Command implements ICommand{

	public CommandRank(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().toLowerCase().startsWith("~rank");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		String text = event.getMessage().getContent().toLowerCase();
		if(text.equals("~rank")) {//ranks for that one person
			sendMessage(event, "XP for " + event.getAuthor().getName() + " is:`" + CommandRank.getXPForUser(event.getAuthor().getLongID()) + "`");
		}else {
			List<IUser> peopleMentioned =event.getMessage().getMentions();
			
			StringBuilder builder = new StringBuilder();
			for(IUser person : peopleMentioned) {
				String name = person.getName();
				if(name.length() > 30) {
					name = name.substring(0, 25);
				}
				builder.append(name + " has `" + CommandRank.getXPForUser(person.getLongID()) + "` XP\n");
				if(builder.length() > 1500) {
					builder.append("I can't send any more people as of right now, discord limits message length");
					break;
				}
			}
			if(builder.length() != 0) {
				sendMessage(event, builder.toString());
			}else {
				sendMessage(event, "no one could be found");
			}
		}
	}
	
	public static long getXPForUser(long id) {
		GuildDataOrginizer guildData = DataGetter.getInstance();
		return guildData.getXPForUser(id);
	}

	@Override
	public String getName() {
		return "~rank";
	}

	@Override
	public String getDisciption() {
		return "gets your rank (global, not server-based). mention someone to get their rank";
	}

}
