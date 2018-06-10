package com.carson.dataObject;

public class DataGetter {
	private static GuildDataOrginizer INSTANCE = new GuildDataOrginizer();
	
	
	private DataGetter() {
		if(INSTANCE != null) {
			throw new IllegalStateException("Already an instance");
		}
	}
	
	public static GuildDataOrginizer getInstance() {
		return INSTANCE;
	}
	
}
