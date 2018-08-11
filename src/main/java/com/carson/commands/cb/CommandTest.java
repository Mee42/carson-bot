package com.carson.commands.cb;

import com.carson.Test;
import com.carson.commandManagers.Command;
import com.carson.commandManagers.Register;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandTest extends Command {
    public CommandTest(IDiscordClient c) {
        super(c);
    }

    @Override
    public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        throw new AssertionError();
    }

    @Override
    public boolean test(MessageReceivedEvent event) {
        if(!hasPermission(getWantedPermissionLevel(), Register.getPermissionLevel(event)))return false;
        return event.getMessage().getContent().equals("cb-runtest");
    }

    @Override
    public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        //processing not needed
        throw new AssertionError();
    }

    @Override
    public void run(MessageReceivedEvent event) {
        Test.runX();
    }

    @Override
    public String getCommandId() {
        return "cbtest";
    }

    @Override
    public PermissionLevel getWantedPermissionLevel() {
        return PermissionLevel.BOT_ADMIN;
    }
}
