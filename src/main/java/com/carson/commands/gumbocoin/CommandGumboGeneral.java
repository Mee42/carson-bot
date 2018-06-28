package com.carson.commands.gumbocoin;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandGumboGeneral extends Command implements ICommand{

	
	private static final String helpMenu = ""
			+ "Glad you've decided to take interest into GumboCoin!\n"
			+ "GumboCoin is a cryptocurrency with several major flaws, however\n"
			+ "Its still backed by a solid blockchain, and can not be exploited by the developer(me)\n"
			+ "It also has integeration into discord with <@425376884843347980> allowing you to get the latest stats\n"
			+ "If you have any questions, contact me at <@293853365891235841>\n"
			+ "Heres how it works: You download the jar, or the exe via <@425376884843347980>, and then register a public key\n"
			+ "this can be your discord username, your discord ID, or a random number. JUST REMEMBER IT\n"
			+ "this is what others will use to pay you stuff\n"
			+ "you will be given a private key. save it somewhere, without it you can not make transactions, and your coins are gone\n"
			+ "now set your key on the miner with set_key. You will need to do this every time you use your miner :*(\n"
			+ "then, go to discord and register your key with <@425376884843347980>. This lets people get your public key a lot easyer\n"
			+ "then simply run `mine` on your miner, and it will mine GumboCoins! note: if there are other people mining, block acceptence rate is about 50%.\n"
			+ ""
			+ "Now for the command list!\n"
			+ "```\n"
			+ "~gumbo 		-			gets this help menu\n\n"
			+ "~gumbo set_key [public_key]	-	sets your key IN DISCORD\n\n"
			+ "~gumbo get_key @Mention	-	gets the key for the mentioned person, if they have registered one\n\n"
			+ "~gumbo get_balance [public_key] -	gets the balance of that person\n\n"
			+ "~gumbo get_jar	-	gets the jar file of the miner\n\n"
			+ "~gumbo start	-	gets the exe file of the miner\n\n"
			+ "		the jar will be more updated, but use the exe first, unless you don't want to\n\n"
			+ "~gumbo get_ip	-	when you boot the miner, you need an ip. This is my ip (it will change - but the bot handles that)\n\n"
			+ "~gumbo manual [command]	-	contact @AwesomeCarson123#5069  for more information. You don't need it for normal use\n\n"
			+ "```\n"
			+ "Thats it. Thanks for mining!"
	;
	
	
	
	public CommandGumboGeneral(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().toLowerCase().equals("~gumbo");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		sendMessage(event, helpMenu);
	}

	@Override
	public String getName() {
		return "~gumbo";
	}

	@Override
	public String getDisciption() {
		return "learn how to use ~gumbo commands";
	}

}
