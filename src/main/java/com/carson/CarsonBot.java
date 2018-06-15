package com.carson;


import java.io.File;
import java.util.*;

import com.carson.classes.*;
import com.carson.commandManagers.Register;
import com.carson.dataObject.DataGetter;
import com.carson.dataObject.GuildDataOrginizer;
import com.carson.lavaplayer.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.*;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.*;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.impl.events.shard.LoginEvent;
import sx.blah.discord.handle.obj.*;

public class CarsonBot { // Curl+shift + / (on num pad)
	//VERSION: 0-3-0
	//variables
	private IDiscordClient client;
	private Messanger messanger;
	
	
	
	private static final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private static final Map<Long, GuildMusicManager> musicManagers  = new HashMap<>();

    private Register reg;
    
    
	public void importClient(IDiscordClient client) {
		this.client = client;
		
		
	}
	
	
	@EventSubscriber
	public void onStartup(LoginEvent event) {
			client.changePresence(StatusType.ONLINE, ActivityType.WATCHING," your every move");
		
		
		messanger = new Messanger(client);
		System.out.println("BOOT: bot started");
//		client.changeAvatar(Image.forFile(new File("C:\\Users\\Carson\\Desktop\\discord\\carson-bot\\profile.jpg")));
		
		
		
        
        //this enables the logger
//        ((Discord4J.Discord4JLogger) Discord4J.LOGGER).setLevel(Level.TRACE);
		
		
        reg = Register.build(client); //puts the subregisters into the reg. and inports the client
        
        DataGetter.getInstance()
        .importFromJson(); //starts up the data getter. takes the data from the json file
        
	}
	
	
	@EventSubscriber
	public void onMessageDeleted(MessageDeleteEvent event) {
		if(event.getGuild().getLongID() != 400786190619639839L) { //mcpoland
			return;
		}
		if(event.getAuthor().getLongID() ==318783502768144384L) { //yellow toad
			return;
		}
		if(event.getAuthor().getLongID() ==422191638736142346L) { //karxn
			return;
		}
		long messageChannel  = 455860841611526144L;
		
		new Messanger().sendMessage(client.getChannelByID(messageChannel), "*a message was deleted* user:`" + event.getAuthor().getName() + "` in channel:`" + event.getChannel().getName() + "`   ```" + event.getMessage().getFormattedContent() + "```");
	}
	
	
	@EventSubscriber
	public void onUserJoin(UserJoinEvent event) {
		sendMessage(event.getGuild().getSystemChannel(), "Welcome " + event.getUser().toString() + " to " + event.getGuild().getName() + "\n"
				+ "current members:`" + event.getGuild().getTotalMemberCount() + "`");
		
		if(event.getGuild().getLongID() == 422570115217883136L) {
			IRole role = event.getGuild().getRolesByName("fresh gumbo").get(0);
			event.getUser().addRole(role);
			System.out.println("added role for gumbosphere");
		}
	}
	
	@EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event){
		
		//prints the message to the console, as well as the text logs, and gives XP onto global XP
		Logger.log(event);

		
		
		//runs the register. 
		reg.testCommands(event);
		
		if(event.getMessage().getContent().equals("~help")) { //sends the help message. needs to be here, because we need to be able to pass the register to the help command
			SendHelp.sendHelp(event, reg);
		}
		
		//check for profanity is some servers. note - needs to make configuring this a bot action, not hardcoded
		if(ProfanityChecker.check(event)) {return;} 
		
		

		if( gumbo(event) ||  //needs to be ported TODO
		cbCommands(event) //needs to be moved TODO
				) {
			return;
		}
		
		
		
		
		

	}//end of handle method
	
	
	
	//sends message
	public void sendMessage(IChannel channel, String message){
		messanger.sendMessage(channel, message);
    }
	
	
	
	
	//gumbo script TODO NEEDS TO BE CONVERTED
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






