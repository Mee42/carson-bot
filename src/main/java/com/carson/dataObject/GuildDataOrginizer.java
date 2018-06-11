package com.carson.dataObject;

import java.util.ArrayList;
import java.util.List;

import com.carson.classes.FileIO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GuildDataOrginizer {
	   /*there should only every be one instance of this class
	  	*the purpose is to keep track of all GuildData objects, and make sure they are backed up properly in the event of an unorthodox shutdown
	    *constructor should be empty, as no arguments are needed. 
	    *updating data from json is not done in constructor to allow for better testing
	    *this could potentially be used to store command prefixes, however that would have to be incorporated with another big change to make rewriting commands worth it
	    */
	    private List<GuildData> guilds = new ArrayList<>();
	    private List<UserDataNoGuild> users = new ArrayList<>();
	    private String jsonFile = "/home/carson/java/files/jsonGuildDataDump.json";

	    public GuildDataOrginizer importFromJson(){
	    		Gson gson = new GsonBuilder().create();
	    		String json = FileIO.use(jsonFile).readString();
	    		GuildDataOrginizer newGuildOrginizerData = gson.fromJson(json,GuildDataOrginizer.class);
	        	this.guilds = newGuildOrginizerData.guilds;
	        	this.users = newGuildOrginizerData.users;
	        	
	        	//import other data from the newGuildOrginizerData
	        	return this;
	    	}

	    public String sterilize(){
	    	Gson gson = new GsonBuilder().create();
	        String json =gson.toJson(this);
	        FileIO.use(jsonFile).write(json);
	        return json;
	    }
	    
	    
	//see GuildData's (almost) identical method for elaborating on why this is written
	    public GuildData getGuild(long id){
	        for(GuildData guildData : guilds){
	            if(guildData.getId() == id){return guildData;}
	        }
	        GuildData justMadeInstance = new GuildData(id);
	        guilds.add(justMadeInstance);
	        return justMadeInstance;
        }
	    
	    @Override
	    public String toString() {
	    	StringBuilder builder = new StringBuilder();
	    	builder.append("guilds:\n");
	    	for(GuildData guild : guilds) {
	    		builder.append(guild.toString() + "\n");
	    	}
	    	builder.append("users:\n");
	    	for(UserDataNoGuild user : users) {
	    		builder.append(user.toString() + "\n");
	    	}
	    	return builder.toString();
	    }
	    public String print() {
	    	String data = toString();
	    	System.out.println(data);
	    	return data;
	    }
	    
	    public UserDataNoGuild getUserData(long id){
		    for(UserDataNoGuild testingUser : users){
		            if(testingUser.getId() == id){
		                  return testingUser;
		            }
		     }
		    UserDataNoGuild newUser = new UserDataNoGuild(id); 
	         users.add(newUser);
	         return newUser;
	    }
	    
	    public long getXPForUser(long id) {
	    	return getUserData(id).getXP();
	    }
	    public long increaseXPForUser(long id, int amount) {
	    	return getUserData(id).incrimentXP(amount);
	    }
	    
	    public List<UserDataNoGuild> getUsers() {
	    	return users;
	    }
	    
	public class UserDataNoGuild extends UserData{
		public UserDataNoGuild(long id) {
			super(id);
		}

		private long XP;

		public long getXP() {
			return XP;
		}
		
		@Override
		public String toString() {
			return "ID:" + id + " XP:" + XP;
		}
		
		public long incrimentXP(int amount) {
			XP+=amount;
			return XP;
		}
		
		
	}
}