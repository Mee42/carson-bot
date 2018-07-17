package com.carson.commands.main;

import com.carson.classes.Googler;
import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandMuseScore extends Command implements ICommand {
    public CommandMuseScore(IDiscordClient c) {
        super(c);
    }

    @Override
    public boolean test(MessageReceivedEvent event) {
        return event.getMessage().getContent().toLowerCase().startsWith("~musescore");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        String text = event.getMessage().getContent();
        if(text.split(" ",2).length != 2){
            sendMessage(event,"uh oh - I couldn't process that");
            return;
        }
        Googler googler = new Googler();

        String link = googler.Google("site:musescore.com " +text.split( " ",2)[1]);

//        EmbedBuilder b = new EmbedBuilder()
////                .withTitle("click here")
////                .withUrl(link)
//                .withDesc(link);
//
//        RequestBuffer.request(()->{
//            event.getChannel().sendMessage(b.build());
//        });
        sendMessage(event,"got it:" + link);
    }

    @Override
    public String getName() {
        return "~musescore *keywords*";
    }

    @Override
    public String getDisciption() {
        return "search musescore for music!";
    }
}