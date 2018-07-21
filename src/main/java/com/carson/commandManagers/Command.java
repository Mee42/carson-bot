package com.carson.commandManagers;

import com.carson.classes.Messenger;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

public abstract class Command{
	
	 protected IDiscordClient client;

	
	public Command(IDiscordClient c) {
		client = c;
	}
	
	
	protected void sendMessage(IChannel c, String t) {
		 new Messenger().sendMessage(c, t);
	}
	
	
	protected void sendMessage(MessageReceivedEvent event, String text) {
		 sendMessage(event.getChannel(), text);
	}

	public abstract boolean test(MessageReceivedEvent event,String content, String[] args); //returns true if the command should be called
	public abstract void run(MessageReceivedEvent event,String content, String[] args); //runs the command
	public abstract String getName(); //gets the name of the command
	public abstract String getDisciption(); //gets the description of the command
    public PermissionLevel getWantedPermissionLevel(){
     return PermissionLevel.USER;
    }

    public enum PermissionLevel{
        USER,//no permission
        MOD,//moderator for server
        BOT_ADMIN//carsonbot admin
    }

    public boolean test(MessageReceivedEvent event){
        if(!hasPermission(getWantedPermissionLevel(),Register.getPermissionLevel(event)))return false;
        return test(event,event.getMessage().getContent(),event.getMessage().getContent().split(" "));
    }
    public void run(MessageReceivedEvent event){
        run(event,event.getMessage().getContent(),event.getMessage().getContent().split(" "));
    }

    public boolean hasPermission(PermissionLevel wanted, PermissionLevel given){
        if(wanted == given)return true;
        if(given == PermissionLevel.BOT_ADMIN)return true;
        if(wanted == PermissionLevel.USER)return true;
        //user      user    true
        //user      mod     true
        //user      cb      true
        //mod       user    false
        //mod       mod     true
        //mod       cb      true
        //cb        user    false
        //cb        mod     false
        //cb        cb      true
        return false;
    }
}
