package com.carson.commands.main.hangman;

import java.io.File;

import com.carson.classes.FileIO;
import com.carson.commandManagers.Command;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class CommandHangmanTwo extends Command {

	private HangmanObject h;
	
	public CommandHangmanTwo(IDiscordClient c,HangmanObject h) {
		super(c);
		this.h = h;
	}

	@Override
	public boolean test(MessageReceivedEvent event,String content, String[] args) {
		return (h.hangmanBoolean && h.hangman.verifyFlop(event, event.getAuthor()) && !event.getChannel().isPrivate() && event.getMessage().getContent().length() == 1);
	}

	@Override
	public void run(MessageReceivedEvent event,String content, String[] args) {

		
		IUser author = event.getAuthor();

		IChannel channel = event.getChannel();
		
			

			char letter = content.charAt(0);
			
			for(int i = 0;i<h.hangmanText.length();i++) {
				if(letter == h.hangmanText.charAt(i)) {
					if(h.hangmanDone.get(i) == '-') {
						h.hangmanDone.set(i, letter);
					}else {
						sendMessage(channel,"you already sent that character");
					}
				}
				
				
			}
			
			
			String temp = "";
			for(int q = 0;q<h.hangmanDone.size();q++) {
				temp+=h.hangmanDone.get(q);
			}
			
			if(temp.equals(h.hangmanText)) {
				sendMessage(channel,"Correct! the word was: **" + h.hangmanText + "**. The winner was " + author.getName()+". They get 500 XP!");
				new File("C:\\users\\carson\\desktop\\discord\\carson-bot\\ranks\\" + event.getGuild().getName() + "\\").mkdirs();
				FileIO fR = new FileIO("C:\\users\\carson\\desktop\\discord\\carson-bot\\ranks\\" + event.getGuild().getName() + "\\" + author.getName() + ".txt");
				long xp = Long.valueOf(fR.readList().get(0));
				xp+= 500;
				fR.write(String.valueOf(xp));
				
				System.out.println("EVENT:hangman game ended, " + author.getName() + " won");
				h.hangman.setActive(false);
			}else {
				temp = "";
				for(int q = 0;q<h.hangmanDone.size();q++) {
					temp = temp +" " + h.hangmanDone.get(q);
				}
				
				sendMessage(channel,"the word is:```" + temp + "```");
			}
			
			
			return ;
			
			
		
		
		
		
	}

	@Override
	public String getName() {return null;}

	@Override
	public String getDisciption() {return null;}

}
