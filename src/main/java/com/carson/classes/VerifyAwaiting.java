package com.carson.classes;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

public class VerifyAwaiting {
	private MessageReceivedEvent event;
	private boolean active = false;
	
	public VerifyAwaiting(MessageReceivedEvent event) {
		this.event = event;
		active = true;
	}
	
	
	//initil initulize command
	public VerifyAwaiting() {
		active = false;
	}
	
	public boolean set(MessageReceivedEvent newEvent) {
		if(!active) {
			this.event = newEvent;
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
		
		
		
		return true;
	}
	
	public boolean verifyFlop(MessageReceivedEvent newEvent, IUser user){
		if(!active)
			return false;
		
		if(newEvent.getAuthor().equals(event.getAuthor()))
			return false;
		
		if(!newEvent.getChannel().equals(event.getChannel()))
			return false;
		
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
		
		return true;
	}
	
	
	public MessageReceivedEvent getInitalEvent() {
		return event;
	}
	
	public boolean getActive() {
		return active;
	}
	
	public void setActive(boolean set) {
		active = set;
	}
	
	
	
	
	
	
}









