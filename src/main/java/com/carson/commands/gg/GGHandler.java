package com.carson.commands.gg;



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
//                sendEmbed( event , "you have " + user.getMoney() + GG);
                sendEmbed(event, "your money",user.getMoney() + GG);
                break;
            case "gg~work":
                int amount = (int)(Math.random()*100);
                user.increaseMoney(amount);
//                sendMessage(event, "you got " + amount + GG + " and you now have " + user.getMoney() + GG);
                sendEmbed(event, "you got " + amount + GG,"current balance:" + user.getMoney() + GG);
                break;
            case "gg~all":
//                String send = "";
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
                break;
            case "gg~easter":
                String str = "```";
                for(long l : data.getEaster()){
                    str += client.getMessageByID(l).getChannel().getName() + "\n";
                }
                sendMessage(event,str + "```");
                break;
            case "gg~data":
                String st = "```";
                for(UserGG use : data.getUserGGs()){
                    st+=use.getId() + ":" + use.getMoney() + "\n";
                }
                sendMessage(event,st + "```");
                break;
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

    protected void sendEmbed(MessageReceivedEvent event, String str, String dis){
	    EmbedBuilder  b = new EmbedBuilder();
	    b.withColor(new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255)));
	    b.appendField(str,dis,false);
        RequestBuffer.request(() -> {
            event.getChannel().sendMessage(b.build());
        });
    }


}
