package com.carson.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.carson.CarsonBot;
import com.carson.classes.FileIO;
import com.carson.classes.MultiOutputStream;

import sx.blah.discord.api.IDiscordClient;

public class Main {

	    public static void main(String[] args){
	    	
	    	
	    	File f = new File("/home/carson/discord/jars/console.txt");
	    	FileIO.use(f).write("");
	    	
	        try //this is for
	        {
	        	FileOutputStream fout= new FileOutputStream(f);
	        	FileOutputStream ferr= new FileOutputStream(f);
	        	
	        	MultiOutputStream multiOut= new MultiOutputStream(System.out, fout);
	        	MultiOutputStream multiErr= new MultiOutputStream(System.err, ferr);
	        	
	        	PrintStream stdout= new PrintStream(multiOut);
	        	PrintStream stderr= new PrintStream(multiErr);
	        	
	        	System.setOut(stdout);
	        	System.setErr(stderr);
	        }
	        catch (FileNotFoundException ex)
	        {
	        	//Could not create/open the file
	        }
	        String token = "";
	    	
	    	try {
	    		token = FileIO.use(new File("/home/carson/discord/jars/key")).readList().get(0);
	    	}catch(Exception e) {
	    		System.out.println("threw a " + e.getClass().getName() + " when trying to read from key");
	    		CleanThread.systemExit(2);
	    	}
	    	
	        IDiscordClient client = BotUtils.getBuiltDiscordClient(token);


	        // Register a listener via the EventSubscriber annotation which allows for organisation and delegation of events
	        CarsonBot cb = new CarsonBot();
	        client.getDispatcher().registerListener(cb);
	        CleanThread hook = new CleanThread(client, cb);
	        Runtime.getRuntime().addShutdownHook(hook);
	        // Only login after all events are registered otherwise some may be missed.
	        
	        try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	        
	        client.login();
	        
	        cb.startup(client, false);
	        
	        
	        
	        
	    }

	
}

