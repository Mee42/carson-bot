package com.carson.commands.gg;

import com.carson.commandManagers.Command;
import com.carson.dataObject.GuildDataOrginizer;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Roulette extends Command {


    /**
     * called if message starts with gg~roulette
     */
    public Roulette(MessageReceivedEvent event, UserGG user, GuildDataOrginizer data){
        super(event.getClient());
        String[] args = event.getMessage().getContent().toLowerCase().split(" ");
        if(args.length == 0 || //no message
                args.length == 1 ||//only gg~roulette
                args.length == 2){//only a bet/place
            sendEmbed(event,"error","command used incorrectly");
            return;
        }
        int bet;
        try {
            bet = Integer.parseInt(args[1]);
        }catch(NumberFormatException e){
            sendEmbed(event,"error","that is not a valid bet");
            return;
        }
        if(bet > user.getMoney()){
            sendEmbed(event,"error","you do not have enough money. you have " + user.toString());
            return;
        }
        if(data.getBank().getMoney() < bet * 36){
            sendEmbed(event, "the bank doesn't have enough money.",data.getBank().outOfMoneyMessage());
            return;
        }


        boolean nextIsInt = false;
        try {
            Integer.parseInt(args[2]);
            nextIsInt = true;
        }catch(NumberFormatException e){}
//       int role = new Random().nextInt( max- min+ 1) +min;
        int role = new Random().nextInt(36 + 1 + 1) - 1 ;
        String number = String.valueOf(role);
        if(role == -1){
            number = "00";
        }
        String color = (role == 0 || role == -1)?":large_blue_circle:":(role % 2 == 1)?":red_circle:":":black_circle:";
        String place = "null";
        if(role >= 1 && role <= 12){
            place = "first_seg";
        }else if(role >= 13 && role <= 24){
            place = "middle_seg";
        }else if(role >= 25 && role <=36){
            place = "last_seg";
        }
        String half = "null";
        if(role >= 1 && role <= 18){
            half = "first_half";
        }else if(role >= 19 && role <= 36){
            half = "last_half";
        }

        sendEmbed(event, "It was a " + number,color + "  " + place + "  "  + half);
        Bank bank = data.getBank();

        if(nextIsInt){//if number bet
            List<String> remaining = Arrays.asList(args).subList(2,args.length);
            //all numbers they want to bet on
            List<Integer> bets = new ArrayList<>();
            for(String s : remaining){
                try {
                    int square = Integer.parseInt(s);
                    if(square < 1 || square > 36){
                        throw new NumberFormatException();
                    }
                    bets.add(square);
                }catch(NumberFormatException e){
                    sendMessage(event, "can not bet on square " + s);
                }
            }
            String str = "";
            for(int i : bets){
                str += i + " ";
            }
            str = str.trim();
            if(bets.contains(role)) {
//                sendEmbed(event, "You won!","your rolls:" + str);
                int winAmount = bet * (36 / bets.size()) - bet;
                user.increaseMoney(winAmount);
//                sendEmbed(event,"you got a " + (36 / bets.size()) + " multiplier, so you got " + winAmount,"your balance:" + user.getMoney() + GGHandler.GG);
                bank.changeMoney(-1*((36 / bets.size())*bet));
                sendEmbed(event,"YOU WONNN!!!!! you got:" + (36 / bets.size())*bet + GGHandler.GG,"your balance:" + user.getMoney() + GGHandler.GG + "\nyour ");

                return;
            }else{
                user.increaseMoney(-1 * bet);
                bank.changeMoney(bet);
                sendEmbed(event,"you lost "  + bet + " :(","your balance:" + user.getMoney());
                return;
            }
        }
        boolean win = false;
        int mult = -1;// chance of winning is 1 / (36 / mult) == inverse chance of winning
        //if mult == 4 (4/36 will work), then you get bet * (36 / 4) || bet * 9 == more then \/
        //if mult == 6 (6/36 will work), then you get bet * (36 / 6) || bet * 6 == less then /\
        switch(args[2]){
            case "first_seg":
                if(place.equals("first_seg")){
                    win = true;
                    mult = 12;
                }
                break;
            case "middle_seg":
                if(place.equals("middle_seg")){
                    win = true;
                    mult = 12;
                }
                break;
            case "last_seg":
                if(place.equals("last_seg")){
                    win = true;
                    mult = 12;
                }
                break;
            case "first_half":
                if(half.equals("first_half")){
                    win = true;
                    mult = 18;
                }
                break;
            case "last_half":
                if(half.equals("last_half")){
                    win = true;
                    mult = 18;
                }
                break;
            case "red":
                if(color.equals(":red_circle:")){
                    win = true;
                    mult = 18;
                }
                break;
            case "black":
                if(color.equals(":black_circle:")){
                    win = true;
                    mult = 18;
                }
                break;
            default:
                sendMessage(event,"that is not an option. you lose no money");
                return;
        }
        if(win){
            user.increaseMoney((36 / mult)*bet - bet);//the - bet is to account for losing the money, and then regaining it
            bank.changeMoney(-1 * ((36 / mult)*bet - bet));
            sendEmbed(event,"YOU WONNN!!!!! you got:" + (36 / mult)*bet + GGHandler.GG,"your balance:" + user.getMoney() + GGHandler.GG);
        }else{
            user.increaseMoney(-1 *  bet);
            bank.changeMoney(bet);
            sendEmbed(event,"you did not win :*(","your balance:" + user.getMoney() + GGHandler.GG);
        }
    }

    private void sendEmbed(MessageReceivedEvent event, String str, String dis){
        EmbedBuilder b = new EmbedBuilder();
        b.withColor(new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255)));
        b.appendField(str,dis,false);
        RequestBuffer.request(() -> {
            event.getChannel().sendMessage(b.build());
        });
    }


}
