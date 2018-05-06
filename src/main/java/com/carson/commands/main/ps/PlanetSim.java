package com.carson.commands.main.ps;

import com.carson.classes.Messanger;
import com.mongodb.MongoClient;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

public class PlanetSim {

	IDiscordClient client;
	MongoClient mClient = MinerManager.getClient();
    public MinerManager manager;
    Messanger m;
    
	public PlanetSim(IDiscordClient c,Messanger m) {
		client = c;
		this.m = m;
		manager =  new MinerManager(c, mClient);
	}
	
	public static PlanetSim make(IDiscordClient c, Messanger m) {
		return new PlanetSim(c,m);
	}
	
	public boolean ps(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContent().split(" ");
		if(args.length == 0) {return false;} //if the message doesn't have content
		
		if(!args[0].equals("ps")) {return false;}//if the message doesn't start with ps
		
		if(args.length == 1) {return printHelp(event);} //prints the help message if there is no other arguments
		
		IChannel channel = event.getChannel();
		
		switch(args[1]) {
			case "mine":
				mine(event);
				return true;
			
			case "extract":
				
				
				extract(event);
				return true;
				
				
			case "admin":
				admin(event);
				
				return true;
				
			case "ma":
				//ps ma int Str
				// 0 1   2   3   length = 4
				if(args.length != 4) {
					return false;
				}
				int ma = Integer.valueOf(args[2]);
				String  str = args[3];
				
				int role = (int)(Math.random()*20+1);
				if(role < ma/2) {
					sendMessage(channel,"You got 2 points!");
					sendMessageDM("```" + event.getAuthor().getName() + " requested an MA. They claimed to have a " + ma + " money stat, and I roled an " + role +
							" so they would get 2 points. They have requested it go towards " + str+ "```");
				}else if(role < ma){
					sendMessage(channel,"You got 1 point!");
					sendMessageDM("```" + event.getAuthor().getName() + " requested an MA. They claimed to have a " + ma + " money stat, and I roled an " + role +
							" so they would get 1 points. They have requested it go towards " + str + "```");
				}else {
					sendMessage(channel,"You didn't get any points  :(");
				}
				
				return true;
				
			
		
		}
		
		
		return true;
	}
	
	
	private boolean printHelp(MessageReceivedEvent event) {
		sendMessage(event.getChannel(), "**help menu coming soon**");
		
		
		
		return true;
	}
	
	private boolean mine(MessageReceivedEvent event) {
		

		manager.mineFor(event);
		return false;
	}
	
	private boolean extract(MessageReceivedEvent event) {
		int mode;
		String modeS = "-3";
		try {
			modeS = event.getMessage().getContent().split(" ")[2];
		}catch (ArrayIndexOutOfBoundsException e) {
			sendMessage(event.getChannel(), "You need an argument");
			return true;
		}
		if(modeS.equalsIgnoreCase("all")){
			modeS = "-2";
		}else if(modeS.equalsIgnoreCase("half")) {
			modeS = "-1";
		}else {
			try {
				mode = Integer.parseInt(modeS);
			} catch (NumberFormatException e) {
				sendMessage(event.getChannel(), "not a vaild argument");
				return true;
			}
			
		}
		mode = Integer.parseInt(modeS);
		event.getChannel().sendMessage(manager.extract(mode));
		return true;
	}
	

	private boolean admin(MessageReceivedEvent event) {
		//so we know args.length != 0 or 1, 
		
		//ps admin command args  
		//0   1      2      3    
//length:     1   2      3      4
		String[] args = event.getMessage().getContent().split(" ");
		IChannel channel = event.getChannel();
		if(args.length >= 3) {
			switch(args[2]) {
				case "delete":
					if(args.length == 4 ) {
						if(args[3].equals("people")){
							manager.hardcodePopulatePeople();
							sendMessage(channel,"cleared people");
						}else if(args[3].equals("miners")) {
							manager.populateMinersBlank();
							sendMessage(channel,"cleared miners");
						}else if(args[3].equals("both")) {
							manager.populateMinersBlank();
							manager.hardcodePopulatePeople();
							sendMessage(channel,"cleared both miners and people");
						}else {
							sendMessage(channel,"not something I can delete");
					
						}
					}else {
						sendMessage(channel,"needs something to delete");
					
					}
					return true;
				case "addsystem":
					
					
					if(args.length != 5) {
						sendMessage(channel,"wrong inputs");
						return true;
					}
					//   ps(0) admin(1) addsystem(2) id(3) planet(4)
					     //1 		2			3	   4      5
					String id = args[3];
					String planet = args[4];
					manager.addPlanet(id, planet);
					sendMessage(channel,"added " + client.getUserByID(Long.valueOf(id)).getName() + " to system " + planet);
					return true;
					
					
				case "getDB":
					sendMessage(event.getChannel(),manager.getEntrys("people"));
					sendMessage(event.getChannel(),manager.getEntrys("miners"));
					
					return true;
				case "help":
					sendMessage(channel, "```admin comands:\n"
							+ "ps admin\n"
							+ "\tdelete\n"
							+ "\t\t-people\n"
							+ "\t\t-miners\n"
							+ "\t\t-both\n"
							+ "\taddsystem player_id system_name\n"
							+ "\tgetDB```");
					return true;
				default:
					sendMessage(channel, "incorrect arguments. use ps admin help for all admin commands");
			}
		}else {
			sendMessage(channel,"you need aditional arguments. use ps admin help for all admin commands");
		}
		return true;
	}
	
	private void sendMessage(IChannel channel,String text) {
		m.sendMessage(channel, text);
	}
	
	private void sendMessageDM(String text) {
		m.sendMessage(client.getOrCreatePMChannel(client.getUserByID(279412525051674624L)), text);
	}
	
}
