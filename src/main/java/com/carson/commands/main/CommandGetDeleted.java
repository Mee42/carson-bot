package com.carson.commands.main;

import java.util.List;

import com.carson.CarsonBot;
import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class CommandGetDeleted extends Command implements ICommand{

	public CommandGetDeleted(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		/*return ((event.getGuild().getLongID() ==400786190619639839L || event.getGuild().getLongID() == 293853365891235841L) && 
					event.getAuthor().getLongID() ==  318783502768144384L && 
					event.getMessage().getContent().toLowerCase().startsWith("~getdeleted"));
		*/
		
		return (( event.getAuthor().getLongID() ==  318783502768144384L|| event.getAuthor().getLongID()  == 293853365891235841L) && 
				event.getGuild().getLongID() ==400786190619639839L && 
				event.getMessage().getContent().toLowerCase().startsWith("~getdeleted"));
		
	}

	@Override
	public void run(MessageReceivedEvent event) {
		String[] messageContentArray = event.getMessage().getContent().split(" ");
		if(messageContentArray.length < 2) {
			sendMessage(event, "incorrect args");
			return;
		}
		String toGetCountString = messageContentArray[1];
		if(!isInteger(toGetCountString)) {
			sendMessage(event, "incorrect args");
			return;
		}
		if(CarsonBot.deletedMessages.size() == 0) {
			sendMessage(event, "no deleted messages");
		}
		boolean filterByUsers;
		List<IUser> mentions = event.getMessage().getMentions();
		if(mentions.isEmpty()) {
			filterByUsers = false;
		}else {
			filterByUsers = true;
		}
		
		int toGet = Integer.parseInt(toGetCountString);
		int currentWorkingMessage = 0;
		for(int i = 0;i<toGet;i++) {
			if(CarsonBot.deletedMessages.size() == currentWorkingMessage) {
				sendMessage(event, "no more messages");
				return;
			}
			IMessage currentMessage = CarsonBot.deletedMessages.get(currentWorkingMessage);
			if(currentMessage.getChannel().getLongID() == event.getChannel().getLongID()) {
//				if(filterByUsers) {
//					boolean foundFilterUser = false;
//					for(IUser shouldFilterUser : mentions) {
//						if(currentMessage.getAuthor().getLongID() == shouldFilterUser.getLongID()) {
//							foundFilterUser = true;
//							break;
//						}
//					}
//					if(!foundFilterUser) {//TOOD UNTESTED
//						currentWorkingMessage++;
//						i--;
//					}
//				} //BROKEN
				sendMessage(event,"sent by: " + currentMessage.getAuthor() + "\ncontent:```" + currentMessage.getContent() + " ``` sent at " + 
						currentMessage.getTimestamp().toString()
						);
				CarsonBot.deletedMessages.remove(currentMessage);
			}else {
				currentWorkingMessage++;
				i--;
			}
		}
		
	
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
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
}
