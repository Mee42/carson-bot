package com.carson.commands.gg;

import com.carson.classes.Messanger;
import com.carson.dataObject.DataGetter;
import com.carson.dataObject.GuildDataOrginizer;
import sx.blah.discord.api.IDiscordClient;

import java.time.Duration;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Taxation {

    public static final double TAX_AMOUNT = 0.8;

    public static void start(IDiscordClient client){
        final Runnable runner = new Runnable() {
            private IDiscordClient client;
            public Runnable submit(IDiscordClient client) {
                this.client = client;
                return this;
            }
            public void run() {
                Taxation.Tax(client);
            }
        }.submit(client);

        Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(runner, timeTillMidnight(), Duration.ofDays(1).toMillis(),
        TimeUnit.MILLISECONDS);


    }

    private static long timeTillMidnight(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long howMany = (c.getTimeInMillis()-System.currentTimeMillis());
        return howMany;
    }



    public static void Tax(IDiscordClient client){
        Messanger m = new Messanger();
        GuildDataOrginizer data = DataGetter.getInstance();
        List<UserGG> users = data.getUserGGs();
        String print = "***Tax was taken from your accounts. here are the results:***\n";
        for(UserGG user : users){
            print+=client.getUserByID(user.getId()).getName() + " had " + user.getMoney() + GGHandler.GG + " but now has ";
            user.setMoney((int)(user.getMoney()  * Taxation.TAX_AMOUNT));
            print+=user.getMoney() + GGHandler.GG + "\n";
        }
        m.sendMessage(client.getChannelByID(468188465662656513L),print);
    }

}