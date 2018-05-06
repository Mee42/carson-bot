package com.carson;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carson.classes.FileIO;
import com.carson.classes.Googler;
import com.carson.classes.Gumbo;
import com.carson.classes.Messanger;
import com.carson.classes.ProfanityChecker;
import com.carson.classes.RunCMD;
import com.carson.classes.SendHelp;
import com.carson.classes.VerifyAwaiting;
import com.carson.commandManagers.Register;
import com.carson.commands.main.lavaplayer.LavaplayerMain;
import com.carson.commands.main.ps.PlanetSim;
import com.carson.lavaplayer.GuildMusicManager;
import com.carson.tic.Tac;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.StatusType;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.audio.AudioPlayer;

public class CarsonBot { // Curl+shift + / (on num pad)
	
	//variables
	private boolean locked;
	private IDiscordClient client;
	private Messanger messanger;
	
	private Tac tac = new Tac();
	private VerifyAwaiting tacV = new VerifyAwaiting();
	private String moves = "";
	
	
	
	private VerifyAwaiting hangman = new VerifyAwaiting();
	private String hangmanText = "";
	private boolean hangmanBoolean = false;
	private List<Character> hangmanDone = new ArrayList<Character>();
	
	private VerifyAwaiting dnd = new VerifyAwaiting();
	
	private static final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private static final Map<Long, GuildMusicManager> musicManagers  = new HashMap<>();

    private Register reg;
    
    
    private PlanetSim ps;
    
	public void startup(IDiscordClient client, boolean locked) {
		this.locked = locked;
		this.client = client;
		
		if(locked) {
			client.changePresence(StatusType.ONLINE, ActivityType.WATCHING," your every move");
		}else {
			client.changePresence(StatusType.DND, ActivityType.PLAYING," IM LOCKED");
		}
		
		
		messanger = new Messanger(client);
		ps = PlanetSim.make(client, messanger);
		
		System.out.println("BOOT: bot started");
//		client.changeAvatar(Image.forFile(new File("C:\\Users\\Carson\\Desktop\\discord\\carson-bot\\profile.jpg")));
		
		LavaplayerMain m = new LavaplayerMain();
		
		
        
        //this enables the logger
//        ((Discord4J.Discord4JLogger) Discord4J.LOGGER).setLevel(Level.TRACE);
        reg = Register.build(client);
        
        
	}
	
	
	
