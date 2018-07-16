package com.carson.commands.gg;



import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import com.carson.dataObject.DataGetter;
import com.carson.dataObject.GuildDataOrginizer;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;


import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GGHandler extends Command implements ICommand{
    public static final String GG = " <:gg:467728709139562497> ";
//    public static final String GG = " :squid: ";
    public static final String STAR = " :squid: ";
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
                sendEmbed(event, "your money", user.getMoney() + GG);

                break;
            case "gg~work":
                int amount = user.getWork();
                user.increaseMoney(amount);
//                sendMessage(event, "you got " + amount + GG + " and you now have " + user.getMoney() + GG);
                sendEmbed(event, "you got " + amount + GG, "current balance:" + user.getMoney() + GG);

                break;
            case "gg~all":
//                String send = "";
                EmbedBuilder b = new EmbedBuilder();
                List<UserGG> users = data.getUserGGs();
                sort(users);
                for (UserGG u : users) {
//                      send+=      "`" +  u.getId() + "`  " + client.getUserByID(u.getId()).getName() + " : " + u.getMoney() + GG   + "\n";
                    b.appendField(client.getUserByID(u.getId()).getName(),// + " : " + u.getMoney() + GG,//title
//                            u.getId() + "",//discri
                            u.toString() + " " + u.getEducationLevel() + " edu level",
                            false);
                }
//                sendEmbed(event, send);
                RequestBuffer.request(() -> {
                    event.getChannel().sendMessage(b.build());
                });
                break;
            case "gg~edu":
                sendEmbed(event, "your education level is " +user.getEducationLevel(),"cost to next level:" + user.getCost());
                break;
            case "gg~upgrade":
                int cost = user.getCost();
                if(user.getMoney() < cost){
                    sendEmbed(event,"error","you do not have enough money. you have " + user.toString());
                    break;
                }
                user.increaseMoney(-1 * cost);
                user.increaseEducationLevel(1);
                sendEmbed(event,"your new education level:" + user.getEducationLevel(),"cost of next level:" + user.getCost());
                break;
            default:
                if (event.getMessage().getContent().toLowerCase().startsWith("gg~pay")) {
                    pay(event, user, data);
                } else if (event.getMessage().getContent().toLowerCase().startsWith("gg~slot")) {
                    slot(event, user, data);
                } else if (event.getMessage().getContent().toLowerCase().startsWith("gg~mod")) {
                    mod(event, user, data);
                }else if(event.getMessage().getContent().toLowerCase().startsWith("gg~roulette")){
                    new Roulette(event,user,data);
                }
                break;

        }
        cleanUsers(data);
        data.privateSterilize();


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
        int amount = getAmount(args);
        if(amount == -1){
            sendEmbed(event, "error","you need an amount to pay");
            return;
        }


        List<IUser> mentioned = event.getMessage().getMentions();
        if(mentioned.size() == 0){
            sendEmbed(event,"error","you need to mention someone");
            return;
        }
        String toSend = "";
        if(Arrays.asList(args).contains("add")) {
            for (IUser u : mentioned) {
                data.getUser(u).increaseMoney(amount);
                toSend+= "added " + amount + " to " + u.getName() + "\n";
            }
        }else if(Arrays.asList(args).contains("set")){
            for (IUser u : mentioned) {
                data.getUser(u).setMoney(amount);
                toSend+=  "set " + amount + " to " + u.getName() + "\n";
            }
        }else if(Arrays.asList(args).contains("edu")){
            for (IUser u : mentioned) {
                data.getUser(u).setEduation(amount);
                toSend+=  "edu: set " + amount + " to " + u.getName() + "\n";
            }
        }


        sendMessage(event, toSend + "\n" + "done. gg~all:");


        EmbedBuilder b = new EmbedBuilder();
        List<UserGG> users = data.getUserGGs();
        sort(users);
        for(UserGG u : users){
//                      send+=      "`" +  u.getId() + "`  " + client.getUserByID(u.getId()).getName() + " : " + u.getMoney() + GG   + "\n";
            b.appendField(client.getUserByID(u.getId()).getName(),// + " : " + u.getMoney() + GG,//title
//                            u.getId() + "",//discri
                    u.getMoney() + GG,
                    false);
        }
