package com.carson.commands.main;

import com.carson.classes.Prime;
import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandPrime extends Command implements ICommand {
    public CommandPrime(IDiscordClient c) {
        super(c);
    }

    @Override
    public boolean test(MessageReceivedEvent event) {
        return event.getMessage().getContent().toLowerCase().startsWith("~prime");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        if(!Prime.getInstance().isDone()){
            sendMessage(event, "We havn't proccessed all of the numbers yet. Check back in a minute or two");
            return;
        }
        String[] primeStrArr = event.getMessage().getContent().replace(",","").split(" ");
        if(primeStrArr.length != 2){
            sendMessage(event, "Illegal arguments");
            return;
        }
        long time = System.currentTimeMillis();
        int pos = 0;
        
        try {
        	pos = Integer.parseInt(primeStrArr[1]);
        }catch  (NumberFormatException e){
            sendMessage(event, "There was an error proccessing your request");
            return;
        }
        if(pos < 0 || pos > Prime.getInstance().getMax()) {
        	sendMessage(event,"thats not inside the valid range: 0 - " + Prime.getInstance().getMax());
        	return;
        }
        boolean isPrime = Prime.getInstance().isPrime(pos);
        if (isPrime) {
            sendMessage(event, primeStrArr[1] + " is prime!");
        } else {
            sendMessage(event, primeStrArr[1] + " is not prime!");
            String str = "factors:```\n";
            for(Integer i : Prime.getInstance().factorize(pos)) {
            	str+=i + "\n";
            }
            str +="```";
            sendMessage(event, str);
        }
        
    }

    @Override
    public String getName() {
        return "~prime *number*";
    }

    @Override
    public String getDisciption() {
        return "test to see if the number is prime. Uses a sieve";
    }
}
