package com.carson.commands.main;

import java.io.IOException;
import java.util.List;

import com.carson.classes.PhotoStream;
import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandReddit extends Command implements ICommand{

	public CommandReddit(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContent().split(" ");
		if(args.length == 0) 
			return false;
		if(!args[0].equals("~reddit")) {
			return false;
		}
		if(args.length == 1) {
			sendMessage(event, " you need a subreddit. ex: ~reddit *subreddit* ");
			return false;
		}
		return true;
		
		
	}

	@Override
	public void run(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContent().split(" ");
		
		List<String> pics;
		try {
			pics = PhotoStream.getUrl("https://www.reddit.com/r/" + args[1]);
			
	        String redditTemp = "";
			for(String pic : pics) {
					
					redditTemp = redditTemp + " " + pic + "\n\n\n";
					if(redditTemp.length()>1000) {
						sendMessage(event.getChannel(),redditTemp);
						redditTemp = "";
					}
			}
			
			sendMessage(event.getChannel(),redditTemp);
				
		} catch (IOException e1) {
			System.out.println("ERROR: running ~reddit threw a IOExecption");
			e1.printStackTrace();
		}
		
		return ;
	}

	@Override
	public String getName() {
		return "~reddit *subreddit_name*";
	}

	@Override
	public String getDisciption() {
		return "gets the latest reddit memes. browes your favoret subriddit, or just use \"memes\" for some  memes :ok_hand:";
	}

}
