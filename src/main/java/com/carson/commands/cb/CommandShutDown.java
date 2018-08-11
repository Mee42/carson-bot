package com.carson.commands.cb;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.Register;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;

public class CommandShutDown extends Command {
    public CommandShutDown(IDiscordClient c) {
        super(c);
    }

    @Override
    public boolean test(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        throw new RuntimeException();//should never happen
    }


    /**
     *
     * @param event the message event received
     * @return true if command should be runX
     */
    @Override
    public boolean test(MessageReceivedEvent event) {
        if(!hasPermission(getWantedPermissionLevel(), Register.getPermissionLevel(event)))return false;
        if(event.getMessage().getContent().equals("cb-s") || event.getMessage().getContent().equals("cb-shutdown")){
            return true;
        }
        return false;
    }

    @Override
    public void run(String prefix, String content, MessageReceivedEvent event, String rawContent, String[] args) {
        RequestBuffer.RequestFuture<Void> request = RequestBuffer.request(() -> {
            event.getChannel().sendMessage("shutting down");
        });//a get to wait for it to send before shutting down. prevents it from never being shutdown before message is sent
        for(int i = 0;i<30;i++){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(request.isDone())break;
        }
        System.exit(0);
    }

    @Override
    public String getCommandId() {
        return "shutdown";
    }

    @Override
    public PermissionLevel getWantedPermissionLevel() {
        return PermissionLevel.BOT_ADMIN;
    }
}
