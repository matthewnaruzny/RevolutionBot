package com.mnaruzny.revolutionbot.listener.commands;

import com.mnaruzny.revolutionbot.listener.GuildSetupListener;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NNNcommands extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(event.getMessage().getContentRaw().startsWith("r!nnn")){

            Message message = event.getMessage();
            String[] args = message.getContentRaw().split(" ");
            String command = args[1];

            // Get Roles
            Role survivingRole;
            Role failRole;

            if(event.getGuild().getRolesByName("fail", true).size() == 0){
                GuildSetupListener.checkAndFixRoles(event.getGuild());
            }
            failRole = event.getGuild().getRolesByName("fail", true).get(0);

            if(event.getGuild().getRolesByName("surviving", true).size() == 0){
                GuildSetupListener.checkAndFixRoles(event.getGuild());
            }
            survivingRole = event.getGuild().getRolesByName("surviving", true).get(0);

            if(command.equalsIgnoreCase("fail")){

                Member member = event.getMember();
                if(member == null) return;

                if(member.getRoles().contains(survivingRole)){
                    event.getGuild().removeRoleFromMember(member, survivingRole).queue();
                }
                event.getGuild().addRoleToMember(member, failRole).queue();
                return;
            }

            if(command.equalsIgnoreCase("join")){

                Member member = event.getMember();
                if(member == null) return;

                if(member.getRoles().contains(failRole)){
                    event.getGuild().removeRoleFromMember(member, failRole).queue();

                }
                if(!member.getRoles().contains(survivingRole)){
                    event.getGuild().addRoleToMember(member, survivingRole).queue();
                }
            }

        }
    }

}
