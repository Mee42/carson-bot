package com.carson.main;

import com.carson.CarsonBot;

import sx.blah.discord.api.IDiscordClient;

public class CleanThread extends Thread{
	
	IDiscordClient client;
	CarsonBot cb;
	static int exitCode = 999;
	
	public CleanThread(IDiscordClient client, CarsonBot carsonBot) {
		this.client = client;
		this.cb = carsonBot;
	}
	
	@Override
	public void run() {
		client.getChannelByID(439890985653370896L).sendMessage("Carson Bot is shutting down");
		
	}
	
	public static void systemExit(int code) {
		
		
		
		
	}
	
	
	
	
}


