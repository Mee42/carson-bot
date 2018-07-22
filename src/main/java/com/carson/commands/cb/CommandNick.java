package com.carson.commands.cb;

import com.carson.commandManagers.Command;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RateLimitException;

public class CommandNick extends Command{
	  

	public CommandNick(IDiscordClient c) {
		super(c);
		
	}

    @Override
    public boolean test(MessageReceivedEvent event, String content, String[] args) {
		return content.startsWith("cb-nick");
	}
    @Override
    public void run(MessageReceivedEvent event, String content, String[] args) {
		try {
			String text = event.getMessage().getContent().split(" ")[1];
			event.getGuild().setUserNickname(client.getOurUser(), text);
		}catch (ArrayIndexOutOfBoundsException e) {
			sendMessage(event,"you need an argument");
		}catch (RateLimitException ef) {
			sendMessage(event,"you are updating it to fast");
		}
	}

    @Override
    public String getName(){
        return "cb-nick *text*";
    }

	@Override
	public String getDisciption() {
		return "changes the nickname of the bot";
	}

    @Override
    public PermissionLevel getWantedPermissionLevel() {
        return PermissionLevel.MOD;
    }
}