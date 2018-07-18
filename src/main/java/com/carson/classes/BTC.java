package com.carson.classes;

import com.carson.commands.gg.GGHandler;
import com.google.gson.GsonBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.io.*;
import java.text.DecimalFormat;
import java.util.UUID;

public class BTC {




    /**
     *
     * @param f file to read json from
     * @return the data object of that json
     * @throws IOException
     */
    public Data getData(File f) throws IOException {
        String str = "";
        BufferedReader r = new BufferedReader(new FileReader(f));
        String line = r.readLine();
        while(line != null){
            str+=line;
            line = r.readLine();
        }
//        System.out.println(str);
        Data s = new GsonBuilder().create().fromJson(str,Data.class);
        return s;
    }

    /**
     *
     * @param f file to read json from
     * @return double value of price in USD
     * @throws IOException
     */
    public double getPrice(File f) throws IOException {
        return getData(f).bpi.USD.rate_float;
    }

    /**
     *
     * @return an up-to-date data object
     * @throws IOException
     * @throws InterruptedException
     */
    public Data download() throws IOException, InterruptedException {
        //https://api.coindesk.com/v1/bpi/currentprice.json
        String random = UUID.randomUUID().toString();
        executeCommands(new String[]{"curl https://api.coindesk.com/v1/bpi/currentprice.json > data" + random});
        Data d = getData(new File("data" + random));
        new File("data" + random).delete();
        return d;
    }

    public double downloadPrice() throws IOException, InterruptedException {
        return download().bpi.USD.rate_float;
    }


    public double downloadPrice(IDiscordClient c, boolean printIfDifferent) throws IOException, InterruptedException {
        double d = downloadPrice();
        String str = "0 :black_circle:";
        if(d == GGHandler.last) {
            return -1;
        }
        if(GGHandler.last == -1){
            GGHandler.last = d;
            return -1;
        }
        double change = d - GGHandler.last;
        if (change > 0) {
            str = "+" + round(change) + " <:upp:469230980822073344>";
        } else if (change < 0) {
            str = "-" + round(java.lang.Math.abs(change)) + " <:down:469230864979591175>";
        }


        final String strS = str;
        RequestBuffer.request(()->{
            c.getChannelByID(469151223627644928L).sendMessage(new EmbedBuilder()
                    .withTitle("BTC PRICE")
//                    .withDesc(d + "" )
                    .appendField(d + "",strS,false)
                    .build());
        });
        GGHandler.last = d;
        return d;
    }

    public double downloadPrice(IDiscordClient c) throws IOException,InterruptedException{
        double d = downloadPrice();
        String str = "0 :black_circle:";
        if(d != GGHandler.last){
            if(GGHandler.last == -1){
            }else {
                double change = d - GGHandler.last;
                if (change > 0) {
                    str = "+" + round(change) + " <:upp:469230980822073344>";
                } else if (change < 0) {
                    str = "-" + round(java.lang.Math.abs(change)) + " <:down:469230864979591175>";
                }
            }
        }
        final String strS = str;
        RequestBuffer.request(()->{
           c.getChannelByID(469151223627644928L).sendMessage(new EmbedBuilder()
                   .withTitle("BTC PRICE")
//                    .withDesc(d + "" )
                   .appendField(d + "",strS,false)
                   .build());
        });
        GGHandler.last = d;
        return d;
    }

    class Data {
        StringS time;
        String disclaimer;
        String chartName;
        All bpi;
        class Type{
            String code;
            String symbol;
            String rate;
            double rate_float;
        }
        class StringS{
            String updated;
            String updatedISO;
            String updateduk;
        }
        class All{
            Type USD;
            Type GBP;
            Type EUR;
        }
    }


    private void executeCommands(String[] commands) throws IOException, InterruptedException {

        File tempScript = createTempScript(commands);

        try {
            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
//            pb.inheritIO();

            Process process = pb.start();
            process.waitFor();
        } finally {
            tempScript.delete();
        }
    }

    private File createTempScript(String[] commands) throws IOException {
        File tempScript = File.createTempFile("script", null);

        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
                tempScript));
        PrintWriter printWriter = new PrintWriter(streamWriter);

        printWriter.println("#!/bin/bash");
        for(String s : commands){
           printWriter.println(s);
        }

        printWriter.close();

        return tempScript;
    }

    private static String round(double val) {
        DecimalFormat df2 = new DecimalFormat("######.###");
        return df2.format(val);
    }


}
