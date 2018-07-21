package com.carson.commands.main.lavaplayer;

import com.carson.commandManagers.Command;

import sx.blah.discord.api.IDiscordClient;

public abstract class MusicCommand extends Command{
	LavaplayerMain m;
	/*
	 * this is to set up the constructer for lavaplayer automaticly.
	 * probably not really needed, but why not. makes stuff a bit easier
	 * 
	 * 
	 */
	public MusicCommand(IDiscordClient c, LavaplayerMain m) {
		super(c);
		this.m = m;
	}

}
