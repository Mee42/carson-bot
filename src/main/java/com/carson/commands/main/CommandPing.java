package com.carson.commands.main;

import com.carson.classes.Messenger;
import com.carson.commandManagers.Command;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

public class CommandPing extends Command {
    public CommandPing(IDiscordClient c) {
        super(c);
    }

    @Override
    public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        return content.equals("ping");
    }

    @Override
    public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        long time = System.nanoTime();
        IMessage message = new Messenger().sendMessageAndGet(event.getChannel(), "pinging :ping_pong:");
        long ping = System.nanoTime() - time;
        message.edit("pinged :ping_pong:   ping:  " + (ping/1000000) + "  ms");
    }

    @Override
    public String getCommandId() {
        return "ping";
    }
}
