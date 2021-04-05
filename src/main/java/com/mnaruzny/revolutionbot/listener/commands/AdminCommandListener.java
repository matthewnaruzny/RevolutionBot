package com.mnaruzny.revolutionbot.listener.commands;

import com.mnaruzny.revolutionbot.registry.DataConnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;
import java.util.List;

public class AdminCommandListener extends ListenerAdapter {

    private final DataConnector dataConnector;

    public AdminCommandListener(DataConnector dataConnector){
        this.dataConnector = dataConnector;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){

        Message message = event.getMessage();
        String[] args = message.getContentRaw().split(" ");

        if(args[0].equals("r!admin")){
            String command = args[1];

            // Verify User is Admin

            boolean isAdmin;

            try {
                isAdmin = dataConnector.getMemberSettings(message.getMember().getIdLong(), message.getGuild().getIdLong()).isAdmin();
            } catch (SQLException exception) {
                exception.printStackTrace();
                return;
            }

            if(!isAdmin && !message.getMember().isOwner() && !message.getMember().hasPermission(Permission.ADMINISTRATOR)) return;

            if(command.equals("status")){
                for(Member member : message.getMentionedMembers()){
                    try {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setTitle("User Status");
                        eb.addField("User:", member.getNickname(), false);
                        eb.addField("ID:", member.getId(), false);

                        boolean isMAdmin = dataConnector.getMemberSettings(member.getIdLong(), member.getGuild().getIdLong()).isAdmin();
                        eb.addField("Is Admin:", (isMAdmin ? "True" : "False"), true);

                        message.getTextChannel().sendMessage(eb.build()).queue();
                    } catch (SQLException exception) {
                        message.getTextChannel().sendMessage("Error").queue();
                    }
                }
            }

            // Clear chat history
            if(command.equals("kick")){
                for(Member member : message.getMentionedMembers()){
                    member.kick("Rev Mad").queue();
                }
                return;
            }
        }
    }
}
