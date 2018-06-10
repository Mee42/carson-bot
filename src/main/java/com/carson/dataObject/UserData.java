package com.carson.dataObject;

public class UserData { //PER GUILD
    protected final long id;
    
    
    //other data
    public UserData (long id){
        this.id = id;
        //inituilize other data to defaults
    }
	public long getId() {
		return id;
	}
	
   
	@Override
	public String toString() {
		return "ID:" + id ;
	}
	
    //getters and setters for data
}