package com.carson.commands.main;

import com.carson.commandManagers.Command;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class CommandSoftBan extends Command {
    public CommandSoftBan(IDiscordClient c) {
        super(c);
    }

    @Override
    public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        return content.startsWith("softban");
    }

    @Override
    public PermissionLevel getWantedPermissionLevel() {
        return PermissionLevel.MOD;
    }

    @Override
    public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
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
    public String getCommandId() {
        return "softban";
    }
}
