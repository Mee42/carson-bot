package com.carson.commands.main;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;
import com.carson.dataObject.DataGetter;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandGetJson extends Command implements ICommand{

	public CommandGetJson(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().toLowerCase().equals("~getjson");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		String json = DataGetter.getInstance().getSterilze();
//		if(json.length() > 1900) {
//			json = json.substring(0, 1900);
//		}
//		sendEmbed(event, "```" + json + "```");
		if(json.length() > 2000) {
			Pattern p = Pattern.compile("(?:.|\n){1,2000}");
			Matcher m = p.matcher(json);
			List<String> segments = new ArrayList<String>();
			while (m.find()) {
				segments.add(m.group(1));
			}

			for (String segment : segments) {
				sendMessage(event, "```" + segment + "```");
			}
		}else{
		    sendMessage(event,"```" + json + " ```");
        }
	}

	@Override
	public String getName() {
		return "~getJSON";
	}

	@Override
	public String getDisciption() {
		return "try it. but its not recommended";
	}
	

}
