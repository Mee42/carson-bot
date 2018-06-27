package com.carson.commands.gumbocoin;

import java.util.List;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;
import com.carson.dataObject.DataGetter;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

public class CommandGumboGetPublicKey extends Command implements ICommand{

	public CommandGumboGetPublicKey(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().toLowerCase().startsWith("~gumbo get_key");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		List<IUser> users = event.getMessage().getMentions();
		if(users.size() == 0) {
			sendMessage(event, "you need to mention someone");
			return;
		}
		IUser user = users.get(0);
		String publicKey = DataGetter.getInstance().getUserData(user.getLongID()).getPublicKey();
		if(publicKey == null) {
			sendMessage(event, "that person doesn't have a registered public key");
			return;
		}
		sendMessage(event, "their public key is:`" + publicKey + "`");
	}

	@Override
	public String getName() {
		return "~gumbo get_key @mention";
	}

	@Override
	public String getDisciption() {
		return "get the public key of the user mentioned";
	}

}
