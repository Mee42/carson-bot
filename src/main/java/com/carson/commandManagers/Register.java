
package com.carson.commandManagers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.carson.classes.DB;
import com.carson.classes.Messanger;
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
import com.carson.dataObject.DBHandler;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.RequestBuffer;

public class Register{

	
	protected List<Command> commands = new ArrayList<Command>();
	protected IDiscordClient client;
	
	public Register(IDiscordClient c) {
		client = c ;
		addMainCommands();
		addCommand(new GGHandler(c));
        addCarsonBotCommands();
        addDBCommands();
        addBuiltCommands();
	}

	private void addBuiltCommands(){
	    addCommand(new CommandBuilder()
            .setCommand("~ping")
            .setName("~ping")
            .setDescription("ping the bot")
            .setRunner((event,content,args)->{
                long time = System.nanoTime();
                IMessage message = new Messanger().sendMessageAndGet(event.getChannel(), "pinging :ping_pong:");
                long ping = System.nanoTime() - time;
                message.edit("pinged :ping_pong:   ping:  " + (ping/1000000) + "  ms");
            }).build(client));

	    addCommand(new CommandBuilder()
            .setCommand("~support")
            .setName("~support")
            .setDescription("get support for Carson-Bot")
            .setRunner((event,content,args) -> {
                new Messanger().sendMessage(client.getOrCreatePMChannel(client.getUserByID(293853365891235841L)),"someone needs help! Their server:"+ event.getGuild().getExtendedInvites().get(0).toString());
                new Messanger().sendMessage(event.getChannel(), "dm me at <@293853365891235841>, or join my support server at discord.gg/BxhRxHW");
            })
            .build(client)
        );
    }

	private void addMainCommands(){
        LavaplayerMain m = new LavaplayerMain();
        DndObject d = new DndObject();
        TicObject v = new TicObject();
        HangmanObject h = new HangmanObject();

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

        addCommand(new CommandSetLeaveMessage(client));

        addCommand(new CommandMath(client));
        addCommand(new CommandPrime(client));

        addCommand(new CommandSoftBan(client));

        addCommand(new CommandMuseScore(client));

        addCommand(new CommandBTC(client));
    }

    private void addCarsonBotCommands(){
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

	private void addDBCommands(){
        addCommand(new Command(client){

            @Override
            public boolean test(MessageReceivedEvent event, String content, String[] args) {
                return content.equals("db");
            }

            @Override
            public void run(MessageReceivedEvent event, String content, String[] args) {
                String message = DBHandler.get().toString();
                if(message.length() > 2000) {
                    Pattern p = Pattern.compile("(?:.|\n){1,2000}");
                    Matcher m = p.matcher(message);
                    List<String> segments = new ArrayList<String>();
                    while (m.find()) {
                        segments.add(m.group(1));
                    }

                    for (String segment : segments) {
                        sendMessage(event, "```" + segment + "```");
                    }
                }else {
                    sendMessage(event, "```" + message + " ```");
                }
            }

            @Override
            public String getName() {
                return "db";
            }

            @Override
            public String getDisciption() {
                return "print the entire database. why would you do this";
            }
        });
        addCommand(new Command(client) {
            @Override
            public boolean test(MessageReceivedEvent event, String content, String[] args) {
                return content.equals("db-drop");
            }

            @Override
            public void run(MessageReceivedEvent event, String content, String[] args) {
                DBHandler.get().getDatabase().drop();
            }

            @Override
            public String getName() {
                return "db-drop";
            }

            @Override
            public String getDisciption() {
                return "drop the entire db";
            }

            @Override
            public PermissionLevel getWantedPermissionLevel() {
                return PermissionLevel.BOT_ADMIN;
            }
        });
        addCommand(new Command(client) {
            @Override
            public boolean test(MessageReceivedEvent event, String content, String[] args) {
                return content.startsWith("db-filter");
            }

            @Override
            public void run(MessageReceivedEvent event, String content, String[] args) {
                if(args.length != 4){
                    sendMessage(event, "unreadable args -_-");
                    return;
                }
                System.out.println("searching for Filters.eq( " + args[1] + ", " + args[2] + ".valueOf(" + args[3] + "))");
                Object search = args[2].equals("long")?Long.valueOf(args[3]):args[3];
                FindIterable<Document> documents = DBHandler.get().getUsersDB().find(Filters.eq(args[1], search));
                for(Document document : documents){
                    sendMessage(event, "```" + DB.toString(document) + "```");
                }
            }

            @Override
            public String getName() {
                return "db-filter *var* <String/long> *value*";
            }

            @Override
            public String getDisciption() {
                return "sort through the db";
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
