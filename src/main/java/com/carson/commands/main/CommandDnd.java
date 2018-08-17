package com.carson.commands.main;

import com.carson.commandManagers.Command;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandDnd extends Command {
    public CommandDnd(IDiscordClient c) {
        super(c);
    }

    @Override
    public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        return content.startsWith("dnd");
    }

    @Override
    public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        if (args.length == 1) {
            sendMessage(event, "you need a dice");
            return;
        }
        int count;
        int die;
        if (args.length == 2) {
            count = 1;
            die = Integer.parseInt(args[1]);
        } else {
            count = Integer.parseInt(args[1]);
            die = Integer.parseInt(args[2]);
        }
        String str = "here " + ((count == 1) ? "is" : "are") + " your rolls:\n";
        for (int i = 0; i < count; i++) {
            str += rand(die) + "  ";
        }
        sendMessage(event, str);
    }

    @Override
    public String getCommandId() {
        return "dnd";
    }


    private int rand(int die) {
        return (int) (Math.random() * die + 1);
    }

}