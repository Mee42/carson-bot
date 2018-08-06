package com.carson.classes;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;

import java.io.File;
import java.util.List;

public class ProfanityChecker {//TODO fix up or delete

	
	public static boolean check(MessageReceivedEvent event ) {
		if(event.getChannel().isPrivate()) {
			return false;
		}
		if(event.getChannel().isNSFW()){
			return false;
		}
		
		if(event.getGuild().getLongID() != 422570115217883136L) {
			return false;
		}
		
		
		List<IRole> roles = event.getAuthor().getRolesForGuild(event.getGuild());
		String text = event.getMessage().getContent();
		text = text.toLowerCase();
		
		for(IRole role : roles) {
			if(role.getName().equals("Sir Gumbo") || role.getName().equals("Senior Gumbo")) { 
				return false;
			}
		}
		
		
		List<String> words = FileIO.use(new File("blocked-words.txt")).readList();
		for(String word : words) {
			if(text.contains(word)) {
				
				event.getChannel().sendMessage( "WOAH THERE - are you sure you want say stuff like that?"
						+ "\nSome language is not approved in this server. especaly for low-ranked individuals who have not gained the admin's trust"
						+ "\nIf you don't belive you said anything wrong, contact the dev for support. "
						+ "Your message has been reported");
				
				event.getClient().getOrCreatePMChannel(
						event.getClient().getUserByID(279412525051674624L)
					).sendMessage(
							event.getAuthor().getName()+ "/"+
							event.getAuthor().getDisplayName(event.getGuild())+
							" said:```" + text+"```");
				
				try {
					event.getMessage().delete();
				}catch (sx.blah.discord.util.MissingPermissionsException e) {
					event.getChannel().sendMessage( "I need manager message perms. contact the moderator of this server, <@" + event.getGuild().getOwnerLongID() + "> ");
				} 
				
				return true;
			}
		}
		
		
		return false;
		
		
	}
}
