package com.carson.commands.main;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class CommandSoftBan extends Command implements ICommand {
    public CommandSoftBan(IDiscordClient c) {
        super(c);
    }

    @Override
    public boolean test(MessageReceivedEvent event) {
        return event.getMessage().getContent().startsWith("~softban");

    }

    @Override
    public void run(MessageReceivedEvent event) {
        if(!event.getAuthor().getPermissionsForGuild(event.getGuild()).contains(Permissions.ADMINISTRATOR)){
            sendMessage(event, "you do not have permission to do that");
            return;
        }
        if(event.getMessage().getMentions().size() != 1){
            sendMessage(event, "you can only softban one person");
            return;
        }
        IUser toBan = event.getMessage().getMentions().get(0);
        IGuild guild = event.getGuild();
        if(!(client.getOurUser().getPermissionsForGuild(guild).contains(Permissions.BAN) && client.getOurUser().getPermissionsForGuild(guild).contains(Permissions.MANAGE_SERVER))){
            sendMessage(event, "I don't have permission to do that");
            return;
        }
//        guild.banUser(toBan);
        guild.banUser(toBan, 7);
        guild.pardonUser(toBan.getLongID());
        IChannel toBanChannel = toBan.getOrCreatePMChannel();
        sendMessage(toBanChannel, "you have been softbanned. Rejoin at:" + guild.getExtendedInvites().get(0));
        sendMessage(event, "Done!");
    }

    @Override
    public String getName() {
        return "~softban @mention";
    }

    @Override
    public String getDisciption() {
        return "bans the mentioned user, and then unbans them. needs MANAGE_SERVER and BAN permission. User using command needs ADMIN permission. will only delete 7 days worth of messages";
    }
}
