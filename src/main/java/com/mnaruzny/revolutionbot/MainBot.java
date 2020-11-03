package com.mnaruzny.revolutionbot;

import com.mnaruzny.revolutionbot.listener.GeneralCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class MainBot {
    public static void main(String[] args) throws LoginException {

        JDABuilder builder = JDABuilder.createDefault("");
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setActivity(Activity.watching("TV"));
        JDA jda = builder.build();

        // Register Listeners
        jda.addEventListener(new GeneralCommandListener());

    }
}
