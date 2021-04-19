package com.mnaruzny.revolutionbot;

import com.mnaruzny.revolutionbot.audio.AudioController;
import com.mnaruzny.revolutionbot.listener.audio.RandomSpeak;
import com.mnaruzny.revolutionbot.listener.RemoteControl;
import com.mnaruzny.revolutionbot.listener.SmartReplyListener;
import com.mnaruzny.revolutionbot.listener.commands.AdminCommandListener;
import com.mnaruzny.revolutionbot.listener.commands.DMCommand;
import com.mnaruzny.revolutionbot.listener.commands.GeneralCommandListener;
import com.mnaruzny.revolutionbot.registry.DataConnector;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class MainBot {
    public static void main(String[] args) throws LoginException {

        DataConnector dataConnector = new DataConnector();

        JDABuilder builder = JDABuilder.createDefault(System.getenv("APIKEY"));
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setActivity(Activity.watching("society collapse"));
        JDA jda = builder.build();

        AudioController audioController = new AudioController();

        // Register Listeners
        jda.addEventListener(new GeneralCommandListener(dataConnector));
        jda.addEventListener(new AdminCommandListener(dataConnector));
        jda.addEventListener(new DMCommand(dataConnector));

        jda.addEventListener(new RandomSpeak(dataConnector, audioController));
        jda.addEventListener(new SmartReplyListener(dataConnector));
        jda.addEventListener(new RemoteControl(jda));

    }
}
