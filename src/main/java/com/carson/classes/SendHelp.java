package com.carson.classes;

import com.carson.commandManagers.ICommand;
import com.carson.commandManagers.Register;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;

public class SendHelp {

	public static void sendHelp(MessageReceivedEvent event, Register reg) {
		System.out.println("EVENT:sending help message to " + event.getAuthor().getName());
		event.getChannel().sendMessage("```I'm Carson-Bot, and i'm Sliding into your DM's using mag-lev technology```");
		IChannel dm = event.getClient().getOrCreatePMChannel(event.getAuthor());
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.withColor((int)(Math.random()*245+1), (int)(Math.random()*245+1),(int)(Math.random()*245+1));
		builder.withAuthorName("Carson-Bot");
		builder.withTitle("all of these amazing commands");
		
		builder.appendDescription("***use ~help*** *command_name* ***to get more detailed information***");
		

		for(ICommand c : reg.getCommands()){
			try{
				if(c.getName() == null || c.getDisciption() == null) {
					
				}else {
					builder.appendField(c.getName(), c.getDisciption(), false);
				}
			}catch(IllegalArgumentException e) {
				e.printStackTrace();
				dm.sendMessage(builder.build());
				builder.clearFields();
			}
		}
		dm.sendMessage(builder.build());
	
		
		
	}
	
	public enum HelpTypes {ADMIN, PLAIN}
	
}
