package com.carson.classes;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.Register;
import com.carson.dataObject.DBHandler;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.lang.Math;

public class SendHelp {

	public static void sendHelpOld(MessageReceivedEvent event, Register reg) {
		System.out.println("EVENT:sending help message to " + event.getAuthor().getName());
//		String message = "I'm Carson-Bot, and i'm Sliding into your DM's using mag-lev technology";
		String message = "I'm Carson-Bot, and I'm sliding into your DM's while I wait for the technological singularity.";
		event.getChannel().sendMessage("```" + message + "```");
		IChannel dm = event.getClient().getOrCreatePMChannel(event.getAuthor());

		EmbedBuilder builder = new EmbedBuilder();
		builder.withColor((int)(Math.random()*245+1), (int)(Math.random()*245+1),(int)(Math.random()*245+1));
		builder.withAuthorName("Carson-Bot");
		builder.withTitle("all of these amazing commands");

//		builder.appendDescription("***use ~help*** *command_name* ***to get more detailed information***");//not done TODO
        int count = 0;
        DBHandler d = DBHandler.get();
		for(Command c : reg.getCommands()){
			try{
			    if(count > 20){
                    dm.sendMessage(builder.build());
                    builder.clearFields();
                    count = 0;
                }
                count++;
                Document doc = d.getDB("commands").find(Filters.eq("_id",c.getCommandId())).first();
                if(doc == null){
                    continue;
                }
				if(doc.containsKey("nohelp") && doc.get("nohelp").equals("true")){
					continue;
				}
				if(doc.containsKey("broken") && doc.get("broken").equals("true")){
					continue;
				}

				String name = (String)doc.get("name");
                String desc = (String)doc.get("desc");
                if(name==null){name ="null:" + (doc.get("_id"));}
                if(desc==null){desc="null";}
                builder.appendField(name, desc, false);//TODO TEST

			}catch(IllegalArgumentException e) {
				e.printStackTrace();
				dm.sendMessage(builder.build());
				builder.clearFields();
			}
		}
		dm.sendMessage(builder.build());



	}


	public static void sendHelp(MessageReceivedEvent event, Register reg, String prefix){
//		String message = "I'm Carson-Bot, and i'm Sliding into your DM's using mag-lev technology";
//		String message = "I'm Carson-Bot, and I'm sliding into your DM's while I wait for the technological singularity.";
//		event.getChannel().sendMessage("```" + message + "```");
//		IChannel dm = event.getClient().getOrCreatePMChannel(event.getAuthor());
		DBHandler d = DBHandler.get();

		if(event.getMessage().getContent().equals(prefix + "help")) {
			String str = "Commands. use ~help to get more info\n`";
			for (Command c : reg.getCommands()) {
				Document doc = d.getDB("commands").find(Filters.eq("_id", c.getCommandId())).first();
				if (doc == null) {
					continue;
				}
				if (doc.containsKey("nohelp") && doc.get("nohelp").equals("true")) {
					continue;
				}
				if (doc.containsKey("broken") && doc.get("broken").equals("true")) {
					continue;
				}
				String name = (String) doc.get("name");
				if (name == null) {
					System.out.println(c.getCommandId() + " has no name :eyes:");
					continue;
				}
				str += name + "    ";
				if (str.split("\n")[str.split("\n").length - 1].length() > 50) {
					str += "\n";
				}
			}
			event.getChannel().sendMessage(str + "`");
		}else{

			String command = event.getMessage().getContent().split(" ",2)[1];
			Document doc = d.getDB("commands").find(Filters.eq("_id",command)).first();
			if(doc == null){
				RequestBuffer.request(()->event.getChannel().sendMessage("couldn't find that command"));
				return;
			}
			if(!doc.containsKey("trigger") || !doc.containsKey("desc")){
				RequestBuffer.request(()->event.getChannel().sendMessage("could not find all information"));
			}
			RequestBuffer.request(()->event.getChannel().sendMessage("**" + command + "**\n`" + String.valueOf(doc.get("trigger")) + "`\n--\n`" + String.valueOf(doc.get("desc")) + "`"));

		}
	}
}
