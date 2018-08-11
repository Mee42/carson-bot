package com.carson.commands.cb;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.Register;
import com.carson.dataObject.DBHandler;
import com.mongodb.client.model.Filters;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;

public class CommandPerm extends Command {
    public CommandPerm(IDiscordClient c) {
        super(c);
    }

    @Override
    public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        throw new RuntimeException();//should never happen
    }

    @Override
    public boolean test(MessageReceivedEvent event) {
        if(!hasPermission(getWantedPermissionLevel(), Register.getPermissionLevel(event)))return false;
        return event.getMessage().getContent().startsWith("cb-perm");
    }


    @Override
    public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        if(rawContent.startsWith("cb-perm-remove")){
            permRemove(event, args);return;
        }else if(rawContent.equals("cb-perms")){
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
            return;
        }

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
        //cb-perm @mention <MOD/BOT_ADMIN>
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

    private void permRemove(MessageReceivedEvent event, String[] args) {
        Object id = args[1];
        DBHandler.get().getPermissionDB().deleteOne(Filters.eq("_id",id));
        sendMessage(event, "removed");
    }

    @Override
    public PermissionLevel getWantedPermissionLevel() {
        return PermissionLevel.BOT_ADMIN;
    }

    @Override
    public String getCommandId() {
        return "perm";
    }
}
