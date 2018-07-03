package com.carson;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.carson.classes.*;
import com.carson.commandManagers.Register;
import com.carson.dataObject.DataGetter;
import com.carson.lavaplayer.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageDeleteEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import sx.blah.discord.handle.impl.events.shard.LoginEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.handle.obj.StatusType;

public class CarsonBot { // Curl+shift + / (on num pad)
	private IDiscordClient client;
//	private Messanger messanger;
	
	
	
	private static final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private static final Map<Long, GuildMusicManager> musicManagers  = new HashMap<>();

    private Register reg;
    
    
	public void importClient(IDiscordClient client) {
		this.client = client;
	}
	
	
	@EventSubscriber
	public void onStartup(LoginEvent event) {
			client.changePresence(StatusType.ONLINE, ActivityType.WATCHING," your every move");
		
		
		System.out.println("BOOT: bot started");
		
		
		
        
        //this enables the logger
//        ((Discord4J.Discord4JLogger) Discord4J.LOGGER).setLevel(Level.TRACE);
		
		
        reg = Register.build(client); //puts the subregisters into the reg. and imports the client
        
        DataGetter.getInstance()
        .importFromJson(); //starts up the data getter. takes the data from the json file

		new Thread(new Runnable() {
			@Override
			public void run() {
				Prime.getInstance().initulize();
			}
		}).start();


	}
	
	
	@EventSubscriber
	public void onMessageDeleted(MessageDeleteEvent event) {
		if(event.getGuild().getLongID() != 400786190619639839L) { //if server is not mcpoland, return
			return;
		}
		if(event.getAuthor().getLongID() ==318783502768144384L) { //if user is yellow toad, return 
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
		if(event.getGuild().getLongID() == 208023865127862272L) {
			return;
		}/**/
		try {
			sendMessage(event.getGuild().getSystemChannel(), "Welcome " + event.getUser().toString() + " to " + event.getGuild().getName() + "\n"
					+ "current members:`" + event.getGuild().getTotalMemberCount() + "`");
		}catch (NullPointerException e) {
			for(IChannel channel : event.getGuild().getChannels()) {
				if(channel.getModifiedPermissions(client.getOurUser()).contains(Permissions.SEND_MESSAGES) &&
						channel.getModifiedPermissions(client.getOurUser()).contains(Permissions.READ_MESSAGES)) {
					try {
						sendMessage(channel, "Welcome " + event.getUser().toString() + " to " + event.getGuild().getName() + "\n"
								+ "current members:`" + event.getGuild().getTotalMemberCount() + "`");
						break;
					}catch (NullPointerException ee) {
//						ee.printStackTrace();
					}
				}
			}
		}
		if(event.getGuild().getLongID() == 422570115217883136L) {
			IRole role = event.getGuild().getRolesByName("fresh gumbo").get(0);
			event.getUser().addRole(role);
			System.out.println("added role for gumbosphere");
		}
	}
	
	
	@EventSubscriber
	public void onUserLeave(UserLeaveEvent event) {
		String userName = event.getUser().mention();
		long channelId = DataGetter.getInstance().getGuild(event.getGuild().getLongID()).getDeathChannel();
		String deathMessage = DataGetter.getInstance().getGuild(event.getGuild().getLongID()).getDeathMessage();
		if(channelId == -1L) { //if not set yet
			channelId = event.getGuild().getSystemChannel().getLongID();
		}
		deathMessage = deathMessage.replace("[name]", userName);
		try {
			sendMessage(client.getChannelByID(channelId), deathMessage);
		}catch (NullPointerException e) {
			//invalid channel
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
		
		

//	gumbo(event);		
		
		
		
		

	}//end of handle method
	
	
	
	//sends message
	public void sendMessage(IChannel channel, String message){
		new Messanger().sendMessage(channel, message);
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
	 
	
	 private  boolean playFile(final IChannel channel, final File file) {
		 
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