//                sendEmbed(event, send);
        RequestBuffer.request(() -> {
            event.getChannel().sendMessage(b.build());
        });
    }

    private void cleanUsers(GuildDataOrginizer data) {
	    List<UserGG> hasZero = new ArrayList<>();
	    for(UserGG u : data.getUserGGs()){
	        if(u.getMoney() == 0){
	            hasZero.add(u);
            }
        }
       data.getUserGGs().removeAll(hasZero);
    }

    private int getAmount(String[] args){
        int amount = -1;
        for(String str : args){
            int temp = -1;
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
        return amount;
    }


    private void slot(MessageReceivedEvent event, UserGG user, GuildDataOrginizer data) {
        int amount = -1;
        for(String str : event.getMessage().getContent().split(" ")){
            int temp = -1;
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
        if(a == b && b == c && a == letters.length - 1){//n^3
            user.increaseMoney(-1 * amount);
            user.increaseMoney(amount * 15_000);
            sendEmbed(event,"YOU WON THE SUPER BIG PRIZE " + (amount * 15_000) + GG + "!!!!", "your balance:" + user.getMoney());
        }else if(a == b && b == c){
            user.increaseMoney(-1 * amount);
            //win grand prize
            user.increaseMoney(amount * 500);
            sendEmbed(event,"YOU WON " + (amount * 500) + GG + "!!!!", "your balance:" + user.getMoney());
        }else if(a == b || b == c|| c == a){
            //win small prize
            int count = 0;
            if(a == letters.length - 1)count++;
            if(b == letters.length - 1)count++;
            if(c == letters.length - 1)count++;
            if(count == 0) {
                user.increaseMoney(-1 * amount);
                user.increaseMoney(amount * 20);
                sendEmbed(event, "you won a small prize: " + (amount * 20) + GG + "!", "your balance:" + user.getMoney());
            }else if(count == 1){
                user.increaseMoney(-1 * amount);
                user.increaseMoney(amount * 20 + 500);
                sendEmbed(event, "you won a small prize: " + (amount * 20) + GG + ", as well as the bonus: 500", "your balance:" + user.getMoney());
            }else if(count == 2){
                user.increaseMoney(-1 * amount);
                user.increaseMoney(amount * 500);
                sendEmbed(event, "you won a " + STAR + " prize: " + (amount * 500) + GG + "!!!! " + STAR + " " + STAR + " ", "your balance:" + user.getMoney());
            }
        }else{
            int count = 0;
            if(a == letters.length - 1)count++;
            if(b == letters.length - 1)count++;
            if(c == letters.length - 1)count++;
            if(count == 0) {
                user.increaseMoney(-1 * amount);
                sendEmbed(event, "you won nothing :*(", "your balance:" + user.getMoney());
            }else{//count == 1
                sendEmbed(event, "you didn't win, but you got a " + STAR + " so you keep your money","your balance:" + user.getMoney());
            }
        }
        

    }


    private void pay(MessageReceivedEvent event, UserGG user, GuildDataOrginizer data){
        String[] args = event.getMessage().getContent().toLowerCase().split(" ");
        if(args.length == 1){
            sendMessage(event, "you need more args");
            return;
        }
        int amount = getAmount(args);
        if(amount == -1){
            sendEmbed(event, "error","you need an amount to pay");
            return;
        }
        if(amount < 1){
            sendEmbed(event, "error","you can not pay that");
            return;
        }

        List<IUser> mentioned = event.getMessage().getMentions();
        if(mentioned.size() == 0){
            sendEmbed(event,"error","you need to mention someone");
            return;
        }


        if(amount * mentioned.size() > user.getMoney()){
            sendEmbed(event,"error:", "you do not have enough money to pay " + mentioned.size() + " people " + amount + GG + "\n" + "you have " + user.getMoney() + GG);
            return;
        }
        user.setMoney(user.getMoney() - (amount * mentioned.size()));
        for(IUser recipient : mentioned){
            if(recipient.isBot()){
                sendMessage(event, "you can not pay the bot. You are losing that money <:thonk:440249411574956032>");
            }
            data.getUser(recipient).increaseMoney(amount);
        }
        sendEmbed(event,"Worked!","your balance:" + user.getMoney());
        
    }


	@Override
	public String getName() {
		return "GG handle";
	}

	@Override
	public String getDisciption() {
		return null;
	}
	

    private List<UserGG> sort(List<UserGG> users){
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
	    b.appendField(str,dis,false);
        RequestBuffer.request(() -> {
            event.getChannel().sendMessage(b.build());
        });
    }


}
