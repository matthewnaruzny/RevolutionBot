package com.mnaruzny.revolutionbot.listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class GuildSetupListener extends ListenerAdapter {

    public static void checkAndFixRoles(Guild guild){

        if(guild.getRolesByName("fail", true).size() == 0){
            guild.createRole().setName("Fail").setMentionable(true).setColor(Color.RED).queue();
        }

        if(guild.getRolesByName("surviving", true).size() == 0){
            guild.createRole().setName("Surviving").setMentionable(true).setColor(Color.GREEN).queue();
        }

    }

}
