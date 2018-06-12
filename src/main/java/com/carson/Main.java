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
	    	
	        try //this is for streaming the console to a file, because....? idk
	        {
	        	FileOutputStream fout= new FileOutputStream(consoleFile);
	        	FileOutputStream ferr= new FileOutputStream(consoleFile);
	        	
	        	MultiOutputStream multiOut= new MultiOutputStream(System.out, fout);
	        	MultiOutputStream multiErr= new MultiOutputStream(System.err, ferr);
	        	
	        	PrintStream stdout= new PrintStream(multiOut);
	        	PrintStream stderr= new PrintStream(multiErr);
	        	
	        	System.setOut(stdout);
	        	System.setErr(stderr);
	        }
	        catch (FileNotFoundException ex)
	        {
	        	System.err.println("COULD NOT PRINT CONSOLE FILE");
	        	return;
	        }
	        String token = "";
	    	
	    	try {
	    		token = FileIO.use(new File("/home/carson/java/jars/key")).readList().get(0);
	    	}catch(Exception e) {
	    		System.out.println("threw a " + e.getClass().getName() + " when trying to read from key");
	    		return;
	    	}
	    	
	        IDiscordClient client = BotUtils.getBuiltDiscordClient(token);


	        // Register a listener via the EventSubscriber annotation which allows for organisation and delegation of events
	        CarsonBot cb = new CarsonBot();
	        client.getDispatcher().registerListener(cb);
	        CleanThread hook = new CleanThread(client, cb);
	        Runtime.getRuntime().addShutdownHook(hook);
	        
	        
	        //all this does in inport the client and locked status
	        cb.startup(client);
	        
	        
	        // Only login after all events are registered otherwise some may be missed.
	        client.login();
	        
	        
	        
	        
	        
	        
	        
	    }

	
}

