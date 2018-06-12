package com.carson.dataObject;

import java.util.ArrayList;
import java.util.List;

public class GuildData {
	private final long id;
    private List<UserData> users;
    
    //any other specific data needed
    public GuildData(long id){
        this.id = id;
        users = new ArrayList<>();
        //inituilize other data to defaults
    }
    
    //getters and setters for data, getter for id

    //for getting UserDataPerGuild objects
    //creating new users should never be needed
    //instead, just attempt to get the user object, and it will return a new one. 


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
