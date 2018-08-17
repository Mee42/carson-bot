package com.carson.classes;

import com.carson.dataObject.DBHandler;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Collection;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimeManager extends Thread{

    private static long startTime;
    public TimeManager() {
        startTime = System.currentTimeMillis() / 1000;
        startDayCounter();
    }



    @Override
    public void run() {
        final MongoCollection<Document> logs = DBHandler.get().getDB("logs");
        long seconds = (System.currentTimeMillis()/1000) - TimeManager.startTime;
        Document doc = new Document().append("seconds" ,seconds);
        logs.insertOne(doc);
        System.out.println("time up:" + seconds + " seconds");



    }

    public static long getSecondsUp(){
        long seconds = 0;
        for(Document doc : DBHandler.get().getDB("logs").find()){
            if(doc.containsKey("seconds:")){
                DBHandler.get().getDB("logs").deleteOne(Filters.eq("_id",doc.get("_id")));
                continue;
            }
            if(doc.containsKey("seconds")) seconds+=(Long)doc.get("seconds");
        }
        return seconds + (System.currentTimeMillis()/1000) - TimeManager.startTime;
    }

    public static long secondsSinceTime(){
        //1534354140
        Instant start = Instant.ofEpochSecond(1534354140);//when I started logging
        Instant now = Instant.now();
        Duration duration = Duration.between(start,now);
        long seconds = duration.getSeconds();
        return seconds;
    }

    public static double getUptime(){
        double up = (getSecondsUp()* 1d) / (secondsSinceTime() * 1d);
        return up;
    }





    private static void startDayCounter() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Calendar currentCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        long now = currentCalendar.getTimeInMillis();
        int min = currentCalendar.get(Calendar.MINUTE);
        currentCalendar.set(Calendar.MINUTE, 10 * (min / 10 + 1));//https://stackoverflow.com/a/6835164
        currentCalendar.set(Calendar.SECOND, 0);
        currentCalendar.set(Calendar.MILLISECOND, 0);
        long passed = now - currentCalendar.getTimeInMillis();
        long minutes = passed / 1000 / 60;
        minutes = -1 * minutes;
        //millisUntilNextHour = (min*60*1000 + sec*1000 + millis + 299999)/300000*300000 - (min*60*1000 + sec*1000 + millis

        Runnable runnable = () -> {
            final MongoCollection<Document> logs = DBHandler.get().getDB("logs");
            Document document = new Document();
            document.append("year", getTodaysYear());
            document.append("day",getTodaysDay());
            logs.insertOne(document);
            System.out.println("logged uptime");
        };
        System.out.println("starting logger in "  + minutes + " minues");
        executor.scheduleAtFixedRate(runnable,minutes,10, TimeUnit.MINUTES);
    }

    private static int logsPerDay(){
        return 6 * 24;//6 an hour. approx
    }
    public static int getTodaysDay(){
        return LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC).getDayOfYear();
    }
    public static int getTodaysYear(){
        return LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC).getYear();
    }
    public static Day getDay(int day, int year){
        final FindIterable<Document> documents = DBHandler.get().getDB("logs").find(Filters.and(Filters.eq("year", year), Filters.eq("day", day)));
        final double uptime = (size(documents) * 1d) / (logsPerDay() * 1d);
        return new Day(uptime,day,year);
    }
    public static class Day{
        public final double uptime;
        public final int day;
        public final int year;

        public Day(double uptime, int day, int year) {
            this.uptime = uptime;
            this.day = day;
            this.year = year;
        }
    }

    public static int size(Iterable<?> it) {//https://stackoverflow.com/a/11414897
        if (it instanceof Collection)
            return ((Collection<?>)it).size();

        // else iterate

        int i = 0;
        for (Object obj : it) i++;
        return i;
    }

}
