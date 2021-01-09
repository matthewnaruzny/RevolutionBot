package com.mnaruzny.revolutionbot;

import com.mnaruzny.revolutionbot.audio.AudioController;
import com.mnaruzny.revolutionbot.config.ReadPropertyFile;
import com.mnaruzny.revolutionbot.listener.LoveListener;
import com.mnaruzny.revolutionbot.listener.RandomSpeak;
import com.mnaruzny.revolutionbot.listener.SmartReplyListener;
import com.mnaruzny.revolutionbot.listener.commands.GeneralCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Properties;

public class MainBot {
    public static void main(String[] args) throws LoginException, IOException {

        // Get Config
        Properties config = ReadPropertyFile.getProperties(args[0]);

        JDABuilder builder = JDABuilder.createDefault(config.getProperty("discordApiKey"));
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setActivity(Activity.watching("society collapse"));
        JDA jda = builder.build();

        AudioController audioController = new AudioController();

        // Register Listeners
        jda.addEventListener(new GeneralCommandListener());
        jda.addEventListener(new RandomSpeak(audioController));
        jda.addEventListener(new SmartReplyListener(args[1], args[2]));

    }
}
