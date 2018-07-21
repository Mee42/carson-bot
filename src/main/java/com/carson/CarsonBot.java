package com.carson;


import com.carson.classes.*;
import com.carson.commandManagers.Register;
import com.carson.commands.gg.Taxation;
import com.carson.dataObject.DBHandler;
import com.carson.lavaplayer.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageDeleteEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.MissingPermissionsException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CarsonBot {
	private IDiscordClient client;
	
	
	private static final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private static final Map<Long, GuildMusicManager> musicManagers  = new HashMap<>();

    private Register reg;
    
    
	void importClient(IDiscordClient client) {
		this.client = client;
	}
	



	@EventSubscriber
	public void onStartup(ReadyEvent event) {
	    CarsonBot.updateMemberCount(client);
        //this enables the logger
//        ((Discord4J.Discord4JLogger) Discord4J.LOGGER).setLevel(Level.TRACE);
		
		
        reg = new Register(client);
        

		new Thread(() -> Prime.getInstance().initulize()).start();
//        Taxation.start(client);
        startBTCApiCaller(client);
        System.out.println("BOOT: bot started");

    }
	
	public static void updateMemberCount(IDiscordClient client){
        List<IGuild> guilds = client.getGuilds();
        int users = 0;
        for(IGuild guild : guilds){
            users+= guild.getTotalMemberCount();
        }
        client.changePresence(StatusType.ONLINE, ActivityType.WATCHING," over " + users + " users");
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
		CarsonBot.updateMemberCount(client);
		if(event.getGuild().getLongID() == 208023865127862272L) {
			return;
		}/**/
		try {
			sendMessage(event.getGuild().getSystemChannel(), "Welcome " + event.getUser().toString() + " to " + event.getGuild().getName() + "\n"
					+ "current members:`" + event.getGuild().getTotalMemberCount() + "`");
		}catch (NullPointerException | MissingPermissionsException e) {
			for(IChannel channel : event.getGuild().getChannels()) {
				if(channel.getModifiedPermissions(client.getOurUser()).contains(Permissions.SEND_MESSAGES) &&
						channel.getModifiedPermissions(client.getOurUser()).contains(Permissions.READ_MESSAGES)) {
					try {
						sendMessage(channel, "Welcome " + event.getUser().toString() + " to " + event.getGuild().getName() + "\n"
								+ "current members:`" + event.getGuild().getTotalMemberCount() + "`");
						break;
					}catch (NullPointerException | MissingPermissionsException ee) {
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
		CarsonBot.updateMemberCount(client);
        String userName = event.getUser().getName();
		long channelId = DBHandler.get().getGuildDataBy(event.getGuild().getLongID()).getLeaveChannel();//correct
		String deathMessage = DBHandler.get().getGuildDataBy(event.getGuild().getLongID()).getLeaveMessage();//correct
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
		
		if(event.getMessage().getContent().equals("~help")) { //sends the help message. needs to be here, because we need to be able to pass the register to the help command462681259370610689
			SendHelp.sendHelp(event, reg);
		}
		}//end of handle method


	private void startBTCApiCaller(IDiscordClient client){
		Runnable r = () -> {
			try {
				new BTC().downloadPrice(client,true);//to not print if different
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		ScheduledExecutorService scheduler =Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(r,1,5, TimeUnit.SECONDS);
	}
	
	
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






