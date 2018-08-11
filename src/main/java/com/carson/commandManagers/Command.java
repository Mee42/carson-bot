package com.carson.commandManagers;

import com.carson.classes.Messenger;
import com.carson.dataObject.DBHandler;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

import java.util.regex.Pattern;

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

	public abstract boolean test(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args); //returns true if the command should be called
	public abstract void run(String prefix, String content,MessageReceivedEvent event,String rawContent, String[] args); //runs the command

    /**
     *
     * @return a unique identifyer for the command
     */
    public abstract String getCommandId();


    public PermissionLevel getWantedPermissionLevel(){
     return PermissionLevel.USER;
    }

    public enum PermissionLevel{
        USER,//no permission
        MOD,//moderator for server
        BOT_ADMIN//carsonbot admin
    }

    /**
     * this method generates several variables and passes them onto the subclass
     * it also makes sure that the message starts with the prefix, and that the user has the right permission level
     *
     * @param event the message event recived
     * @return true if the command should be runX, false if not
     *
     */
    public boolean test(MessageReceivedEvent event){
        if(!event.getMessage().getContent().startsWith(getPrefix(event)))return false;
        if(!hasPermission(getWantedPermissionLevel(),Register.getPermissionLevel(event)))return false;
        return test(getPrefix(event), event.getMessage().getContent().replaceFirst(Pattern.quote(getPrefix(event)),""), event,event.getMessage().getContent(),event.getMessage().getContent().split(" "));
    }
    public void run(MessageReceivedEvent event){
        run(getPrefix(event),event.getMessage().getContent().replaceFirst(Pattern.quote(getPrefix(event)),""),event,event.getMessage().getContent(),event.getMessage().getContent().split(" "));
    }

    public static String getPrefix(MessageReceivedEvent event){
        if(event.getGuild() == null)return "~";
        return DBHandler.get().getPrefix(event.getGuild().getLongID());
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
