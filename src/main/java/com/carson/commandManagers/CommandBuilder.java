package com.carson.commandManagers;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;


public class CommandBuilder {
    private String command;
    private TestType testType;//alternative is starts with
    private Tester tester;
    private Command.PermissionLevel level = Command.PermissionLevel.USER;


    enum TestType{
        EQUAL,STARTS_WITH,CUSTOM
    }
    interface Tester{
        boolean test(MessageReceivedEvent event, String content, String[] args);
    }


    private Runner runner;//actually runs the command
    interface Runner{
        void run(MessageReceivedEvent event, String content, String[] args);
    }
    String name;
    String description;




    public CommandBuilder setCommand(String command){
        this.command = command;
        this.testType = TestType.EQUAL;
        return this;
    }

    public CommandBuilder setCommand(String command, TestType type) {
        this.command = command;
        this.testType = type;
        return this;

    }
    public CommandBuilder setPermissionLevel(Command.PermissionLevel user) {
        level = user;
        return this;
    }

    public CommandBuilder setTester(Tester tester) {
        this.tester = tester;
        this.testType = TestType.CUSTOM;
        return this;
    }

    public CommandBuilder setRunner(Runner runner) {
        this.runner = runner;
        return this;
    }

    public CommandBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CommandBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public Command build(IDiscordClient client){
        return new Command(client){

            @Override
            public boolean test(MessageReceivedEvent event, String content, String[] args) {
                if(testType == TestType.EQUAL){
                    return content.equals(command);
                }else if(testType == TestType.STARTS_WITH){
                    return content.startsWith(command);
                }else{
                    return tester.test(event, content,args);
                }
            }

            @Override
            public void run(MessageReceivedEvent event, String content, String[] args) {
                runner.run(event,content,args);
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getDisciption() {
                return description;
            }

            @Override
            public PermissionLevel getWantedPermissionLevel() {
                return level;
            }
        };
    }
}
