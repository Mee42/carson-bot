package com.carson.dataObject;

public class DataGetter {
	private static GuildDataOrginizer INSTANCE = new GuildDataOrginizer();
	
	
	private DataGetter() {
		if(INSTANCE != null) {
			throw new IllegalStateException("Already an instance");
		}
	}
	
	public static GuildDataOrginizer getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new GuildDataOrginizer();
			INSTANCE.importFromJson();
			System.out.println("imported from json");
		}	
		return INSTANCE;
		
	}
	
}
