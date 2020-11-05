package com.mnaruzny.revolutionbot.listener.commands;

import com.mnaruzny.revolutionbot.listener.GuildSetupListener;
import com.mnaruzny.revolutionbot.registry.entities.NNNStatus;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Properties;

public class NNNcommands extends ListenerAdapter {

    private Properties config;

    public NNNcommands(Properties config){
        this.config = config;
    }

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

                NNNStatus status = NNNStatus.getStatus(config, member);

                if(status.hasUserJoined()){
                    event.getGuild().removeRoleFromMember(member, survivingRole).queue();
                    event.getGuild().addRoleToMember(member, failRole).queue();
                    message.getChannel().sendMessage("<@" + message.getAuthor().getId() + "> We can't all survive..").queue();
                    status.setUserFailed(true);
                } else {
                    message.getChannel().sendMessage("<@" + message.getAuthor().getId() + "> You never joined... weakling").queue();
                }
                return;
            }

            if(command.equalsIgnoreCase("join")){

                Member member = event.getMember();
                if(member == null) return;

                NNNStatus status = NNNStatus.getStatus(config, member);

                if(status.hasUserFailed()){
                    message.getChannel().sendMessage("<@" + message.getAuthor().getId() + "> You cannot rejoin after failing...").queue();
                    event.getGuild().addRoleToMember(member, failRole).queue();
                    return;
                }

                if(!status.hasUserJoined()){
                    event.getGuild().addRoleToMember(member, survivingRole).queue();
                    message.getChannel().sendMessage("<@" + message.getAuthor().getId() + "> Goodluck...").queue();
                    status.setUserJoined(true);
                } else {
                    message.getChannel().sendMessage("<@" + message.getAuthor().getId() + "> You've already joined!").queue();
                    event.getGuild().addRoleToMember(member, survivingRole).queue();
                }
                return;
            }

            if(command.equalsIgnoreCase("info")){
                EmbedBuilder eb = new EmbedBuilder();
                NNNStatus s = NNNStatus.getStatus(config, message.getMember());
                eb.setTitle("NNN User Status");
                eb.addField("Username", message.getAuthor().getName(), false);
                eb.addField("Joined", (s.hasUserJoined()) ? "True" : "False", false);
                eb.addField("Failed", (s.hasUserFailed()) ? "True" : "False", false);
                message.getChannel().sendMessage(eb.build()).queue();
                return;
            }

            if(command.equalsIgnoreCase("help")){
                message.getChannel().sendMessage("Help Menu Things...\n" +
                        "r!nnn join - Join the challenge\n" +
                        "r!nnn fail - Admit defeat...\n").queue();
            }

        }
    }

}
