package com.carson;

import com.carson.classes.FileIO;
import com.carson.main.BotUtils;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.RequestBuffer;

import java.io.File;

public class MainGoodbye {
    public static void main(String[] args) throws InterruptedException {
        String token = "";
        try {
            token = FileIO.use(new File("/home/carson/java/jars/key")).readList().get(0);
        }catch(Exception e) {
            System.out.println("threw a " + e.getClass().getName() + " when trying to read from key");
            e.printStackTrace();
            return;
        }
        IDiscordClient client = BotUtils.getBuiltDiscordClient(token);


        client.login();

        String invite = "https://tinyurl.com/CARSON-XKCD";

        String message = "Hey there, short message for you all\n" +
                "As you know, <@425376884843347980> is a big project maintained by <@293853365891235841>. There were many fatal errors that make development annoying, the most of which made writing help menu entries easily" +
                " near imposable. It's no longer worth it to maintain such a code base just to support my crappy command code that I'm to lazy to rewrite\n" +
                "But damn, its been a journey. 6000 lines of actually code, a thousond lines of comments, 2k blank lines, days worth of work all went into this bot\n" +
                "And now it's being shutdown\n" +
                "\n" +
                "There is just no fathomable idea of why I should keep this running\n" +
                "```md\n#However!\n```\n" +
                "This isn't the end of bots. XKCD-Bot is coming, a bot that has the same functionality as the old ~xkcd command\n" +
                "You can invite it here:<" + invite + ">\n" +
                "Thanks for being a part of Carson-Bot's growth\n" +
                "   - Carson-Bot, last message";
        Thread.sleep(1000);
//        for(long id : new long[]{
//                450658157019267092L,
//                469695997199515659L,
//                469995538624937994L,
//                447526186936041482L,
//                400847229809590272L,
//                471049957630279683L
//        }){
//            RequestBuffer.request(() -> client.getChannelByID(id).sendMessage(message));
//        }

        for(IGuild guild : client.getGuilds()){
//            RequestBuffer.request(() -> guild.leave());
            System.out.println(guild.getName());
        }
    }
}
