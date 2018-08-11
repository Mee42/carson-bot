package com.carson.commandManagers;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandSupport extends Command {
    public CommandSupport(IDiscordClient c) {
        super(c);
    }

    @Override
    public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        return content.equals("support");
    }

    @Override
    public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        sendMessage(client.getOrCreatePMChannel(client.getUserByID(293853365891235841L)),"someone needs help! Their server:"+ event.getGuild().getExtendedInvites().get(0).toString());
        sendMessage(event.getChannel(), "dm me at <@293853365891235841>, or join my support server at discord.gg-slash-BxhRxHW ");
    }

    @Override
    public String getCommandId() {
        return "support";
    }
}
