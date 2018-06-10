package com.carson.dataObject;

import com.google.gson.annotations.SerializedName;

public class UserData { //PER GUILD
    protected final long id;
    
    protected String name;
    
    @SerializedName("XP_FOR_USER_DATA")
    protected long XP;
    
    //other data
    public UserData (long id){
        this.id = id;
        name = "no name set yet";
        XP = 0;
    }
    
	public long getId() {
		return id;
	}
	
   
	@Override
	public String toString() {
		return "ID:" + id ;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getXP() {
		return XP;
	}
	public void setXP(long xP) {
		XP = xP;
	}
	
	
	
}