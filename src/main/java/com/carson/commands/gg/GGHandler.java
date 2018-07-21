package com.carson.commands.gg;



import com.carson.classes.BTC;
import com.carson.classes.DB;
import com.carson.commandManagers.Command;

import com.carson.dataObject.DBHandler;
import com.carson.dataObject.DataGetter;
import com.mongodb.client.model.Filters;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;


import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GGHandler extends Command {
    public static final String GG = " <:gg:467728709139562497> ";
//    public static final String GG = " :squid: ";
    private static final String STAR = " :squid: ";

    public static double last = -1;

	public GGHandler(IDiscordClient c) {
		super(c);
	}

    @Override
    public boolean test(MessageReceivedEvent event, String content, String[] args) {
	    if(event.getGuild().getLongID() != 462681259370610689L)return false;
        return event.getMessage().getContent().toLowerCase().startsWith("gg~");
	}

    @Override
    public void run(MessageReceivedEvent event, String content, String[] args) {

        DBHandler db = DBHandler.get();
        Bank bank = new Bank(db.getBank().getMoney());
        UserGG user;
        try {
            user = UserGG.from(db.toUserGG(DB.getById(event.getAuthor().getLongID(), db.getGGDB())));
        }catch (NullPointerException e){
            user = new UserGG(event.getAuthor().getLongID());
        }
        switch (event.getMessage().getContent().toLowerCase()) {
            case "gg~money":
//                sendEmbed( event , "you have " + user.getMoney() + GG);
                double price = -1;
                try {
                    price = new BTC().downloadPrice();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                sendEmbed(event, "your money", user.toString(price));
                return;
            case "gg~work":
                int amount = user.getWork();
                user.increaseMoney(amount);
//                sendMessage(event, "you got " + amount + GG + " and you now have " + user.getMoney() + GG);
                sendEmbed(event, "you got " + condense(amount), "current balance:" + condense(user.getMoney()));
                break;
            case "gg~all":
                sendAll(event);
                return;//return if nothing was changed
            case "gg~edu":
                sendEmbed(event, "your education level is " +user.getEducationLevel(),"cost to next level:" + condense(user.getCost()));
                break;
            case "gg~upgrade":
                int cost = user.getCost();
                if(user.getMoney() < cost){
                    sendEmbed(event,"error","you do not have enough money. you have " + condense(user.getMoney()));
                    break;
                }
                user.increaseMoney(-1 * cost);
                user.increaseEducationLevel(1);
                bank.changeMoney(cost);
                sendEmbed(event,"your new education level:" + user.getEducationLevel(),"cost of next level:" + condense(user.getCost()));
                break;
            case "gg~bank":
                sendEmbed(event,"BANK:" + condense(bank.getMoney()),"_ _");
                return;
            default:
                if (event.getMessage().getContent().toLowerCase().startsWith("gg~slot")) {
                    slot(event, user,bank);
                } else if (event.getMessage().getContent().toLowerCase().startsWith("gg~mod")) {
                    mod(event,bank);
                    return;
                }else if(event.getMessage().getContent().toLowerCase().startsWith("gg~roulette")){
                    new Roulette(event,user,bank);
                }else if(event.getMessage().getContent().toLowerCase().startsWith("gg~get_loan")){
                    loan(event,user,bank);
                }else if(event.getMessage().getContent().toLowerCase().startsWith("gg~pay_loan")){
                    payLoan(event,user,bank);
                }//else if (event.getMessage().getContent().toLowerCase().startsWith("gg~pay")) {
                else if(event.getMessage().getContent().toLowerCase().startsWith("gg~btc")){
                    btc(event,user,bank);
                }

        }//end switch
        db.update(DBHandler.get().new Bank(bank.getMoney()));
        db.update(user.to());
    }

    private void payLoan(MessageReceivedEvent event, UserGG user, Bank bank) {
        int amount = getAmount(event.getMessage().getContent().split(" "));
        if(amount == -1){
            sendEmbed(event,"error","invalid amount");
            return;
        }
        if(amount < 1){
            sendEmbed(event,"error","you can not play back a negative amount");
            return;
        }
        if(amount > user.getDebt()){
            sendEmbed(event,"error","you do not have that much debt" +"\nyou have " + condense(user.getDebt()) + " of debt");
            return;
        }
        if(amount > user.getMoney()){
            sendEmbed(event,"error","you do not have enough money\nyour balance:" + condense(user.getMoney()));
            return;
        }

        user.payBack(amount);
        bank.changeMoney(amount);
        if(user.getDebt() == 0){
            sendEmbed(event,"you paid back your entire loan!","your balance:" + condense(user.getMoney()));
        }else {
            sendEmbed(event, "you paid back " + condense(user.getMoney()), "you have " + condense(user.getDebt()) + " debt left to pay off");
        }
    }

    private void loan(MessageReceivedEvent event, UserGG user, Bank bank) {
	    int amount = getAmount(event.getMessage().getContent().split(" "));
	    if(amount == -1){
	        sendEmbed(event,"error","invalid amount");
	        return;
        }
        if(amount < 1){
	        sendEmbed(event,"error","you can not get loaned a negative amount");
	        return;
        }
        double interest = 0.05;
        if(amount > 1_000)interest = 0.1;
        if(amount > 1_000_000)interest = 0.25;
        if(amount > 1_000_000_000)interest = 0.5;
        try {
            bank.withdrawl(amount);
            user.loan(amount, interest);
            sendEmbed(event, "you got a loan of " + condense(amount), "your interest:" + user.getInterest() * 100 + "%");
        } catch (Bank.OutOfMoneyException e) {
            sendEmbed(event, "the bank doesn't have enough money.",bank.outOfMoneyMessage());
        }
    }

    private static void sendAll(MessageReceivedEvent event) {
//                String send = "";
        EmbedBuilder b = new EmbedBuilder();
        List<DBHandler.UserGG> users = DBHandler.get().getUserGG();
        System.out.println(users.size() + " users in db");

        GGHandler.sort(users);
        b.appendField("BANK:" + condense(DBHandler.get().getBank().getMoney()),"_ _",false);
        double worth = -1;
        try{
            worth = new BTC().downloadPrice(event.getClient());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        for (DBHandler.UserGG user : users) {
//                      send+=      "`" +  u.getId() + "`  " + client.getUserByID(u.getId()).getName() + " : " + u.getMoney() + GG   + "\n";
            UserGG u = UserGG.from(user);
            if(u.getMoney() == 0 && u.getEducationLevel() == 0 && u.getDebt() == 0 & u.getInterest() == 0 & u.getCoins() == 0 && u.getInvested() == 0 && u.getGotten() == 0){
                System.out.println("skipping " + u.getId());
                continue;//skip
            }
            b.appendField(event.getClient().getUserByID(u.getId()).getName(),u.toString(worth), false);
        }
//                sendEmbed(event, send);
        RequestBuffer.request(() -> {
            event.getChannel().sendMessage(b.build());
        });
    }


    private void mod(MessageReceivedEvent event,Bank bank) {
	    if(!event.getAuthor().getRolesForGuild(event.getGuild()).contains(event.getGuild().getRolesByName("MOD").get(0))){
            sendMessage(event,"you need the @MOD role to use this command");
            return;
        }
        String[] args = event.getMessage().getContent().toLowerCase().split(" ");
        if(args.length == 1){
            sendMessage(event, "you need more args");
            return;
        }
        int amount;
        try {
            amount = getAmount(args);
        }catch(NumberFormatException e){
            sendEmbed(event,"error","couldn't parse amount");
            return;
        }
        if(Arrays.asList(args).contains("bank")) {
            bank.setMoney(amount);
        }else if(Arrays.asList(args).contains("help")){
            String help = "**gg~mod**\n" +
                    "needs number\n" +
                    "  \\\\----needs mention\n" +
                    "         \\\\--add\n" +
                    "         \\\\--set\n" +
                    "         \\\\--edu\n" +
                    "         \\\\--int\n" +
                    "         \\\\--debt\n" +
                    "         \\\\--btc\n" +
                    "         \\\\--reset\n" +
                    " \\\\---does not need mention\n" +
                    "      \\\\--bank\n" +
                    "      \\\\--help\n" +
                    "";
            sendMessage(event,help);
        }else {//if needs a mention
            List<IUser> mentioned = event.getMessage().getMentions();
            if (mentioned.size() == 0) {
                sendEmbed(event, "error", "you need to mention someone");
                return;
            }
            DBHandler db = DBHandler.get();
            List<DBHandler.UserGG> users = new ArrayList<>();
            for (IUser u : mentioned) {
                try {
                    users.add(db.toUserGG(DB.getById(u.getLongID(), db.getGGDB())));
                    System.out.println("added " + u.getName());
                }catch(NullPointerException e){
                    users.add(db.new UserGG(u.getLongID()));
                }
            }

            if (Arrays.asList(args).contains("add")) {
                for (DBHandler.UserGG u : users) {
                    u.setMoney(u.getMoney() + amount);
                    db.update(u);
                }
            } else if (Arrays.asList(args).contains("set")) {
                for (DBHandler.UserGG u : users) {
                    u.setMoney(amount);
                    db.update(u);
                    System.out.println("updated to" + u.getMoney());
                }
            } else if (Arrays.asList(args).contains("edu")) {
                for (DBHandler.UserGG u : users) {
                    u.setEduLevel(amount);
                    db.update(u);
                }
            } else if (Arrays.asList(args).contains("int")) {
                for (DBHandler.UserGG u : users) {
                    u.setInterest(amount);
                    db.update(u);
                }
            } else if (Arrays.asList(args).contains("debt")) {
                for (DBHandler.UserGG u : users) {
                    u.setDebt(amount);
                    db.update(u);
                }

            } else if (Arrays.asList(args).contains("btc")) {
                for (DBHandler.UserGG u : users) {
                    u.setCoins(amount);
                    db.update(u);
                }

            }else if (Arrays.asList(args).contains("reset")) {
                for (DBHandler.UserGG u : users) {
                    UserGG resettable = UserGG.from(u);
                    resettable.reset();
                    db.update(resettable.to());
                }

            }
        }

        sendMessage(event, "done");

    }



    private int getAmount(String[] args) throws NumberFormatException{

        for(String str : args){
            str = str.replace(",","").replace("_","");
            try{
                return Integer.parseInt(str);
            }catch(NumberFormatException e){
//                throw new NumberFormatException();
            }

        }
        throw new NumberFormatException();
    }


    private void slot(MessageReceivedEvent event, UserGG user, Bank bank) {
        int amount = -1;
        for(String str : event.getMessage().getContent().split(" ")){
            int temp ;
            try{
                temp = Integer.parseInt(str);
            }catch(NumberFormatException e){
                temp = -1;
            }
            if(temp != -1){
                amount = temp;
                break;
            }
        }
        if(amount == -1){
            sendEmbed(event, "error","you need an amount to pay");
            return;
        }
        if(amount > user.getMoney()){
            sendEmbed(event, "error:","you do not have enough money");
            return;
        }
        int star3 = 1_000;
        int star2 = 75;
        int match2 = 10;
        int match3 = 75;
        int match2StarBonus = 100;

        if(bank.getMoney() < (star3 * amount)) {
            sendEmbed(event, "the bank doesn't have enough money. max bet: " + condense(bank.getMoney() / star3), bank.outOfMoneyMessage());
            return;
        }
        //:regional_indicator_a:
        String[] letters = new String[]{
//                ":regional_indicator_a:",
//                ":regional_indicator_b:",
//                ":regional_indicator_c:",
//                ":regional_indicator_d:",
                ":regional_indicator_e:",
//                ":regional_indicator_f:",
//                ":regional_indicator_g:",
//                ":regional_indicator_h:",
//                ":regional_indicator_i:",
                ":regional_indicator_j:",
//                ":regional_indicator_k:",
//                ":regional_indicator_l:",
//                ":regional_indicator_m:",
//                ":regional_indicator_n:",
                ":regional_indicator_o:",
//                ":regional_indicator_p:",
//                ":regional_indicator_q:",
//                ":regional_indicator_r:",
                ":regional_indicator_s:",
//                ":regional_indicator_t:",
//                ":regional_indicator_u:",
//                ":regional_indicator_v:",
                ":regional_indicator_w:",
//                ":regional_indicator_x:",
//                ":regional_indicator_y:",
//                ":regional_indicator_z:",
                ":banana:",
                ":b:",
                ":flag_pl:",
                ":thinking:",
                STAR.trim()
        };

        int a = (int)(Math.random()*letters.length);
        int b = (int)(Math.random()*letters.length);
        int c = (int)(Math.random()*letters.length);
        sendEmbed(event, "you got",
                letters[a] + " " +
                letters[b] + " " +
                letters[c]
        );
        if(a == b && b == c && a == letters.length - 1){//n^3
            user.increaseMoney(-1 * amount);
            user.increaseMoney(amount * star3);
            bank.changeMoney(-1 * amount * star3);
            sendEmbed(event,"YOU WON THE SUPER BIG PRIZE " + condense(amount * star3) + "!!!!", "your balance:" + user.getMoney());
        }else if(a == b && b == c){
            user.increaseMoney(-1 * amount);
            //win grand prize
            user.increaseMoney(amount * match3);
            bank.changeMoney(-1 * amount * match3);

            sendEmbed(event,"YOU WON " + condense(amount * match3)+ "!!!!", "your balance:" + condense(user.getMoney()));
        }else if(a == b || b == c|| c == a){
            //win small prize
            int count = 0;
            if(a == letters.length - 1)count++;
            if(b == letters.length - 1)count++;
            if(c == letters.length - 1)count++;
            if(count == 0) {
                user.increaseMoney(-1 * amount);
                user.increaseMoney(amount * match2);
                bank.changeMoney(-1 * amount * match2);
                sendEmbed(event, "you won a small prize: " + condense(amount * match2)  + "!", "your balance:" + condense(user.getMoney()));
            }else if(count == 1){
                user.increaseMoney(-1 * amount);
                user.increaseMoney(amount * match2 + match2StarBonus);
                bank.changeMoney(-1 * amount * match2);
                bank.changeMoney(-1 * match2StarBonus);
                sendEmbed(event, "you won a small prize: " + condense(amount * match2) + ", as well as the bonus:" + match2StarBonus, "your balance:" + condense(user.getMoney()));
            }else if(count == 2){
                user.increaseMoney(-1 * amount);
                user.increaseMoney(amount * star2);
                bank.changeMoney(-1 * amount * star2);
                sendEmbed(event, "you won a " + STAR + " prize: " + condense(amount * star2) + "!!!! " + STAR + " " + STAR + " ", "your balance:" + condense(user.getMoney()));
            }
        }else{
            int count = 0;
            if(a == letters.length - 1)count++;
            if(b == letters.length - 1)count++;
            if(c == letters.length - 1)count++;
            if(count == 0) {
                user.increaseMoney(-1 * amount);
                bank.changeMoney(amount);
                sendEmbed(event, "you won nothing :*(", "your balance:" + condense(user.getMoney()));
            }else{//count == 1
                sendEmbed(event, "you didn't win, but you got a " + STAR + " so you keep your money","your balance:" + condense(user.getMoney()));
            }
        }
        

    }

    private void btc(MessageReceivedEvent event, UserGG user, Bank bank){
	    int amount;
	    try {
            amount = getAmount(event.getMessage().getContent().split(" "));
        }catch(NumberFormatException e){
            sendEmbed(event,"error","no amount specified");
            return;
	    }
	    if(amount == 0) {
            sendEmbed(event, "error", "you can not buy 0 btc :eyes:");
            return;
        }
        if(amount < 0){//wants to sell
	        amount = Math.abs(amount);
	        if(user.getCoins() < amount){
	            sendEmbed(event,"error","you do not have enough coins. you have " + user.getCoins() + "BTC");
	            return;
            }
	        try {
                int cost = (int) (new BTC().downloadPrice() * amount);
                user.setCoins(user.getCoins() - amount);
                user.increaseMoney(cost);
                user.setGotten(user.getGotten() + cost);
                sendEmbed(event, "transaction was a success! you have " + condense(user.getMoney()),"your coins:" + user.getCoins());
                return;
            }catch(IOException e){}catch (InterruptedException e) {}
            sendEmbed(event,"error","there was an error processing your transaction");
        }else{//wants to buy
            try {
                int cost = (int) (new BTC().downloadPrice() * amount);
                if(user.getMoney() < cost){
                    sendEmbed(event,"error","you do not have enough money to buy " + condense(cost) + " worth of BTC");
                    return;
                }
                user.increaseMoney(-1 * cost);
                user.setCoins(user.getCoins() + amount);
                user.setInvested(cost + user.getInvested());
                sendEmbed(event, "transaction was a success! you have " + condense(user.getMoney()),"your coins:" + user.getCoins());
                return;
            }catch(IOException e){}catch (InterruptedException e) {}
            sendEmbed(event,"error","there was an error processing your transaction");
        }

    }


	@Override
	public String getName() {
		return "GG handle";
	}

	@Override
	public String getDisciption() {
		return null;
	}
	

    private static List<DBHandler.UserGG> sort(List<DBHandler.UserGG> users){
        for (int i = 0; i < users.size() - 1; i++){
            int index = i;
            for (int j = i + 1; j < users.size(); j++) {
                if (users.get(j).getMoney() > users.get(index).getMoney())
                    index = j;
                }
            DBHandler.UserGG temp = users.get(index);
            users.set(index, users.get(i));
            users.set(i,temp);
        }
        return users;
    }

    private void sendEmbed(MessageReceivedEvent event, String str, String dis){
	    EmbedBuilder  b = new EmbedBuilder();
	    b.withColor(new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255)));
	    if(str.equals("error") || str.equals("error:")){
	        b.withColor(Color.red);
        }
	    b.appendField(str + "_ _",dis + "_ _",false);
        RequestBuffer.request(() -> {
            event.getChannel().sendMessage(b.build());
        });
    }



    public static String condense(int amount){
	    if(amount <= 999){
            return amount + GGHandler.GG;
        }else if(amount <= 999_999){
	        return ((int)(amount / 1_000d) + "") + "," + trailing((int)(amount % 1_000d)) + GG;
        }else  if(amount <= 999_999_999){
//	        return String.valueOf((double)((int)(amount / 1_000_000d)*100)/100d) + " MIL " + GGHandler.GG;
	        return roundTo2Decimals(amount / 1_000_000d)+ " MIL " + GGHandler.GG;
        }else {
	        return roundTo2Decimals(amount / 1_000_000_000d)+ " BIL " + GGHandler.GG;
        }
    }


    private static String roundTo2Decimals(double val) {
        DecimalFormat df2 = new DecimalFormat("###.##");
        return df2.format(val);
    }
    private static String trailing(int i){
	    String str = String.valueOf(i);
	    while(str.length() < 3){
	        str = "0" + str;
        }
        return str;
    }



}
