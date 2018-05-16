package com.carson;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carson.classes.FileIO;
import com.carson.classes.Gumbo;
import com.carson.classes.Logger;
import com.carson.classes.Messanger;
import com.carson.classes.ProfanityChecker;
import com.carson.classes.SendHelp;
import com.carson.commandManagers.Register;
import com.carson.lavaplayer.GuildMusicManager;
import com.carson.main.CleanThread;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
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
import sx.blah.discord.handle.obj.StatusType;

public class CarsonBot { // Curl+shift + / (on num pad)
	
	//variables
	private boolean locked;
	private IDiscordClient client;
	private Messanger messanger;
	
	
	
	
	
	private static final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private static final Map<Long, GuildMusicManager> musicManagers  = new HashMap<>();

    private Register reg;
    
    
    
	public void startup(IDiscordClient client, boolean locked) {
		this.locked = locked;
		this.client = client;
		
		if(locked) {
			client.changePresence(StatusType.ONLINE, ActivityType.WATCHING," your every move");
		}else {
			client.changePresence(StatusType.DND, ActivityType.PLAYING," IM LOCKED");
		}
		
		
		messanger = new Messanger(client);
		
		System.out.println("BOOT: bot started");
//		client.changeAvatar(Image.forFile(new File("C:\\Users\\Carson\\Desktop\\discord\\carson-bot\\profile.jpg")));
		
		
		
        
        //this enables the logger
//        ((Discord4J.Discord4JLogger) Discord4J.LOGGER).setLevel(Level.TRACE);
		
		
        reg = Register.build(client); //puts the subregisters into the reg. and inports the client
        
        
	}
	
	
	
	@EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event){
		
		//prints the message to the console, as well as the text logs
		Logger.log(event);

		
		
		//runs the register. 
		reg.testCommands(event);
		
		if(event.getMessage().getContent().equals("~help")) {
			SendHelp.sendHelp(event, reg);
		}
		
		//check for profanity is some servers. note - needs to make configuring this a bot action, not hardcoded
		if(ProfanityChecker.check(event)) {return;} 
		
		//checks to see if the bot is locked. may be removed soon
		if(checkLock(event)){return;}


		if( 
				
				
				
				
				gumbo(event) ||  //needs to be ported
				
				
			
				
				
				
				cbCommands(event) //needs to be moved
				
				) 
		
		
				{
			return;
		}
		
		
		
		
		

	}//end of handle method
	
	
	
	//sends message
	public void sendMessage(IChannel channel, String message){
		messanger.sendMessage(channel, message);
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
	
	


	
	
	

	
	
	
	

	
	//all carsonbot commands
	private boolean cbCommands(MessageReceivedEvent event) {
		
		
		long id = event.getAuthor().getLongID();
		
		if(!(id == 398876612701192212L ||
		   id == 293853365891235841L ||
		   id ==279412525051674624L
		
				
		 )) {
			
		}
		
		
		switch(event.getMessage().getContent()) {
			
				
			
			
			
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






