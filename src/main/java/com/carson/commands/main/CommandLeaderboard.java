package com.carson.commands.main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.carson.commandManagers.*;
import com.carson.dataObject.DataGetter;
import com.carson.dataObject.GuildDataOrginizer;
import com.carson.dataObject.GuildDataOrginizer.UserDataNoGuild;
import com.carson.dataObject.UserData;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

public class CommandLeaderboard extends Command implements ICommand{

	public CommandLeaderboard(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return (event.getMessage().getContent().toLowerCase().equals("~lb") || event.getMessage().getContent().toLowerCase().equals("~leaderboard"));
	}

	@Override
	public void run(MessageReceivedEvent event) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.withAuthorName("Carson-Bot");
		
		Random rand = new Random();
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		Color randomColor = new Color(r, g, b);
		builder.withColor(randomColor);
		builder.withDesc("leaderboard for " + event.getGuild().getName());
		GuildDataOrginizer  guildData = DataGetter.getInstance();
//		List<UserData> users = guildData.getGuild(event.getGuild().getLongID()).getUsers(); //XP PER GUILd
		List<UserDataNoGuild> users = guildData.getUsers();
		System.out.println("users gotten:" + users.size());
		
		List<UserData> toDelete = new ArrayList<>();
		for(UserData user : users) {
			if(event.getGuild().getUserByID(user.getId()) == null) {
				toDelete.add(user);
			}
		}
		users.removeAll(toDelete);
		
		
		for(UserDataNoGuild user : users) {
			user.setName(client.getUserByID(user.getId()).getName());
		}
		
		Collections.sort(users, new Comparator<UserData>(){

		  public int compare(UserData o1, UserData o2)
		  {
		     return (o1.getXP() > o2.getXP()) ? -1 : (   (o1.getXP() == o2.getXP())? 0 : 1);
		  }
		});
		
		
		int index = 1;
		for(UserData user : users) {
			builder.appendField(user.getName() + " has " + user.getXP() + " xp",index  + "  place!",false);//TODO test inline var
			if(index > 15) {
				break;
			}
			index++;
		} 
		
		event.getChannel().sendMessage(builder.build());
	}

	@Override
	public String getName() {
		return "~leaderboard, or ~lb";
	}

	@Override
	public String getDisciption() {
		return "gets the leaderboard for everyone on the server. Only the top 15 people get on in";
	}

}
