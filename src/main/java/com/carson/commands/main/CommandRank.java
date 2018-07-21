package com.carson.commands.main;

import com.carson.commandManagers.Command;
import com.carson.dataObject.DBHandler;
import com.mongodb.client.model.Filters;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;

public class CommandRank extends Command {

	public CommandRank(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event, String content, String[] args) {
		return content.toLowerCase().startsWith("~rank");
	}

	@Override
	public void run(MessageReceivedEvent event, String content, String[] args) {
		String text = event.getMessage().getContent().toLowerCase();
		if(text.equals("~rank")) {//ranks for that one person
			sendMessage(event, "XP for " + event.getAuthor().getName() + " is:`" + CommandRank.getXPForUser(event.getAuthor().getLongID(),event.getGuild().getLongID()) + "`");
		}else {
			List<IUser> peopleMentioned =event.getMessage().getMentions();
			
			StringBuilder builder = new StringBuilder();
			for(IUser person : peopleMentioned) {
				String name = person.getName();
				if(name.length() > 30) {
					name = name.substring(0, 25);
				}
				builder.append(name + " has `" + CommandRank.getXPForUser(person.getLongID(),event.getGuild().getLongID()) + "` XP\n");
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
	
	public static long getXPForUser(long userId,long guildId) {
		return DBHandler.get().toGuildUserData(DBHandler.get().getUsersDB().find(Filters.eq("_id",userId + "" + guildId)).first()).getXp();
	}

	@Override
	public String getName() {
		return "~rank";
	}

	@Override
	public String getDisciption() {
		return "gets your rank (server-based). mention someone to get their rank";
	}

}
