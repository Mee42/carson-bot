package com.carson.commands.cb;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.Register;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RateLimitException;

public class CommandNick extends Command{
	  

	public CommandNick(IDiscordClient c) {
		super(c);
		
	}

    @Override
    public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
		throw new RuntimeException();//should never happen
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		if(!hasPermission(getWantedPermissionLevel(), Register.getPermissionLevel(event)))return false;
		return event.getMessage().getContent().startsWith("cb-nick");
	}

	@Override
    public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
		try {
			String text = content.split(" ",2)[1];
			event.getGuild().setUserNickname(client.getOurUser(), text);
		}catch (ArrayIndexOutOfBoundsException e) {
			sendMessage(event,"you need an argument");
		}catch (RateLimitException ef) {
			sendMessage(event,"you are updating it to fast");
		}
	}

	@Override
	public String getCommandId() {
		return "nick";
	}

	@Override
    public PermissionLevel getWantedPermissionLevel() {
        return PermissionLevel.MOD;
    }
}