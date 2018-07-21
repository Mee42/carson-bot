package com.carson.classes;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Messenger {
//	IDiscordClient client;
	
	
	
	public Messenger(IDiscordClient c) {
//		this.client = c;
	}
//	public Messenger() {
//		
//	}
	
	public Messenger() {
		
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
			return ;
        });
		
		
		
	}
	
	public IMessage sendMessageAndGet(IChannel c, String str) {
		Logger.logBot(c, str);
        // this is so it can repeat sending messages when rate limited
        return RequestBuffer.request(() -> {
            try{
                return c.sendMessage(str);
            } catch (DiscordException e){
                System.err.println("Message could not be sent with error: ");
                e.printStackTrace();
                return null;
            }
        }).get();
	}
	
	
	
	
}
