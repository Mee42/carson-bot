package com.carson.commands.main;

import java.awt.Color;
import java.util.*;

import com.carson.commandManagers.*;

import com.carson.dataObject.DBHandler;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

public class CommandLeaderboard extends Command{

	public CommandLeaderboard(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event,String content, String[] args) {
		return (event.getMessage().getContent().toLowerCase().equals("~lb") || event.getMessage().getContent().toLowerCase().equals("~leaderboard"));
	}

	@Override
	public void run(MessageReceivedEvent event,String content, String[] args) {
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
	public String getName() {
		return "~leaderboard, or ~lb";
	}

	@Override
	public String getDisciption() {
		return "gets the leaderboard for everyone on the server";
	}

}
