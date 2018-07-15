package com.carson.main;

import com.carson.CarsonBot;
import com.carson.dataObject.DataGetter;
import sx.blah.discord.api.IDiscordClient;

import javax.xml.crypto.Data;

public class CleanThread extends Thread{
	
	IDiscordClient client;
//	CarsonBot cb;

	public CleanThread(IDiscordClient client) {
		this.client = client;
//		this.cb = carsonBot;
	}
	public CleanThread(IDiscordClient client, CarsonBot carsonBot) {
		this.client = client;
//		this.cb = carsonBot;
	}

	@Override
	public void run() {
		DataGetter.getInstance().privateSterilize();
		if(client.isReady()) {
			for(long l : DataGetter.getInstance().getEaster()){
			    client.getMessageByID(l).delete();
            }
			client.getChannelByID(439890985653370896L).sendMessage("Carson Bot is shutting down");
		    client.logout();
		}
		System.out.println("bot shutdown");
	}

	
	
}


