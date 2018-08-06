
package com.carson.commandManagers;

import com.carson.classes.DB;
import com.carson.classes.FileIO;
import com.carson.classes.Messenger;
import com.carson.commands.cb.CommandNick;
import com.carson.commands.cb.CommandStatus;
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
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
                IMessage message = new Messenger().sendMessageAndGet(event.getChannel(), "pinging :ping_pong:");
                long ping = System.nanoTime() - time;
                message.edit("pinged :ping_pong:   ping:  " + (ping/1000000) + "  ms");
            }).build(client));

	    addCommand(new CommandBuilder()
            .setCommand("~support")
            .setName("~support")
            .setDescription("get support for Carson-Bot")
            .setRunner((event,content,args) -> {
                new Messenger().sendMessage(client.getOrCreatePMChannel(client.getUserByID(293853365891235841L)),"someone needs help! Their server:"+ event.getGuild().getExtendedInvites().get(0).toString());
                new Messenger().sendMessage(event.getChannel(), "dm me at <@293853365891235841>, or join my support server at discord.gg/BxhRxHW");
            })
            .build(client)
        );
	    addCommand(new CommandBuilder()
                .setCommand("cb-perms")
                .setName("cb-perms")
                .setDescription(null)
                .setRunner((event,content,args) -> {
                    DBHandler db = DBHandler.get();
                    System.out.println("entry size:" + db.getEntrys().size());
                    EmbedBuilder b = new EmbedBuilder();
                    for(DBHandler.Entry entry : db.getEntrys()){
                        b.appendField(entry.getId() + "",
                                "user_id:" + entry.getUser_id() + "\nuser:" + client.getUserByID(entry.getUser_id())
                                + "\nrole_id:" + entry.getRole_id()  + "\nrole:" + client.getRoleByID(entry.getRole_id())
                                + "\nguild_id:" + entry.getGuild_id() + "\nguild:" + client.getGuildByID(entry.getGuild_id()).getName()
                                + "\npermission_level:" + entry.getLevel(),false);
                    }
                    RequestBuffer.request(()->{
                       event.getChannel().sendMessage(b.build());
                    });
                })
                .build(client)
        );
	    addCommand(new CommandBuilder()
                .setCommand("cb-test",CommandBuilder.TestType.STARTS_WITH)
                .setName("cb-test")
                .setDescription(null)
                .setRunner((event,content,args) -> {
                    Command.PermissionLevel level = Command.PermissionLevel.USER;
                    if(args[1].equalsIgnoreCase("mod")){
                        level = Command.PermissionLevel.MOD;
                    }
                    if(args[1].equalsIgnoreCase("bot_admin")){
                        level = CommandSkip.PermissionLevel.BOT_ADMIN;
                    }
                    boolean works = new CommandBTC(client).hasPermission(level,getPermissionLevel(event));
                    RequestBuffer.request(()->{
                       event.getChannel().sendMessage(works?"permission granted":"permission denied");
                    });
                }).build(client)
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
        addCommand(new Command(client){ //cb-s
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
        });//cb-s
        addCommand(new Command(client) {
            @Override
            public boolean test(MessageReceivedEvent event, String content, String[] args) {
                return args[0].equals("cb-perm");
            }

            @Override
            public void run(MessageReceivedEvent event, String content, String[] args) {
                //cb-perm @mention <bot_admin/mod/user>
                //   0      1               3
                List<IRole> roles = event.getMessage().getRoleMentions();
                DBHandler.Entry entry = DBHandler.get().new Entry();
                if(roles.size() == 0){
                    IUser userMentioned = event.getMessage().getMentions().get(0);
                    entry.setUser_id(userMentioned.getLongID());
                }else{
                    entry.setRole_id(roles.get(0).getLongID());
                }
                entry.setGuild_id(event.getGuild().getLongID());

                PermissionLevel permissionLevel;

                if(args[2].equalsIgnoreCase("bot_admin")){
                    permissionLevel = PermissionLevel.BOT_ADMIN;
                }else if(args[2].equalsIgnoreCase("mod")){
                    permissionLevel = PermissionLevel.MOD;
                }else if(args[2].equalsIgnoreCase("user")){
                    permissionLevel = PermissionLevel.USER;
                }else{
                    sendMessage(event, "that is not a valid permission");
                    return;
                }
                entry.setLevel(permissionLevel);
                DBHandler.get().update(entry);
            }

            @Override
            public String getName() {
                return "cb-perm @mention <bot_admin/mod/user>";
            }

            @Override
            public String getDisciption() {
                return null;
            }

            @Override
            public PermissionLevel getWantedPermissionLevel() {
                return PermissionLevel.BOT_ADMIN;
//                return PermissionLevel.MOD;
            }
        });//cb-perm

        addCommand(new CommandBuilder()
                .setCommand("cb-perm-remove", CommandBuilder.TestType.STARTS_WITH)
                .setName("cb-perm-remove")
                .setDescription(null)
                .setRunner((event,content,args) -> {
                    Object id = args[1];
                    DBHandler.get().getPermissionDB().deleteOne(Filters.eq("_id",id));
                })
                .build(client));
    }

	private void addDBCommands(){
        addCommand(new Command(client){

            @Override
            public boolean test(MessageReceivedEvent event, String content, String[] args) {
                return content.equals("db");
            }

            private List<String> getParts(String string, int partitionSize) {
                List<String> parts = new ArrayList<>();
                int len = string.length();
                for (int i=0; i<len; i+=partitionSize)
                    parts.add(string.substring(i, Math.min(len, i + partitionSize)));
                return parts;
            }

            @Override
            public void run(MessageReceivedEvent event, String content, String[] args) {
                String message = DBHandler.get().toString();
                if(!(message.length() > 10_000)) {
                    if (message.length() > 2000) {
                        for (String segment : getParts(message, 1990)) {
                            sendMessage(event, "```" + segment + "```");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        sendMessage(event, "```" + message + " ```");
                    }
                }else{
                    sendMessage(event, "to much text for me to send.sending a file");
                    File db = new File("db" + UUID.randomUUID().toString().replace("-","").substring(0,5) + ".txt");
                    try {
                        db.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    FileIO.use(db).write(message);
                    RequestBuffer.request(() -> {
                        try {
                            System.out.println(db.exists());
                            event.getChannel().sendFile(db);
                        } catch (FileNotFoundException e) {
//                        e.printStackTrace();
                            try {
                                FileIO.use(db).write(message);
                                event.getChannel().sendFile(db);
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                    db.delete();
                }

            }

            @Override
            public String getName() {
                return "db";
            }

            @Override
            public String getDisciption() {
                return "returns the entire database. why would you do this";
            }
        });//db
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
        });//db-drop
        addCommand(new Command(client) {
            @Override
            public boolean test(MessageReceivedEvent event, String content, String[] args) {
                return content.startsWith("db-filter");
            }

            @Override
            public void run(MessageReceivedEvent event, String content, String[] args) {
                if(args.length != 5){
                    sendMessage(event, "unreadable args -_- size:" + args.length);
                    return;
                }
                //db-filter gg money str 1002100
                //  0       1   2     3    4
                //length == 5 WAT
                Object search = args[3].equals("num")?Long.valueOf(args[4]):args[4];
                MongoCollection<Document> database;
                DBHandler db = DBHandler.get();
                switch(args[1]){
                    case "global":
                        database = db.getGlobalDB();
                        break;
                    case "guilds":
                        database = db.getGuildsDB();
                        break;
                    case "users":
                        database = db.getUsersDB();
                        break;
                    case "gg":
                        database = db.getGGDB();
                        break;
//                    case "permissions":
//                        database = db.getPermissionsDB();
//                        break;
                    default:
                        sendMessage(event, "couldn't process DB :thonk:");
                        return;
                }
                FindIterable<Document> documents = database.find(Filters.eq(args[2], search));
                for(Document document : documents){
                    sendMessage(event, "```" + DB.toString(document) + "```");
                }
            }

            @Override
            public String getName() {
                return "db-filter *db* *var* <num/str> *value*";
            }

            @Override
            public String getDisciption() {
                return "sort through the db";
            }

        });//db-filter
        addCommand(new CommandBuilder()//db-size
                .setName("db-size")
                .setDescription("get the size(in chars) of the db")
                .setCommand("db-size")
                .setRunner((event,content,args) -> new Messenger().sendMessage(event.getChannel(),"db size:" + DBHandler.get().toString().length() + " chars")).build(client));
        addCommand(new CommandBuilder()
                .setName("optout")
                .setDescription("opt out of all features of Carson-Bot, including data collection")
                .setCommand("~optout")
                .setRunner((event,content,args) -> {
                    DBHandler.get().getDB("opt").deleteOne(Filters.eq("_id",event.getAuthor().getLongID()));
                    DBHandler.get().getDB("opt").insertOne(new Document().append("_id",event.getAuthor().getLongID()).append("opt",false));
                }).build(client));
        addCommand(new CommandBuilder()
                .setName("optin")
                .setDescription("opt back in to all features of Carson-Bot, including data collection")
                .setCommand("~optin")
                .setRunner((event,content,args) -> {
                    DBHandler.get().getDB("opt").deleteOne(Filters.eq("_id",event.getAuthor().getLongID()));
                    DBHandler.get().getDB("opt").insertOne(new Document().append("_id",event.getAuthor().getLongID()).append("opt",true));
                }).build(client));
        addCommand(new CommandBuilder()
                .setName("opt")
                .setDescription("get your current opt status")
                .setCommand("~opt")
                .setRunner((event,content,args) -> {
                    Document document = DBHandler.get().getDB("opt").find(Filters.eq("_id",event.getAuthor().getLongID())).first();
                    if(document == null || !document.containsKey("opt") || (boolean)document.get("opt")){
                        RequestBuffer.request(()->event.getChannel().sendMessage("You are opted in. opt out with ~optout"));
                    }else{
                        RequestBuffer.request(()->event.getChannel().sendMessage("You are opted out. opt int with ~optin"));
                    }
                }).build(client));
        addCommand(new CommandBuilder()
                .setName("opt_info")
                .setDescription("opt info")
                .setCommand("cb-opt")
                .setRunner((event,content,args) -> {
                    String message = "all of these people have opted out:\n";
                    for(Document d : DBHandler.get().getDB("opt").find(Filters.eq("opt",false))){
                        message+=client.getUserByID((long)d.get("_id")).getName() + "\n";
                    }
                    final String messageFinal = message;
                    RequestBuffer.request(()->{event.getChannel().sendMessage(messageFinal);});
                }).build(client));


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
