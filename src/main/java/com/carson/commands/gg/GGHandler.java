package com.carson.commands.gg;



import com.carson.classes.BTC;
import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import com.carson.dataObject.DataGetter;
import com.carson.dataObject.GuildDataOrginizer;
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

public class GGHandler extends Command implements ICommand{
    public static final String GG = " <:gg:467728709139562497> ";
//    public static final String GG = " :squid: ";
    public static final String STAR = " :squid: ";

    public static double last = -1;

	public GGHandler(IDiscordClient c) {
		super(c);
	}
	

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getGuild().getLongID() == 462681259370610689L && event.getMessage().getContent().toLowerCase().startsWith("gg~");
	}

	@Override
	public void run(MessageReceivedEvent event) {
        GuildDataOrginizer data = DataGetter.getInstance();
        UserGG user = data.getUser(event.getAuthor());
        switch (event.getMessage().getContent().toLowerCase()) {
            case "gg~money":
//                sendEmbed( event , "you have " + user.getMoney() + GG);
                double price = -1;
                try {
                    price = new BTC().downloadPrice();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendEmbed(event, "your money", user.toString(price));
                break;
            case "gg~work":
                int amount = user.getWork();
                user.increaseMoney(amount);
//                sendMessage(event, "you got " + amount + GG + " and you now have " + user.getMoney() + GG);
                sendEmbed(event, "you got " + condense(amount), "current balance:" + condense(user.getMoney()));
                break;
            case "gg~all":
                sendAll(data,event);
                break;
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
                data.getBank().changeMoney(cost);
                sendEmbed(event,"your new education level:" + user.getEducationLevel(),"cost of next level:" + condense(user.getCost()));
                break;
            case "gg~bank":
                sendEmbed(event,"BANK:" + condense(data.getBank().getMoney()),"_ _");
                return;
            default:
                if (event.getMessage().getContent().toLowerCase().startsWith("gg~slot")) {
                    slot(event, user, data);
                } else if (event.getMessage().getContent().toLowerCase().startsWith("gg~mod")) {
                    mod(event, user, data);
                }else if(event.getMessage().getContent().toLowerCase().startsWith("gg~roulette")){
                    new Roulette(event,user,data);
                }else if(event.getMessage().getContent().toLowerCase().startsWith("gg~get_loan")){
                    loan(event,user,data);
                }else if(event.getMessage().getContent().toLowerCase().startsWith("gg~pay_loan")){
                    payLoan(event,user,data);
                }//else if (event.getMessage().getContent().toLowerCase().startsWith("gg~pay")) {
                else if(event.getMessage().getContent().toLowerCase().startsWith("gg~btc")){
                    btc(event,user,data);
                }
//                    pay(event, user, data);
//                }
                break;

        }
        cleanUsers(data);
        data.privateSterilize();


    }

    private void payLoan(MessageReceivedEvent event, UserGG user, GuildDataOrginizer data) {
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
        data.getBank().changeMoney(amount);
        if(user.getDebt() == 0){
            sendEmbed(event,"you paid back your entire loan!","your balance:" + condense(user.getMoney()));
        }else {
            sendEmbed(event, "you paid back " + condense(user.getMoney()), "you have " + condense(user.getDebt()) + " debt left to pay off");
        }
    }

    private void loan(MessageReceivedEvent event, UserGG user, GuildDataOrginizer data) {
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
            data.getBank().withdrawl(amount);
            user.loan(amount, interest);
            sendEmbed(event, "you got a loan of " + condense(amount), "your interest:" + user.getInterest() * 100 + "%");
        } catch (Bank.OutOfMoneyException e) {
            sendEmbed(event, "the bank doesn't have enough money.",data.getBank().outOfMoneyMessage());
        }
    }

    public static void sendAll(GuildDataOrginizer data, MessageReceivedEvent event) {
//                String send = "";
        EmbedBuilder b = new EmbedBuilder();
        List<UserGG> users = data.getUserGGs();
        GGHandler.sort(users);
        b.appendField("BANK:" + condense(data.getBank().getMoney()),"_ _",false);
        double worth = -1;
        try{
            worth = new BTC().downloadPrice(event.getClient());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (UserGG u : users) {
//                      send+=      "`" +  u.getId() + "`  " + client.getUserByID(u.getId()).getName() + " : " + u.getMoney() + GG   + "\n";
            b.appendField(event.getClient().getUserByID(u.getId()).getName(),u.toString(worth), false);
        }
//                sendEmbed(event, send);
        RequestBuffer.request(() -> {
            event.getChannel().sendMessage(b.build());
        });
    }


    private void mod(MessageReceivedEvent event, UserGG user, GuildDataOrginizer data) {
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
            data.getBank().setMoney(amount);
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
                    "         \\\\--gotten        (applys to both gotten and invested)\n" +
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
            if (Arrays.asList(args).contains("add")) {
                for (IUser u : mentioned) {
                    data.getUser(u).increaseMoney(amount);
                }
            } else if (Arrays.asList(args).contains("set")) {
                for (IUser u : mentioned) {
                    data.getUser(u).setMoney(amount);
                }
            } else if (Arrays.asList(args).contains("edu")) {
                for (IUser u : mentioned) {
                    data.getUser(u).setEducation(amount);
                }
            } else if (Arrays.asList(args).contains("int")) {
                for (IUser u : mentioned) {
                    data.getUser(u).setInterest(amount);
                }
            } else if (Arrays.asList(args).contains("debt")) {
                for (IUser u : mentioned) {
                    data.getUser(u).setDebt(amount);
                }
            } else if (Arrays.asList(args).contains("btc")) {
                for (IUser u : mentioned) {
                    data.getUser(u).setCoins(amount);
                }
            }else if (Arrays.asList(args).contains("gotten")) {
                for (IUser u : mentioned) {
                    data.getUser(u).setInvested(amount);
                    data.getUser(u).setGotten(amount);
                }
            }else if (Arrays.asList(args).contains("reset")) {
                for (IUser u : mentioned) {
                    data.getUser(u).reset();
                }
            }
        }

        sendMessage(event, "done");

    }

    private void cleanUsers(GuildDataOrginizer data) {
	    List<UserGG> hasZero = new ArrayList<>();
	    for(UserGG u : data.getUserGGs()){
	        if(u.getMoney() == 0 && u.getDebt() == 0 && u.getEducationLevel() == 0 ){
	            hasZero.add(u);
            }
        }
       data.getUserGGs().removeAll(hasZero);
    }

    private int getAmount(String[] args) throws NumberFormatException{
        for(String str : args){

            try{
                return Integer.parseInt(str);
            }catch(NumberFormatException e){
//                throw new NumberFormatException();
            }

        }
        throw new NumberFormatException();
    }


    private void slot(MessageReceivedEvent event, UserGG user, GuildDataOrginizer data) {
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
        int star3 = 15_000;
        int star2 = 500;
        int match2 = 20;
        int match3 = 500;
        int match2StarBonus = 500;
        int
        if(data.getBank().getMoney() < (star3 * amount)) {
            sendEmbed(event, "the bank doesn't have enough money. max bet: " + condense(data.getBank().getMoney() / 15_000), data.getBank().outOfMoneyMessage());
            return;
        }
        //:regional_indicator_a:
        String[] letters = new String[]{
                ":regional_indicator_a:",
                ":regional_indicator_b:",
                ":regional_indicator_c:",
                ":regional_indicator_d:",
                ":regional_indicator_e:",
                ":regional_indicator_f:",
                ":regional_indicator_g:",
                ":regional_indicator_h:",
                ":regional_indicator_i:",
                ":regional_indicator_j:",
                ":regional_indicator_k:",
                ":regional_indicator_l:",
                ":regional_indicator_m:",
                ":regional_indicator_n:",
                ":regional_indicator_o:",
                ":regional_indicator_p:",
                ":regional_indicator_q:",
                ":regional_indicator_r:",
                ":regional_indicator_s:",
                ":regional_indicator_t:",
                ":regional_indicator_u:",
                ":regional_indicator_v:",
                ":regional_indicator_w:",
                ":regional_indicator_x:",
                ":regional_indicator_y:",
                ":regional_indicator_z:",
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
        Bank bank = data.getBank();
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

    private void btc(MessageReceivedEvent event, UserGG user, GuildDataOrginizer data){
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
	        return;
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
//pay script \/

//    private void pay(MessageReceivedEvent event, UserGG user, GuildDataOrginizer data){//disabled for now - no point /shrug
//        String[] args = event.getMessage().getContent().toLowerCase().split(" ");
//        if(args.length == 1){
//            sendMessage(event, "you need more args");
//            return;
//        }
//        int amount = getAmount(args);
//        if(amount == -1){
//            sendEmbed(event, "error","you need an amount to pay");
//            return;
//        }
//        if(amount < 1){
//            sendEmbed(event, "error","you can not pay that");
//            return;
//        }
//
//        List<IUser> mentioned = event.getMessage().getMentions();
//        if(mentioned.size() == 0){
//            sendEmbed(event,"error","you need to mention someone");
//            return;
//        }
//
//
//        if(amount * mentioned.size() > user.getMoney()){
//            sendEmbed(event,"error:", "you do not have enough money to pay " + mentioned.size() + " people " + amount + GG + "\n" + "you have " + user.getMoney() + GG);
//            return;
//        }
//        user.setMoney(user.getMoney() - (amount * mentioned.size()));
//        for(IUser recipient : mentioned){
//            if(recipient.isBot()){
//                sendMessage(event, "you can not pay the bot. You are losing that money <:thonk:440249411574956032>");
//            }
//            data.getUser(recipient).increaseMoney(amount);
//        }
//        sendEmbed(event,"Worked!","your balance:" + user.getMoney());
//
//    }


	@Override
	public String getName() {
		return "GG handle";
	}

	@Override
	public String getDisciption() {
		return null;
	}
	

    private static List<UserGG> sort(List<UserGG> users){
        for (int i = 0; i < users.size() - 1; i++){
            int index = i;
            for (int j = i + 1; j < users.size(); j++) {
                if (users.get(j).getMoney() > users.get(index).getMoney())
                    index = j;
                }
            UserGG temp = users.get(index);
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