	@EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event){
		
		
		
		//lruns the register. 
				reg.testCommands(event);
		
		if(event.getMessage().getContent().equals("~help"))
			SendHelp.sendHelp(event, reg);
		
		//check for profanity is some servers. note - needs to make configuring this a bot action, not hardcoded
		if(ProfanityChecker.check(event)) {return;} 
		
		//checks to see if the bot is locked. may be removed soon
		if(checkLock(event)){return;}


		if( 
				
				
				
				
				startHangman(event) ||
				hangmanTwo(event) || 
				hangmanOne(event)|| 
				
				
				
				gumbo(event) || 
				
				dndStart(event) ||
				dnd(event) ||
				
				playYoutubeLink(event) || 
				playYoutubeKeywords(event)|| 				
				
				
				
				
				tic1(event) ||
				tic2(event) ||
				
				cbCommands(event) ||
				musicCommands(event) //not sure how if this is broken
				
				
				) 
		
		
				{
			return;
		}
		
		
		
		
		

	}//end of handle method
	
	
	
	//sends message
	public void sendMessage(IChannel channel, String message){
		messanger.sendMessage(channel, message);
    }
	
	//send message without console print (used for tic tac toe board, mainly)
	private void sendMessageClean(IChannel channel, String message) {
		messanger.sendMessageClean(channel, message);
		
	}
	
	
	//checks to see if the command will be allowed to run
	private boolean checkLock(MessageReceivedEvent event) {
		if(!locked) {
			return false;
		}
		
		if(event.getChannel().isPrivate() && event.getAuthor().getLongID() != 293853365891235841L ) {
			return true;
		}
		if(event.getAuthor().getLongID() == 293853365891235841L || event.getGuild().getLongID() == 432560125299916810L) {
			return false;
		}

		return true;
	}

	
	
	
	//METHODS
	
	
	
	//start hangman game
	private boolean startHangman(MessageReceivedEvent event) {
		if(!event.getMessage().getContent().equals("~hangman")) {
			return false;
		}
		
		if(hangman.set(event)) {
			System.out.println("EVENT:starting hangman game with" + event.getAuthor().getName());
			sendMessage(event.getChannel(), "Nice! Now go check your DM's");
			sendMessage(event.getAuthor().getOrCreatePMChannel(), "What do you want your word to be?");
		}else {
			sendMessage(event.getChannel(), "Sorry, someone is already using hangman right now. ");
			System.out.println(event.getAuthor().getName() + " tryed to use the hangman bot, but " + hangman.getInitalEvent().getAuthor().getName()+ " is already using it");
		}
		
		
		
		
		
		return true;
	}
	
	//gumbo script
	private boolean gumbo(MessageReceivedEvent event) {
		IMessage message = event.getMessage();
		String text = message.getContent();
		IChannel channel = event.getChannel();
		
		
		if(text.length()>=6) {
			String first5 = text.substring(0, 6);
			
		
			if(first5.equals("~gumbo")) {
				System.out.println("EVENT:running gumbo");
				if(text.length() >= 8) {
					String input = text.substring(7, text.length());
					System.out.println(input);
					Gumbo g = new Gumbo();
					sendMessage(channel, g.code(input, true));
					
				}else {
					sendMessage(channel, "you need an input");
					System.out.println("WARN:gumbo used without text");

				}
				return true;
			}else if(text.length()>=8) {
				if(text.substring(0,8).equals("~english")) {
					System.out.println("EVENT:running english");
					if(text.length() >= 10) {
						String input = text.substring(9, text.length());
						System.out.println(input);
						Gumbo g = new Gumbo();
						sendMessage(channel,g.code(input, false));
					}else {
						System.out.println("WARN:english used without text");
						sendMessage(channel,"you need an input");
						
					}
					return true;
				}
				
			}

		}
		
		return false;
		
		
	}
	
	//hangmanTwo
	private boolean hangmanTwo(MessageReceivedEvent event) {
		
		IUser author = event.getAuthor();
		String text = event.getMessage().getContent();
		IChannel channel = event.getChannel();
		
		
		if(hangmanBoolean && hangman.verifyFlop(event, author) &&!channel.isPrivate() && text.length() == 1) {
			
			
			char letter = text.charAt(0);
			
			for(int i = 0;i<hangmanText.length();i++) {
				if(letter == hangmanText.charAt(i)) {
					if(hangmanDone.get(i) == '-') {
						hangmanDone.set(i, letter);
					}else {
						sendMessage(channel,"you already sent that character");
					}
				}
				
				
			}
			
			
			String temp = "";
			for(int q = 0;q<hangmanDone.size();q++) {
				temp+=hangmanDone.get(q);
			}
			
			if(temp.equals(hangmanText)) {
				sendMessage(channel,"Correct! the word was: **" + hangmanText + "**. The winner was " + author.getName()+". They get 500 XP!");
				new File("C:\\users\\carson\\desktop\\discord\\carson-bot\\ranks\\" + event.getGuild().getName() + "\\").mkdirs();
				FileIO fR = new FileIO("C:\\users\\carson\\desktop\\discord\\carson-bot\\ranks\\" + event.getGuild().getName() + "\\" + author.getName() + ".txt");
				long xp = Long.valueOf(fR.readList().get(0));
				xp+= 500;
				fR.write(String.valueOf(xp));
				
				System.out.println("EVENT:hangman game ended, " + author.getName() + " won");
				hangman.setActive(false);
			}else {
				temp = "";
				for(int q = 0;q<hangmanDone.size();q++) {
					temp = temp +" " + hangmanDone.get(q);
				}
				
				sendMessage(channel,"the word is:```" + temp + "```");
			}
			
			
			return true;
			
			
		}
		
		
		
		
		return false;
	}


		
	//dnd
	private boolean dnd(MessageReceivedEvent event) {
		String text = event.getMessage().getContent();
		
		
		if(dnd.verify(event)) {
			
			if(text.length()<=2) {
				return false;
			}
			
			int Xd = 0;
			int dX = 0;
			int i =0;
			String temp = "";
			
			while(true) {
				if(text.length() == i) {
					try {
					dX = Integer.valueOf(temp);
					}catch (java.lang.NumberFormatException e) {
						sendMessage(event.getChannel(), "sorry, that input is not allowed");
						return true;
					}
					
					break;
				}else if(text.charAt(i) == 'd') {
					Xd = Integer.valueOf(temp);
					temp = "";
					i++;
				}else {
					temp+= text.charAt(i);
					i++;
				}
				
				
				
			}
				System.out.println("EVENT: dnd roll:");

		
		
			
			int total = 0;
			List<Integer> number = new ArrayList<Integer>();
		
			for(int i1 = 0;i1<Xd;i1++) {
				int temp1 = (int)(Math.random()*dX+1);
				number.add(temp1);
				total+=temp1;
				
			}
			System.out.println(total);
			sendMessage(event.getChannel(),"You got:" + total);
			
			dnd.setActive(false);
			return true;
		}
		
		return false;
	}

	
	
	
	//play youtube
	private boolean playYoutubeLink(MessageReceivedEvent event) {
		String text = event.getMessage().getContent();
		if(!text.startsWith("v~link ")) {
			return false;
		}
		System.out.println("EVENT: playing a youtube video from link");
		String url = text.substring(7,text.length());
		playLink(event.getChannel(),url);
		
		
		
		return false;
	}
	
	//hangmanOne
	private boolean hangmanOne(MessageReceivedEvent event) {
		if(hangman.verifyFromPM(event)) {
			System.out.println("EVENT: hangman word found");
			hangmanText = event.getMessage().getContent();
			sendMessage(event.getChannel(), "Great!");
			hangmanBoolean = true;
			String temp = "";
			String text = event.getMessage().getContent();
			
			for(int i = 0;i<text.length();i++) {
				hangmanDone.add('-');
			}
			
			temp = "";
			for(int q = 0;q<hangmanDone.size();q++) {
				temp = temp +" " + hangmanDone.get(q);
			}
			
			sendMessage(hangman.getInitalEvent().getChannel(),"the word is:```" + temp + "```");
			
			return true;
		}else {
			return false;
		}
	}
	
	
	
	//play a youtube video from keywords	
	private boolean playYoutubeKeywords(MessageReceivedEvent event) {
		String text = event.getMessage().getContent();
		if(!(text.startsWith("v~youtube "))) {
			return false;
		}
		System.out.println("EVENT: youtube played with keywords");
		String url = new Googler().GoogleYoutube(text.substring(10,text.length()));
		System.out.println(url);
		playLink(event.getChannel(),url);
		
		
		return true;
	}
	
	
	
	

	
	//all carsonbot commands
	private boolean cbCommands(MessageReceivedEvent event) {
		
		
		long id = event.getAuthor().getLongID();
		
		if(!(id == 398876612701192212L ||
		   id == 293853365891235841L ||
		   id ==279412525051674624L
		
				
		 )) {
			
		}
		
		
		IChannel channel = event.getChannel();
		switch(event.getMessage().getContent()) {
			
				
			case "cb-s":
			case "cb-stop":
			
			
				sendMessage(channel,"shuting down");
				System.out.println("EVENT:shuting down the bot");
				this.client.changePresence(StatusType.OFFLINE);
				System.exit(0);
				return true;
				
			case "cb-lock":
				sendMessage(channel,"locked");
				locked = true;
				System.out.println("EVENT:bot locked");
				client.changePresence(StatusType.ONLINE, ActivityType.PLAYING,"CURRENTLY LOCKED");
				return true;
			
			case "cb-unlock":
				sendMessage(channel,"unlocked" );
				System.out.println("EVENT:bot unlocked");
				client.changePresence(StatusType.ONLINE, ActivityType.WATCHING," your every move");
				locked = false;
				return true;
			
			case "cb-getlock":
			case "cb-getLock":
				if(locked) {
					sendMessage(channel,"locked");
				}else {
					sendMessage(channel,"unlocked");
				}
				System.out.println("EVENT: sent onlyMe status, which is:" + locked);
				return true;
			
		
			
				
			case "cb-hangman-kill":
				sendMessage(hangman.getInitalEvent().getChannel(),"this hangman game has been forceable killed");
				System.out.println("EVENT:force killed hangman game");
				hangman.setActive(false);
				return true;
				
				
			case "cb-pc-shutdown":
				RunCMD.run("\"C:\\Users\\Carson\\Desktop\\shutdown.bat");
				System.out.println("EVENT:ran shutdown command");
				return true;
			case "cb-pc-nh-kill":
				RunCMD.runAdmin("taskkill /IM NiceHashMinerLegacy.exe");
				sendMessage(channel,"killed");
				System.out.println("EVENT:killed nicehash");
				return true;
			case "cb-pc-nh-start":
				RunCMD.run("\"C:\\Users\\Carson\\Desktop\\discord\\carson-bot\\batch\\nh-start.bat\"");
				System.out.println("EVENT:started nicehash");
				sendMessage(channel,"started");
				return true;
			
				
			case "cb-tic-kill":
				tacV.setActive(false);
				tac = new Tac();
				sendMessage(channel, "killed");
				System.out.println("EVENT: reset tac");
				return true;
			
			case "cb-our-songs":
				
				
				if(!(
				id == 293853365891235841L ||
				id == 398876612701192212L
						)) {
					sendMessage(event.getChannel(), "Chris, you really thought you would go ahead an listen to that? shame on you. \n`username is not in the sudoers file. This incident will be reported.`");
					sendMessage(client.getOrCreatePMChannel(client.getUserByID(293853365891235841L)), "Chris tryed to do that thing he wasn't supposted to do. cri");
					return true;
				}
				
				List<File> files = FileIO.use(new File("/home/carson/music/Music/isha")).listFiles();
				client.getGuildByID(432560125299916810L).getVoiceChannelsByName("for_lovers").get(0).join();
				
				for(File file : files) {
					playFile(event.getChannel(), file);
				}
				
				
				return true;
			case "cb-help":
				System.out.println("EVENT: sent cb help message");
				sendMessage(channel,"help:\n"
						+ "watching\n"
						+ "s\n"
						+ "stop\n"
						+ "lock\n"
						+ "unlock\n"
						+ "getlock\n"
						+ "hangman-kill\n"
						+ "pc-shutdown\n"
						+ "pc-nh-start\n"
						+ "pc-nh-kill\n"
						+ "status\n"
						+ "old\n"
						+ "tic-kill\n"
						+ "out-songs\n"
						+ "help"		
						+ "");
			
				
				
			default:
				return false;
			
		}
		
		
	}
	
	//start tic game
	private boolean tic2(MessageReceivedEvent event) {
		if(!tacV.verify(event)) {
			return false;
		}
		
		String text = event.getMessage().getContent();
		
		if(isInteger(text) && Integer.valueOf(text) < 10) {
			sendMessageClean(event.getChannel(),tac.moveNext(Integer.valueOf(text)));
			
			if(tac.checkWin() == 1) {
				sendMessage(event.getChannel(), "I Won");
				tacV.setActive(false);
				tac = new Tac();
				moves = "";
			}else if(tac.checkWin() == 2) {
				sendMessage(event.getChannel(), "You Won! time to recode the entire stupid bot *fuckingsmarthuman*");
				tacV.setActive(false);
				tac = new Tac();
				System.out.println("ERROR: someone won tictactoe. shit. take a look at there sick moves:" + moves);
				sendMessage(client.getOrCreatePMChannel(client.getUserByID(293853365891235841L)), "ERROR: someone won tictactoe. shit. take a look at there sick moves:" + moves);
			}else if(tac.checkWin() == -1) {
				sendMessage(event.getChannel(), "It was a draw. Better luck next time");
				tacV.setActive(false);
				tac = new Tac();
				moves = "";
			}else {
				moves+=text;
				sendMessageClean(event.getChannel(), "What is your next move?");
			}
		}
		
		
		
		
		return true;
		
		
	}
	
	
	//continue tic game
	private boolean tic1(MessageReceivedEvent event) {
		String text = event.getMessage().getContent();
		if(!text.equals("~tic")) {
			return false;
		}
		if(tacV.getActive()) {
			sendMessage(event.getChannel(),"Sorry, Someone is using the Tic Tac Toe game right now.");
			System.out.println("EVENT:" +tacV.getInitalEvent().getAuthor().getName() + " is using the ticTacToe game right now, so " + event.getAuthor().getName() + " can't");
			return false;
		}
		
		
		tacV.set(event);
		if((int)(Math.random()*2+1) == 2) {
			sendMessageClean(event.getChannel(),tac.start(0));
		}else{
			sendMessageClean(event.getChannel(),tac.start(-1));
		}
		return true;
	}


	
	

	
	


	
	
	//start dnd game
	private boolean dndStart(MessageReceivedEvent event) {
		if(!event.getMessage().getContent().equals("~dnd")) {
			return false;
		}
		
		System.out.println("EVENT: started dnd role");
		sendMessage(event.getChannel(),"what dice do you want to role?(format: XdY, X and Y can be multi-digit");
		dnd.set(event);
		return true;
		
		
	}
	
	
	
	
	
	private static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
	
	//OTHER
	
	
	//test for integer in a string
	
	

	
	
	
	//run all music commands
	private boolean musicCommands(MessageReceivedEvent event) {
		
			
		switch(event.getMessage().getContent()) {

			


		
			
			//MUSIC COMMANDS
				
		
				
			
		
		
			default:
				return false;
			
		}
		
	}
	

		
	//LAVAPLAYER CLASSES
	
	private static synchronized GuildMusicManager getGuildAudioPlayer(IGuild guild) {
        long guildId = guild.getLongID();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setAudioProvider(musicManager.getAudioProvider());

        return musicManager;
    }
	 
	 private static void play(GuildMusicManager musicManager, AudioTrack track) {

	        musicManager.getScheduler().queue(track);
	    }
	 
	 private  void skipTrack(IChannel channel) {
	        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
	        musicManager.getScheduler().nextTrack();

	        sendMessage(channel, "Skipped to next track,.. playing:" + AudioPlayer.getAudioPlayerForGuild(channel.getGuild()).getCurrentTrack());
	    }
	
	 private  void playLink(final IChannel channel, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

       
        
        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
               sendMessage(channel, "Adding to queue " + track.getInfo().title);

                play(musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                sendMessage(channel, "Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")");

                play(musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                sendMessage(channel, "Nothing found by " + trackUrl);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                sendMessage(channel, "Could not play: " + exception.getMessage());
            }
        });
    }

	 private boolean playFile(final IChannel channel, final File file) {
		 
	        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

	        playerManager.loadItem(file.getAbsolutePath(),  new AudioLoadResultHandler() {
	            @Override
	            public void trackLoaded(AudioTrack track) {
	               sendMessage(channel, "Adding to queue " + track.getInfo().title);

	                play(musicManager, track);
	            }

	            @Override
	            public void playlistLoaded(AudioPlaylist playlist) {
	                AudioTrack firstTrack = playlist.getSelectedTrack();

	                if (firstTrack == null) {
	                    firstTrack = playlist.getTracks().get(0);
	                }

	                sendMessage(channel, "Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")");

	                play(musicManager, firstTrack);
	            }

	            @Override
	            public void noMatches() {
	                sendMessage(channel, "Nothing found ");
	            }

	            @Override
	            public void loadFailed(FriendlyException exception) {
	                sendMessage(channel, "Could not play: " + exception.getMessage());
	            }
	        });
		 return true;
		 
		 
	 }











}






