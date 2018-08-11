package com.carson.commands.cb;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.Register;
import com.carson.dataObject.DBHandler;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;

public class CommandOptInfo extends Command{


	public CommandOptInfo(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
		throw new AssertionError();//should never happen
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		if(!hasPermission(getWantedPermissionLevel(), Register.getPermissionLevel(event)))return false;
		return event.getMessage().getContent().toLowerCase().equals("cb-opt");
	}

	@Override
	public void run(String prefix, String content, MessageReceivedEvent event,String rawContent, String[] args) {
		String message = "all of these people have opted out:\n";
		for(Document d : DBHandler.get().getDB("opt").find(Filters.eq("opt",false))){
			message+=client.getUserByID((long)d.get("_id")).getName() + "\n";
		}
		final String messageFinal = message;
		RequestBuffer.request(()->{event.getChannel().sendMessage(messageFinal);});
	}

	@Override
	public String getCommandId() {
		return "opt";
	}

	@Override
	public PermissionLevel getWantedPermissionLevel() {
		return PermissionLevel.BOT_ADMIN;
	}
}
