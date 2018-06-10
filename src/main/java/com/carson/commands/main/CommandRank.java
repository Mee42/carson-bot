package com.carson.commands.main;

import java.util.List;

import com.carson.commandManagers.*;
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
				builder.append(person.getName() + " has `" + CommandRank.getXPForUser(person.getLongID()) + "` XP\n");
				if(builder.length() > 1500) {
					builder.append("I can't send any more people as of right now, discord limits message length");
					break;
				}
			}
			sendMessage(event, builder.toString());
		}
	}
	
	public static long getXPForUser(long id) {
		GuildDataOrginizer guildData = DataGetter.getInstance();
		return guildData.getXPForUser(id);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisciption() {
		// TODO Auto-generated method stub
		return null;
	}

}
