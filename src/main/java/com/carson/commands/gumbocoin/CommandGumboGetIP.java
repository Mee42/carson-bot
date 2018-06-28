package com.carson.commands.gumbocoin;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;		  

public class CommandGumboGetIP extends Command implements ICommand{

	public CommandGumboGetIP(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().toLowerCase().equals("~gumbo get_ip");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		sendMessage(event,"ip should be:`" + getIp()  + "`");
	}

	@Override
	public String getName() {
		return "~gumbo get_ip";
	}

	@Override
	public String getDisciption() {
		return "get the ip of the blockchain server to connect to";
	}
	public static String getIp(){
        URL whatismyip;
		try {
			whatismyip = new URL("http://checkip.amazonaws.com");
		} catch (MalformedURLException e1) {
			return "`not avalible right now. It's probably:`192.168.10.202";
		}
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        } catch (IOException e) {
			return "`not avalible right now. It's probably:`192.168.10.202";
		} finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}