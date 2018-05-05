package com.carson.classes;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Messanger {
	IDiscordClient client;
	
	
	
	public Messanger(IDiscordClient c) {
		this.client = c;
	}
	
	//sends a message
	public void sendMessage(IChannel channel, String message) {
		

		Logger.logBot(channel, message);
        // this is so it can repeat sending messages when rate limited
		
        RequestBuffer.request(() -> {
            try{
                channel.sendMessage(message);
            } catch (DiscordException e){
                System.err.println("Message could not be sent with error: ");
                e.printStackTrace();
            }
        });

		
		
		
	}
	
	
	//send message without console print (used for tic tac toe board, mainly)
	public void sendMessageClean(IChannel channel, String message) {
		Logger.logBotClean(channel, message);
        // this is so it can repeat sending messages when rate limited
		
        RequestBuffer.request(() -> {
            try{
                channel.sendMessage(message);
            } catch (DiscordException e){
                System.err.println("Message could not be sent with error: ");
                e.printStackTrace();
            }
        });
		
		
	}
	
}
