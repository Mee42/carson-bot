
package com.carson.commandManagers;

import com.carson.commands.cb.*;
import com.carson.commands.db.CommandAdd;
import com.carson.commands.db.CommandDb;
import com.carson.commands.db.CommandDbSize;
import com.carson.commands.db.CommandOptOut;
import com.carson.commands.gg.GGHandler;
import com.carson.commands.main.*;
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
import com.carson.dataObject.DBHandler;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class Register{


	protected List<Command> commands = new ArrayList<>();
	protected IDiscordClient client;
	
	public Register(IDiscordClient c) {
		client = c ;
		addMainCommands();
		addCommand(new GGHandler(c));
        addCarsonBotCommands();
        addDBCommands();
	}




	private void addMainCommands(){
        LavaplayerMain m = new LavaplayerMain();
        TicObject v = new TicObject();
        HangmanObject h = new HangmanObject();

        addCommand(new CommandMemberCount(client));
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
        addCommand(new CommandDnd(client));


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

        addCommand(new CommandSetLeaveMessage(client));

        addCommand(new CommandMath(client));
        addCommand(new CommandPrime(client));

        addCommand(new CommandSoftBan(client));

        addCommand(new CommandMuseScore(client));

        addCommand(new CommandBTC(client));

        addCommand(new CommandPing(client));
        addCommand(new CommandSupport(client));
    }

    private void addCarsonBotCommands(){
        addCommand(new CommandStatus(client));
        addCommand(new CommandNick(client));
        addCommand(new CommandShutDown(client));
        addCommand(new CommandPerm(client));
        addCommand(new CommandTestPerm(client));
        addCommand(new CommandTest(client));
        addCommand(new CommandAdd(client));
        addCommand(new CommandNotDone(client));
        addCommand(new CommandOptInfo(client));
    }

	private void addDBCommands() {
        addCommand(new CommandDb(client));
        addCommand(new CommandDbSize(client));
        addCommand(new CommandOptOut(client));
    }

	
	private Register addCommand(Command c) {
	    if(c.getCommandId() == null || c.getCommandId().equals(" ")){
            System.err.println("ERROR COMMAND DOES NOT HAVE ID" + c.getClass().getName());
            System.err.println("NOT ADDINg");
            return this;
	    }
		commands.add(c);
		return this;
	}
	
	
	
	public List<Command> getCommands(){
		return commands;
	}
	
	public void testCommands(MessageReceivedEvent  event) {
		for(Command c : commands) {
			if(c.test(event)) {
//				System.out.println("EVENT: running " + c.getName());
				c.run(event);
				return;
			}
		}
	}


	public static Command.PermissionLevel getPermissionLevel(MessageReceivedEvent event) {
        long id = event.getAuthor().getLongID();
        if(id == 293853365891235841L)return Command.PermissionLevel.BOT_ADMIN;
        List<DBHandler.Entry> entries = DBHandler.get().getEntrys();
        Command.PermissionLevel highest = Command.PermissionLevel.USER;
        for(DBHandler.Entry entry : entries){
            if(entry.getUser_id() == id ||
                    event.getAuthor().getRolesForGuild(event.getGuild()).contains(event.getClient().getRoleByID(entry.getRole_id()))){//if has the role in that server
                Command.PermissionLevel level = entry.getLevel();
                if(level == Command.PermissionLevel.MOD && event.getGuild().getLongID() != entry.getGuild_id()){//if mod in another server
                    continue;//skip this one+
                }else{
//                    System.out.println("current server:" + event.getGuild().getLongID());
//                    System.out.println("    mod server:" + entry.getGuild_id());
                }
                if(level == highest){
                    continue;//skip
                }
                if(level == Command.PermissionLevel.MOD && highest == Command.PermissionLevel.USER){
                    highest = level;
                }
                if(level == Command.PermissionLevel.BOT_ADMIN){
                    return Command.PermissionLevel.BOT_ADMIN;
                }

            }
        }
        if(highest != Command.PermissionLevel.USER)return highest;

        if(event.getAuthor().getLongID() == event.getGuild().getOwnerLongID()){
            return Command.PermissionLevel.MOD;
        }
        return Command.PermissionLevel.USER;
	}



}
