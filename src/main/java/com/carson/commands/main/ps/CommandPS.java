package com.carson.commands.main.ps;

import com.carson.commandManagers.*;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;

public class CommandPS extends Command implements ICommand{

	public CommandPS(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return false;
//		
//		if(event.getGuild().getLongID() == 428613801269788687L) {return false;}
//
//		return event.getMessage().getContent().startsWith("ps");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		if(event.getGuild().getLongID() != 428613801269788687L) {return;}

		String[] args = event.getMessage().getContent().split(" ");
		if(args.length == 0) {return ;} //if the message doesn't have content
		if(args.length == 1) {printHelp(event);return;} //prints the help message if there is no other arguments
		
		IChannel channel = event.getChannel();
		switch(args[1]) {
		case "mine":
			if(!MinerManager.SmineFor(event)) {
				if(event.getGuild().getLongID() == 428613801269788687L) {return;}
				sendMessage(event, "sorry, there was an error mining for you. You may have mined before this hour");
			}else {
				if(event.getGuild().getLongID() == 428613801269788687L) {return;}
				sendMessage(event, "Mined for " + event.getAuthor().getDisplayName(event.getGuild()));
			}
			return;
		
		case "extract":
			System.out.println("DEBUG: extracting");
			long id = event.getAuthor().getLongID();
			if(!(id == 293853365891235841L || id == 317104272405823489L || id ==279412525051674624L)) {
				sendMessage(event, "you lack perms to run that command. please contact <@279412525051674624> or <@317104272405823489> or <@293853365891235841>");
				return ;
			}
			
			
			extract(event);
			return ;
			
			
		case "admin":
			long id1 = event.getAuthor().getLongID();
			if(!(id1 == 293853365891235841L || id1 == 317104272405823489L || id1 ==279412525051674624L)) {
				sendMessage(event, "you lack perms to run that command. please contact <@279412525051674624> or <@317104272405823489> or <@293853365891235841>");
				return ;
			}
			admin(event);
			
			return ;
			
		case "ma":
			//ps ma int Str
			// 0 1   2   3   length = 4
			if(args.length != 4) {
				return ;
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
			
			return ;
			
		
	
	}
		
		return ;
	}

	@Override
	public String getName() {
		return "ps";
	}

	@Override
	public String getDisciption() { 	
		return "only usable for the planet sim. use ps help for help";
	}
	
	private boolean printHelp(MessageReceivedEvent event) {
		
		EmbedBuilder b = new EmbedBuilder();
		b.withTitle("Planet Sim Commands");
		b.withAuthorName("by Carson Bot");
		
		b.appendField(
				"ps mine", 
				"mines for you. no other arguments needed",
				false);
		b.appendField(
				"ps extract <*int*/all/half>", 
				"extracts some miner records. **warning:** once you extract them, the records are gone. Only Mods can use this command",
				false);
		b.appendField(
				"ps admin help", 
				"returns the admin help menu.",
				false);
		
		event.getChannel().sendMessage(b.build());
		
		return true;
		
	}
	
	private void sendMessageDM(String text) {
		sendMessage(client.getOrCreatePMChannel(client.getUserByID(279412525051674624L)), text);
	}

	
	private boolean extract(MessageReceivedEvent event) {
		System.out.println("DEBUG:extracting");
		int mode = -4;
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
			
			
		}
		try {
			mode = Integer.parseInt(modeS);
		} catch (NumberFormatException e) {
			sendMessage(event.getChannel(), "not a vaild argument");
			return true;
		}
		event.getChannel().sendMessage(new MinerManager(client).extract(mode));
		return true;
	}
	
	
	//runs all admin commands 
	private boolean admin(MessageReceivedEvent event) {
		//so we know args.length != 0 or 1, 
		
		//ps admin command args  
		//0   1      2      3    
//length:     1   2      3      4
		MinerManager manager = new MinerManager(event.getClient());
		
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
							manager.hardcodePopulatePeople();
							manager.populateMinersBlank();
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
					event.getChannel().sendMessage(manager.getEntrys("people"));
					event.getChannel().sendMessage(manager.getEntrys("miners"));
					
					return true;
				case "mine":
					
					if(!MinerManager.SAdminMineFor(args[3])) {
						sendMessage(event, "well, shit. there was an error with the DB, not the time. id:" + args[3]);
					}else {
						sendMessage(event, "done!");
					}
					
					
					return true;
					
				case "add":
					try {
						String type = args[3];
						String playerId = args[4];
						int ammount = Integer.parseInt(args[5]);
						new MinerManager(client).fix(playerId, type, ammount);
						sendMessage(event, "looks like nothing failed");
					}catch (Exception e) {
						sendMessage(event, "something happened, I'm not sure what. please use proper sintax");
					}
					
					return true;
				case "help":
					sendMessage(channel, "```admin comands:\n"
							+ "delete\n"
							+ "\t-people\n"
							+ "\t-miners\n"
							+ "\t-both\n"
							+ "addsystem player_id system_name\n"
							+ "mine *player_id*\n"
							+ "add <metal/coal/oil/neo/silicon> *player_id* *int_ammount*\n"
							+ "getDB```");
					return true;
				default:
					sendMessage(channel, "incorrect arguments");
			}
		}else {
			sendMessage(channel,"you need aditional arguments. use ps admin help for all admin commands");
		}
		return true;
	}
	
}
