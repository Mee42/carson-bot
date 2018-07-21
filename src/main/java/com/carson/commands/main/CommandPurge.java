package com.carson.commands.main;

import java.util.List;

import com.carson.commandManagers.Command;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.RequestBuffer;

public class CommandPurge extends Command  {

	public CommandPurge(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event,String content, String[] args) {
		return content.startsWith("~purge");
	}

	@Override
	public void run(MessageReceivedEvent event,String content, String[] args) {
		String integer;
		try {
			integer = event.getMessage().getContent().split(" ")[1];
		}catch (ArrayIndexOutOfBoundsException e) {
			sendMessage(event, "sorry, there was an error");
			return;
		}
		if(isInteger(integer)) {
			int in = Integer.valueOf(integer);
			if(in>100) {
				sendMessage(event.getChannel(), "sorry, Discord rate limits bulk deletes to 100 messages at a time. I'l delete the last 100 for you anyway");
			}
			
				RequestBuffer.request(() -> {
					try {
						List<IMessage> list = event.getChannel().getMessageHistory(in);
						event.getChannel().bulkDelete(list);
						
					}catch (sx.blah.discord.util.MissingPermissionsException e) {
						sendMessage(event, "I need manager message perms. contact the moderator of this server, <@" + event.getGuild().getOwnerLongID() + "> ");
					} 
				});
			
			sendMessage(event.getChannel(), "done!");
		}else {
			
			sendMessage(event.getChannel(), " not a valid command. ");
			System.out.println("ERROR:" + event.getMessage().getContent() + " threw an error when trying to purge");
		}
				
	}

	@Override
	public String getName() {
		return "~purge *int_message_count*";
	}

	@Override
	public String getDisciption() {
		return "deletes the last *int* messages";
	}

	
	private static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}

	@Override
	public PermissionLevel getWantedPermissionLevel() {
		return PermissionLevel.MOD;
	}
}
