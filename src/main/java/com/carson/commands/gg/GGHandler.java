package com.carson.commands.gg;



import com.carson.Format;
import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import com.carson.dataObject.DataGetter;
import com.carson.dataObject.GuildDataOrginizer;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;


import java.util.List;

public class GGHandler extends Command implements ICommand{
    public static final String GG = "<:gg:467728709139562497>";

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
        switch (event.getMessage().getContent().toLowerCase()){
            case "gg~money":
                sendMessage( event , "you have " + user.getMoney() + GG);
                break;
            case "gg~work":

                user.increaseMoney(10);
                sendMessage(event, "you now have " + user.getMoney() + GG);
                break;
            case "gg~all":
                String send = "";

                for(UserGG u : data.getUserGGs()){
                      send+=      "`" + u .getId() + "`  " + client.getUserByID(u.getId()).getName() + " : " + u.getMoney() + GG   + "\n";
                }
                sendMessage(event, send);
        }

        if(event.getMessage().getContent().toLowerCase().startsWith("gg~pay")){
            pay(event,user,data);
            return;
        }
	}


	private void pay(MessageReceivedEvent event, UserGG user, GuildDataOrginizer data){
        String[] args = event.getMessage().getContent().toLowerCase().split(" ");
        if(args.length == 1){
            sendMessage(event, "you need more args");
            return;
        }
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
        if(amount == -1){
            sendMessage(event, "you need an amount");
            return;
        }
        if(amount < 1){
            sendMessage(event, "you can not send that");
            return;
        }

        List<IUser> mentioned = event.getMessage().getMentions();
        if(mentioned.size() == 0){
            System.out.println("you need someone to pay");
            return;
        }


        if(amount > (user.getMoney() * mentioned.size())){
            sendMessage(event, "you do not have enough money to pay " + mentioned.size() + " people " + amount + GG + "  you have " + user.getMoney() + GG);
            return;
        }
        user.setMoney(user.getMoney() - (amount * mentioned.size()));
        for(IUser recipient : mentioned){
            data.getUser(recipient).increaseMoney(amount);
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
	


}
