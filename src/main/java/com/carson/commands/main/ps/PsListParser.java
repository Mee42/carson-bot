package com.carson.commands.main.ps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mongodb.DBObject;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.EmbedBuilder;

public class PsListParser {

	private IDiscordClient client;
	
	
	public PsListParser(IDiscordClient client) {
		this.client = client;
	}

	public  String parse(List<DBObject> list) {
		String str = "```\n";
		
		
		
		for(DBObject obj : list) {
			List<Object> objectKeys = Arrays.asList(obj.keySet().toArray());
			
			
			List<String> keys = new ArrayList<>(list.size());
			for (Object objectKey : objectKeys) {
			    keys.add(String.valueOf(objectKey));
			}
			
			for(String key : keys) {
				str+= key + ":"+ obj.get(key).toString() + "\n";
			}
			
			str+="\n";
			
			
		}
		
		
		str += "```";
		return str;
	}
	
	public  EmbedBuilder parseBuilder(List<DBObject> list, String title) {
		EmbedBuilder b = new EmbedBuilder();
		b.withColor((int)Math.random()*255);
		b.withTitle(title);
		
		
		
		for(DBObject obj : list) {
			List<Object> objectKeys = Arrays.asList(obj.keySet().toArray());
			
			
			List<String> keys = new ArrayList<>(list.size());
			for (Object objectKey : objectKeys) {
			    keys.add(String.valueOf(objectKey));
			}
			String str = "";
			
			for(String key : keys) {
				if(key.equals("_id")) {
					
				}else {
					str += key + ":"+ obj.get(key).toString() + "\n";
				}
			}
			b.appendField(client.getUserByID(Long.parseLong(String.valueOf(obj.get("_id")))).getName() + " ( " + String.valueOf(obj.get("_id")) + " )", str, false);
			
			
		}
		
		return b;
		
		
	}
	
}
