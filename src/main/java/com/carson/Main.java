package com.carson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.carson.classes.*;
import com.carson.main.*;

import sx.blah.discord.api.IDiscordClient;

public class Main {

	    public static void main(String[] args){
	    	
	    	
	    	File consoleFile = new File("console.txt");
	    	FileIO.use(consoleFile).write(""); //clears the console
	    	

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
	        
	        CleanThread hook = new CleanThread(client, cb);
	        Runtime.getRuntime().addShutdownHook(hook);
	        
	        
	        //all this does in inport the client
	        cb.importClient(client);
	        
	        
	        // Only login after all events are registered otherwise some may be missed.
	        client.login();
	        
	        
	        
	        
	        
	        
	        
	    }

	
}

