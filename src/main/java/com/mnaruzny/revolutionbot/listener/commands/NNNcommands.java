package com.mnaruzny.revolutionbot.listener.commands;

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

            if(command.equalsIgnoreCase("fail")){
                Member member = event.getMember();
                if(member == null) return;
                Role failRole = message.getGuild().getRolesByName("fail", true).get(0);
                Role survivingRole = message.getGuild().getRolesByName("surviving", true).get(0);
                if(member.getRoles().contains(survivingRole)){
                    event.getGuild().removeRoleFromMember(member, survivingRole).queue();
                }
                event.getGuild().addRoleToMember(member, failRole).queue();
                return;
            }

            if(command.equalsIgnoreCase("join")){
                Member member = event.getMember();
                if(member == null) return;
                Role failRole = message.getGuild().getRolesByName("fail", true).get(0);
                Role survivingRole = message.getGuild().getRolesByName("surviving", true).get(0);
                if(member.getRoles().contains(failRole)){
                    event.getGuild().removeRoleFromMember(member, failRole).queue();
                    return;
                }
                if(!member.getRoles().contains(survivingRole)){
                    event.getGuild().addRoleToMember(member, survivingRole).queue();
                    return;
                }
            }

        }
    }

}
