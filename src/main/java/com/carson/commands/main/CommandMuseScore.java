package com.carson.commands.main;

import com.carson.classes.Googler;
import com.carson.commandManagers.Command;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandMuseScore extends Command{
    public CommandMuseScore(IDiscordClient c) {
        super(c);
    }

    @Override
    public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        return content.startsWith("musescore");
    }

    @Override
    public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        if(content.split(" ",2).length != 2){//can't use agrs bc of i=2;
            sendMessage(event,"uh oh - I couldn't process that");
            return;
        }
        Googler googler = new Googler();

        String link = googler.Google("site:musescore.com " +content.split( " ",2)[1]);

//        EmbedBuilder b = new EmbedBuilder()
////                .withTitle("click here")
////                .withUrl(link)
//                .withDesc(link);
//
//        RequestBuffer.request(()->{
//            event.getChannel().sendMessage(b.build());
//        });//TODO work on
        sendMessage(event,"got it:" + link);
    }


    @Override
    public String getCommandId() {
        return "musescore";
    }
}
