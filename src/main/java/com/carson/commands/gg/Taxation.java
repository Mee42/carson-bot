package com.carson.commands.gg;

import com.carson.classes.Messenger;
import com.carson.dataObject.DBHandler;
import sx.blah.discord.api.IDiscordClient;

import java.util.Calendar;
import java.util.List;

public class Taxation {

    public static final double TAX_AMOUNT = 0.9;

    public static void start(IDiscordClient client){
        if(true)return;

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
//            public void runX() {
//                Taxation.Tax(client);
//            }
//        }.submit(client);
//        Executors.newScheduledThreadPool(1).schedule(runner,timeTillHourMinutes(),TimeUnit.MINUTES); //backup plan


        Messenger m = new Messenger();
        List<DBHandler.UserGG> users = DBHandler.get().getUserGG();
        String print = "***Tax was taken from your accounts***";//. here are the results:***\n";
        for(DBHandler.UserGG user : users){
            user.setDebt((int) (user.getDebt() * user.getInterest() + user.getDebt()));
            DBHandler.get().update(user);
        }
        m.sendMessage(client.getChannelByID(468188465662656513L),print);
    }

}