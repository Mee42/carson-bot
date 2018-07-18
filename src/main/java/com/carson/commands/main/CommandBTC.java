package com.carson.commands.main;

import com.carson.classes.BTC;
import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.io.IOException;

public class CommandBTC extends Command implements ICommand {
    public CommandBTC(IDiscordClient c) {
        super(c);
    }

    @Override
    public boolean test(MessageReceivedEvent event) {
        return event.getMessage().getContent().toLowerCase().equals("~btc");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        EmbedBuilder b = new EmbedBuilder();
        b.withTitle("BTC price:");
        b.withUrl("https://www.coindesk.com/price/");

        try {
            b.withDescription(String.valueOf(new BTC().downloadPrice()));
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
    public String getName() {
        return "~btc";
    }

    @Override
    public String getDisciption() {
        return "get the current btc price from coindesk";
    }
}
