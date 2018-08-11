package com.carson.commands.main;

import com.carson.classes.BTC;
import com.carson.commandManagers.Command;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.io.IOException;

public class CommandBTC extends Command {
    public CommandBTC(IDiscordClient c) {
        super(c);
    }

    @Override
    public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        return content.toLowerCase().equals("btc");
    }

    @Override
    public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        EmbedBuilder b = new EmbedBuilder();
        b.withTitle("BTC price:");
        b.withUrl("https://www.coindesk.com/price/");

        try {
            b.withDescription(String.valueOf(new BTC().downloadPrice(client)));
            RequestBuffer.request(()->{
                event.getChannel().sendMessage(b.build());
            });
            return;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendMessage(event, "looks like there was an error. <@293853365891235841> :eyes:");

    }

    @Override
    public String getCommandId() {
        return "btc";
    }
}
