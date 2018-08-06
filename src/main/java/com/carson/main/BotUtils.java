package com.carson.main;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;

public class BotUtils {

    // Constants for use throughout the bot

    // Handles the creation and getting of a IDiscordClient object for a token
    public static IDiscordClient getBuiltDiscordClient(String token){

        // The ClientBuilder object is where you will attach your params for configuring the instance of your bot.
        // Such as withToken, setDaemon etc
        return new ClientBuilder()
                .withToken(token)
                .setMaxReconnectAttempts(9999)
                .setMaxMessageCacheCount(-1)
                .build();

    }

    
}
