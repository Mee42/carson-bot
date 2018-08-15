

package com.carson;

import com.carson.classes.FileIO;
import com.carson.classes.TimeManager;
import com.carson.main.BotUtils;
import sx.blah.discord.api.IDiscordClient;

import java.io.File;

public class Main {

	    public static void main(String[] args){
	    	


	        String token = "";
	    	try {
	    		token = FileIO.use(new File("/home/carson/java/jars/key")).readList().get(0);
	    	}catch(Exception e) {
	    		System.out.println("threw a " + e.getClass().getName() + " when trying to read from key");
	    		e.printStackTrace();
	    		return;
	    	}
	
	        IDiscordClient client = BotUtils.getBuiltDiscordClient(token);


	        // Register a listener via the EventSubscriber annotation which allows for organisation and delegation of events
	        CarsonBot cb = new CarsonBot();
	        client.getDispatcher().registerListener(cb);

	        
	        //all this does in import the client
	        cb.importClient(client);


	        
	        // Only login after all events are registered otherwise some may be missed.
	        client.login();


			Runtime.getRuntime().addShutdownHook(new TimeManager());//starts the logger

	        
	    }
	
}

