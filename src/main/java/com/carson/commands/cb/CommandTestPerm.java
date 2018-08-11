package com.carson.commands.cb;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.Register;
import com.carson.commands.main.CommandBTC;
import com.carson.commands.main.lavaplayer.CommandSkip;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;

public class CommandTestPerm extends Command {
    public CommandTestPerm(IDiscordClient c) {
        super(c);
    }

    @Override
    public boolean test(MessageReceivedEvent event) {
        return event.getMessage().getContent().startsWith("cb-test");
    }

    @Override
    public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        throw new AssertionError();
    }

    @Override
    public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        Command.PermissionLevel level = Command.PermissionLevel.USER;
        if(args[1].equalsIgnoreCase("mod")){
            level = Command.PermissionLevel.MOD;
        }
        if(args[1].equalsIgnoreCase("bot_admin")){
            level = CommandSkip.PermissionLevel.BOT_ADMIN;
        }
        boolean works = new CommandBTC(client).hasPermission(level, Register.getPermissionLevel(event));//why commandbtc? this intrudeces a unwanted dependency and coudl be messed up. even already
        RequestBuffer.request(()->{
            event.getChannel().sendMessage(works?"permission granted":"permission denied");
        });
    }

    @Override
    public String getCommandId() {
        return "testperm";
    }
}
