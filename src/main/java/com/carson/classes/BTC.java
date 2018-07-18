package com.carson.classes;

import com.google.gson.GsonBuilder;

import java.io.*;

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
        executeCommands(new String[]{"curl https://api.coindesk.com/v1/bpi/currentprice.json > data"});
        Data d = getData(new File("data"));
        new File("data").delete();
        return d;
    }

    public double downloadPrice() throws IOException, InterruptedException {
        return download().bpi.USD.rate_float;
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
            pb.inheritIO();
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

}
