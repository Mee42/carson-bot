package com.carson.commands.main;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.carson.classes.Messanger;
import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;


public class CommandRemind extends Command implements ICommand{

	public CommandRemind(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().equals("~remind");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		 final ScheduledExecutorService scheduler =Executors.newScheduledThreadPool(1);
		 
		 
		 final Runnable remind= new Runnable() {
			 
		      @Override
			 public void run() {
				 new Messanger(client).sendMessage(event.getAuthor().getOrCreatePMChannel(), "reminder sent");
				 System.out.println("reminded");
			 }
		       
		       
		 };		     
		     
		     final ScheduledFuture<?> handle = scheduler.scheduleAtFixedRate(remind, 2, 2, TimeUnit.SECONDS);
		     scheduler.schedule(new Runnable() {public void run() { handle.cancel(true);}}, 10, TimeUnit.SECONDS);
	}

	@Override
	public String getName() {
		return "~remind";
	}

	@Override
	public String getDisciption() {
		return "still in developlemt";
	}

}
