package com.carson.dataObject;

import java.util.ArrayList;
import java.util.List;

public class GuildData {
	private final long id;
    private List<UserData> users;
    
    private String deathMessage;
    private long deathChannel;
    
    //any other specific data needed
    public GuildData(long id){
        this.id = id;
        users = new ArrayList<>();
        deathMessage  = "[name] has left the server";
        deathChannel = -1L;
    }
    

//gets the UserDataPerGuild instance for that id
    public UserData getUserData(long id){
	    for(UserData testingUser : users){
	            if(testingUser.getId() == id){
	                  return testingUser;
	            }
	     }
         UserData newUser = new UserData (id); 
         users.add(newUser);
         return newUser;
    }
    
    
    
    
    
    public String getDeathMessage() {
		return deathMessage;
	}

	public void setDeathMessage(String deathMessage) {
		this.deathMessage = new String(deathMessage);
	}
	
	public void setDeathChannel(long channel) {
		this.deathChannel = channel;
	}
	public long getDeathChannel() {
		return deathChannel;
	}

	@Override
    public String toString() {
    	StringBuilder builder = new StringBuilder();
    	builder.append("\tguild id:" + id);
    	builder.append("\n\tusers:\n");
    	for(UserData user : users) {
    		builder.append("\t\t" + user.toString() + "\n");
    	}
    	return builder.toString();
    }
    
    //deletes and re-adds the user instance, in effect resetting it to default
	public UserData resetUser(long id) {
		 for(UserData testingUser : users){
			if(testingUser.getId() == id){
					users.remove(testingUser);
					UserData newUser = new UserData(id);
					users.add(newUser);
					return newUser;
			}
		}
		 UserData newUser = new UserData(id);
		users.add(newUser);
		return newUser;
	}

	public long getId() {
		return id;
	}

	public List<UserData> getUsers() {
		return users;
	}

		

//no methods will edit any data or do any processing to keep in compliance with the POJO  requirements, and to make it safer to convert to JSON

}
