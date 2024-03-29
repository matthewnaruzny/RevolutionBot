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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminCommandListener extends ListenerAdapter {

    private final HashMap<Long, List<Member>> purgeList;
    private final DataConnector dataConnector;

    public AdminCommandListener(DataConnector dataConnector){
        this.dataConnector = dataConnector;
        purgeList = new HashMap<>();
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

            if(command.equals("mrping")){

                message.getTextChannel().sendMessage("https://cdn.discordapp.com/attachments/619735741089579009/833208288044384296/image0.png").queue();

                String mentionMessage;
                if(message.getMentionedMembers().size() == 0){
                    mentionMessage = message.getGuild().getPublicRole().getAsMention();
                } else {
                    StringBuilder sb = new StringBuilder();
                    for(Member member : message.getMentionedMembers()){
                        sb.append(member.getAsMention());
                    }
                    mentionMessage = sb.toString();
                }

                for(int i = 0; i < 19; i++){
                    message.getTextChannel().sendMessage(mentionMessage).queue();
                }
                return;
            }

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
                    member.kick("Revolution!").queue();
                    message.getTextChannel().sendMessage("Kicked <@" + member.getId() + ">").queue();
                }
                return;
            }

            // Raise to Admin
            if(command.equals("op")){
                for(Member member : message.getMentionedMembers()){
                    try {
                        dataConnector.getMemberSettings(member.getIdLong(), member.getGuild().getIdLong()).setAdmin(true);
                        message.getTextChannel().sendMessage("Done! <@" + member.getId() + "> is now admin!").queue();
                    } catch (SQLException exception) {
                        message.getTextChannel().sendMessage("Error").queue();
                        exception.printStackTrace();
                    }
                }
            }

            // Demote from Admin
            if(command.equals("deop")){
                for(Member member : message.getMentionedMembers()){
                    try {
                        dataConnector.getMemberSettings(member.getIdLong(), member.getGuild().getIdLong()).setAdmin(false);
                        message.getTextChannel().sendMessage("Done! <@" + member.getId() + "> is no longer admin!").queue();
                    } catch (SQLException exception){
                        message.getTextChannel().sendMessage("Error").queue();
                        exception.printStackTrace();
                    }
                }
            }

            // Purge Function Commands
            if(command.equals("purge")){

                if(!purgeList.containsKey(message.getGuild().getIdLong())){
                    purgeList.put(message.getGuild().getIdLong(), new ArrayList<>());
                }

                List<Member> toPurge = purgeList.get(message.getGuild().getIdLong());

                if(args[2].equals("add")){
                    for(Member member : message.getMentionedMembers()){
                        message.getTextChannel().sendMessage("Added <@" + member.getId() + ">").queue();
                        toPurge.add(member);
                    }
                    return;
                }
                if(args[2].equals("del")){
                    for(Member member : message.getMentionedMembers())
                        toPurge.remove(member);
                    return;
                }
                if(args[2].equals("list")){
                    StringBuilder sb = new StringBuilder();
                    sb.append("Purge List");
                    for(Member member : toPurge)
                        sb.append("<@" + member.getIdLong() + ">\n");

                    message.getTextChannel().sendMessage(sb.toString()).queue();
                    return;
                }
                if(args[2].equals("purge")){
                    StringBuilder sb = new StringBuilder();
                    sb.append("Purged:");
                    for(Member member : toPurge){
                        sb.append("<@" + member.getIdLong() + ">\n");
                        member.kick("It happened").queue();
                    }
                    message.getTextChannel().sendMessage(sb.toString()).queue();
                    return;
                }
                if(args[2].equals("clear")){
                    toPurge.clear();
                    message.getTextChannel().sendMessage("Cleared!").queue();
                }

                // Purge Help Menu
                message.getTextChannel().sendMessage("--Purge Time--\n" +
                        "add - Add user to purge list\n" +
                        "del - Remove user from purge list\n" +
                        "list - Display purge list\n" +
                        "purge - Purge the purge list\n").queue();
                return;

            }
        }
    }
}
