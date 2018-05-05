package com.carson.classes;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

public class VerifyAwaiting {
	private MessageReceivedEvent event;
	private long timestamp;	
	private boolean active = false;
	
	public VerifyAwaiting(MessageReceivedEvent event) {
		this.event = event;
		timestamp = System.currentTimeMillis() / 1000;
		active = true;
	}
	
	
	//initil initulize command
	public VerifyAwaiting() {
		active = false;
	}
	
	public boolean set(MessageReceivedEvent newEvent) {
		if(!active) {
			this.event = newEvent;
			timestamp = System.currentTimeMillis() / 1000;
			active = true;
			return true;
		}else {
			return false;			
		}
		
		
	}
	
	
	public boolean verify(MessageReceivedEvent newEvent) {
		if(!active)
			return false;
		
		if(!newEvent.getAuthor().equals(event.getAuthor()))
			return false;
		
		if(!newEvent.getChannel().equals(event.getChannel()))
			return false;
		
		if(System.currentTimeMillis()/1000   >  timestamp + 100) {
			active = false;
			System.out.println("EVENT: timed out");
			return false;

		}
		
		timestamp = System.currentTimeMillis() / 1000;
		return true;
	}
	
	public boolean verifyFlop(MessageReceivedEvent newEvent, IUser user){
		if(!active)
			return false;
		
		if(newEvent.getAuthor().equals(event.getAuthor()))
			return false;
		
		if(!newEvent.getChannel().equals(event.getChannel()))
			return false;
		
		if(System.currentTimeMillis()/1000   >  timestamp + 100) {
			System.out.println("EVENT: timed out");
			active = false;
			System.out.println("EVENT: timed out");

			return false;
		}
		
		timestamp = System.currentTimeMillis() / 1000;
		return true;
		
		
		
		
		
	}
	public boolean verifyFromPM(MessageReceivedEvent newEvent) {
		if(!active) { 
			return false;
		}
		
		if(!newEvent.getAuthor().equals(event.getAuthor())) {
			return false;
		}
		
		if(!newEvent.getChannel().isPrivate()) {
			return false;
		}
		
		if(System.currentTimeMillis()/1000   >  timestamp + 100) {
			active = false;
			timestamp = System.currentTimeMillis() /1000;
			System.out.println("EVENT: timed out");

			return false;
		}
		
		timestamp = System.currentTimeMillis() / 1000;
		return true;
	}
	
	
	public MessageReceivedEvent getInitalEvent() {
		return event;
	}
	
	public boolean getActive() {
		
		if(System.currentTimeMillis()/1000   >  timestamp + 100) {
			active = false;
			timestamp = System.currentTimeMillis() /1000;
			System.out.println("EVENT: timed out");
		}
		timestamp = System.currentTimeMillis() / 1000;
		return active;
	}
	
	public void setActive(boolean set) {
		active = set;
	}
	
	
	
	
	
	
}









