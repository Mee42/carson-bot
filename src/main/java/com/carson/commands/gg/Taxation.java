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

    public static final double TAX_AMOUNT = 0.9;

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
//        Executors.newScheduledThreadPool(1).schedule(runner,timeTillHourMinutes(),TimeUnit.MINUTES);//backup plan

//        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(runner,timeTillHourMinutes(),60,TimeUnit.MINUTES);
    }

    public static long timeTillHourMinutes(){
        Calendar rightNow = Calendar.getInstance();
        long hour = rightNow.get(Calendar.MINUTE);
        return (60 - hour);
    }




    public static void Tax(IDiscordClient client){
//        final Runnable runner = new Runnable() {
//            private IDiscordClient client;
//            public Runnable submit(IDiscordClient client) {
//                this.client = client;
//                return this;
//            }
//            public void run() {
//                Taxation.Tax(client);
//            }
//        }.submit(client);
//        Executors.newScheduledThreadPool(1).schedule(runner,timeTillHourMinutes(),TimeUnit.MINUTES); //backup plan


        Messanger m = new Messanger();
        GuildDataOrginizer data = DataGetter.getInstance();
        List<UserGG> users = data.getUserGGs();
        String print = "***Tax was taken from your accounts***";//. here are the results:***\n";
        for(UserGG user : users){
//            print+=client.getUserByID(user.getId()).getName() + " had " + user.getMoney() + GGHandler.GG + " but now has ";
            user.midnight();
//            print+=user.getMoney() + GGHandler.GG + "\n";
        }
        m.sendMessage(client.getChannelByID(468188465662656513L),print);
    }

}