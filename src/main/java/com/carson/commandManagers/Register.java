
package com.carson.commandManagers;

import java.util.ArrayList;
import java.util.List;


import com.carson.commands.cb.*;
import com.carson.commands.gg.GGHandler;
import com.carson.commands.main.*;
import com.carson.commands.main.dnd.CommandDndStart;
import com.carson.commands.main.dnd.CommandDndTwo;
import com.carson.commands.main.dnd.DndObject;
import com.carson.commands.main.hangman.CommandHangmanOne;
import com.carson.commands.main.hangman.CommandHangmanStart;
import com.carson.commands.main.hangman.CommandHangmanTwo;
import com.carson.commands.main.hangman.HangmanObject;
import com.carson.commands.main.lavaplayer.*;
import com.carson.commands.main.tac.CommandContinueTac;
import com.carson.commands.main.tac.CommandTac;
import com.carson.commands.main.tic.CommandTicStart;
import com.carson.commands.main.tic.CommandTicTwo;
import com.carson.commands.main.tic.TicObject;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class Register{

	
	protected List<Command> commands = new ArrayList<Command>();
	protected IDiscordClient client;
	
	public Register(IDiscordClient c) {
		client = c ;
		addMainCommands();
		addCommand(new GGHandler(c));
        addCarsonBotCommads();
	}


	private void addMainCommands(){
        LavaplayerMain m = new LavaplayerMain();
        DndObject d = new DndObject();
        TicObject v = new TicObject();
        HangmanObject h = new HangmanObject();

        addCommand(new CommandPing(client));
        addCommand(new CommandMemberCount(client));
        addCommand(new CommandRecomand(client));
        addCommand(new CommandIFunny(client));
        addCommand(new CommandGoogle(client));
        addCommand(new CommandReddit(client));
        addCommand(new CommandXKCD(client));
        addCommand(new CommandPurge(client));

        addCommand(new CommandJoin(client));
        addCommand(new CommandLeave(client));
        addCommand(new CommandPlayLocal(client, m));
        addCommand(new CommandSkip(client,m));
        addCommand(new CommandYoutubeKeywords(client,m));
        addCommand(new CommandYoutubeLink(client,m));

        addCommand(new CommandDndStart(client,d));
        addCommand(new CommandDndTwo(client,d));

        addCommand(new CommandTicStart(client,v));
        addCommand(new CommandTicTwo(client,v));

        addCommand(new CommandHangmanStart(client,h));
        addCommand(new CommandHangmanOne(client,h));
        addCommand(new CommandHangmanTwo(client,h));

        addCommand(new CommandGetEmoji(client));

        addCommand(new CommandGetID(client));

        addCommand(new CommandGetRoles(client));

        addCommand(new CommandGetRole(client));

        addCommand(new CommandRank(client));
        addCommand(new CommandLeaderboard(client));

        addCommand(new CommandTac(client));
        addCommand(new CommandContinueTac(client));

        addCommand(new CommnadSetDeathMessage(client));

        addCommand(new CommandMath(client));
        addCommand(new CommandPrime(client));

        addCommand(new CommandSoftBan(client));

        addCommand(new CommandMuseScore(client));

        addCommand(new CommandBTC(client));
    }
    private void addCarsonBotCommads(){
        addCommand(new CommandStatus(client));
        addCommand(new CommandNick(client));
        addCommand(new Command(client){
            @Override public boolean test(MessageReceivedEvent event, String content,String[] args) {
                return content.equals("cb-s");
            }
            @Override public void run(MessageReceivedEvent event,String content, String[] args) {
                System.exit(0);
            }
            @Override public String getName() {
                return "cb-s";
            }
            @Override public String getDisciption() {
                return "shuts down the bot";
            }
            @Override public PermissionLevel getWantedPermissionLevel() {
                return PermissionLevel.BOT_ADMIN;
            }
        });

    }

	
	private Register addCommand(Command c) {
		commands.add(c);
		return this;
	}
	
	
	
	public List<Command> getCommands(){
		return commands;
	}
	
	public void testCommands(MessageReceivedEvent  event) {
		for(Command c : commands) {
			if(c.test(event)) {
				System.out.println("EVENT: running " + c.getName());
				c.run(event);
				return;
			}
		}
	}

	public static Command.PermissionLevel getPermissionLevel(MessageReceivedEvent event) {
        long id = event.getAuthor().getLongID();
        if(id == 293853365891235841L){
            return Command.PermissionLevel.BOT_ADMIN;
        }
        if(id == 279412525051674624L){
            return Command.PermissionLevel.BOT_ADMIN;
        }
        return Command.PermissionLevel.USER;
	}



}
