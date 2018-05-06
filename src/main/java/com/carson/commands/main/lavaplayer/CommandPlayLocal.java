package com.carson.commands.main.lavaplayer;

import java.io.File;
import java.util.List;

import com.carson.classes.FileIO;
import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

public class CommandPlayLocal extends Command implements ICommand{
	
	private LavaplayerMain m;
	
	public CommandPlayLocal(IDiscordClient c, LavaplayerMain m) {
		super(c);
		this.m = m;
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().startsWith("v~local");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		if(event.getMessage().getContent().equals("v~local~get") || event.getMessage().getContent().equals("v~local")) {
			getLocal(event);
			return;
		}
		
		String text = event.getMessage().getContent().substring(8, event.getMessage().getContent().length());
		List<File> files = FileIO.use(new File("/home/carson/music/Music")).listFiles();
		
		for(File file : files) {
			if(file.getName().substring(0, file.getName().length()-4).equals(text) ){
				if(event.getAuthor().getLongID() == 293853365891235841L || event.getAuthor().getLongID() == 398876612701192212L) {
					m.playFile(event.getChannel(),file);
					System.out.println("EVENT: playing " + file.getName() + " in guild " + event.getGuild().getName() );
					return ;
				}else if(file.getParentFile().equals(new File("/home/carson/music/Music"))){
					System.out.println("EVENT: playing " + file.getName() + " in guild " + event.getGuild().getName() );
					m.playFile(event.getChannel(),file);
					return ;
				}
			}
		}
		System.out.println("EVENT: couldn't find music file");
		sendMessage(event.getChannel(), "Couldn't find that piece of music. try v~local~get, or be depressed because you don't have perms to play some songs");

		
	}
	
	//get all local songs and there names
		private void getLocal(MessageReceivedEvent event) {
			System.out.println("EVENT: getLocal called by " + event.getAuthor().getName());
			EmbedBuilder builder = new EmbedBuilder();
			builder.withColor(255, 0, 0);
			builder.withAuthorName("Carson-Bot");
			List<File> files = FileIO.use(new File("/home/carson/music/Music")).listFiles();
			builder.withTitle("Local files:");
			for(File file : files) {
				if(event.getAuthor().getLongID() == 293853365891235841L || event.getAuthor().getLongID() == 398876612701192212L || file.getParentFile().equals(new File("/home/carson/music/Music"))){
					builder.appendField(file.getName().substring(0, file.getName().length()-4), file.getAbsolutePath(),true);		
				}
			}
			
			event.getChannel().sendMessage(builder.build());
		}
	

	@Override
	public String getName() {
		return "v~local";
	}

	@Override
	public String getDisciption() {
		return "not done yet....";
	}

}
