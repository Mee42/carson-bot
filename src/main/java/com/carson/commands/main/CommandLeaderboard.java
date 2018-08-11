package com.carson.commands.main;

import com.carson.commandManagers.Command;
import com.carson.dataObject.DBHandler;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CommandLeaderboard extends Command{

	public CommandLeaderboard(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
		return (content.toLowerCase().equals("lb") ||
				content.toLowerCase().equals("leaderboard"));
	}

	@Override
	public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.withAuthorName("Carson-Bot");
		Random rand = new Random();
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		Color randomColor = new Color(r, g, b);
		builder.withColor(randomColor);
		builder.withDesc("leaderboard for " + event.getGuild().getName());

        DBHandler db = DBHandler.get();
		List<DBHandler.GuildUserData> users = db.getGuildUserDataForGuild(event.getGuild().getLongID());
		
		List<DBHandler.GuildUserData> toDelete = new ArrayList<>();
		for(DBHandler.GuildUserData user : users) {
			if(event.getGuild().getUserByID(user.getUserId()) == null) {
				toDelete.add(user);
			}
		}
		users.removeAll(toDelete);
		
		
	
		
		Collections.sort(users, (o1, o2) -> (o1.getXp() > o2.getXp()) ? -1 : (   (o1.getXp() == o2.getXp())? 0 : 1));
		
		
		int index = 1;
		for(DBHandler.GuildUserData user : users) {
//			builder.appendField(client.getUserByID(user.getId()).getDisplayName(event.getGuild()) + " has " + user.getXP() + " xp",index  + "  place!",false);
			builder.appendField(client.getUserByID(user.getUserId()).getDisplayName(event.getGuild()) + " is in place " + index +"!",
					"XP:" + user.getXp(),
					false);
			if(index > 9) {
				break;
			}
			index++;
		}
        RequestBuffer.request(()-> {
            event.getChannel().sendMessage(builder.build());
        });
	}

	@Override
	public String getCommandId() {
		return "leaderboard";
	}
}
