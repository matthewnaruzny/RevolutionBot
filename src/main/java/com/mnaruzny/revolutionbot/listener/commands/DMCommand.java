package com.mnaruzny.revolutionbot.listener.commands;

import com.mnaruzny.revolutionbot.registry.DataConnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;
import java.util.List;

public class DMCommand extends ListenerAdapter {

    private final DataConnector dataConnector;

    public DMCommand(DataConnector dataConnector){
        this.dataConnector = dataConnector;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        Message message = event.getMessage();
        String[] args = message.getContentRaw().split(" ");
        if(!message.getChannelType().equals(ChannelType.PRIVATE)) return;

        boolean isAdmin = false;
        try {
            isAdmin = dataConnector.getMemberSettings(message.getAuthor().getIdLong(), 0).isAdmin();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(!isAdmin) return;
        if(args[0].equals("log")){
            String guildId = args[1];
            int limit = 100;
            if(args.length > 2) limit = Integer.parseInt(args[2]);

            message.getPrivateChannel().sendMessage("*Audit Log Fetch*\nGuild: " + guildId).queue();
            List<AuditLogEntry> entries;
            try {
                entries = message.getJDA().getGuildById(guildId).retrieveAuditLogs().complete();
            } catch (InsufficientPermissionException ex){
                message.getPrivateChannel().sendMessage("*Insufficient Permission*").queue();
                return;
            }
            int x = 0;
            for(AuditLogEntry entry : entries){
                x++;
                if(x > limit) return;
                String entryMessage = ("Target: " + entry.getTargetId() + " Issuer: " + entry.getUser() + " Changes: " + entry.getChanges() + " Type: " + entry.getType() + "\n" + entry);
                message.getPrivateChannel().sendMessage(entryMessage).queue();
            }
            return;
        }

        if(args[0].equals("usr")){
            User checkUser = null;
            try {
                checkUser = message.getJDA().retrieveUserById(args[1]).complete();
            } catch (ErrorResponseException e) {
                message.getPrivateChannel().sendMessage("**User Not Found**").queue();
                return;
            }
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("User Check");
            eb.setImage(checkUser.getAvatarUrl());
            eb.addField("Name", checkUser.getName(), false);
            eb.addField("Mutual Guilds", checkUser.getMutualGuilds().toString(), false);
            eb.addField("Flags", checkUser.getFlags().toString(), false);
            message.getPrivateChannel().sendMessage(eb.build()).queue();
        }

        if(args[0].equals("getCurrent")){
            PrivateChannel privateChannel = message.getPrivateChannel();
            privateChannel.sendMessage("Channel Id: " + privateChannel.getId() + " Name: " + privateChannel.getUser().getName() + " User Id: " + privateChannel.getUser().getId()).queue();
        }

        if(args[0].equals("convo")){
            int limit = 10;
            String checkId = args[1];
            if(args.length > 2) limit = Integer.parseInt(args[2]);
            MessageHistory history = null;
            PrivateChannel dmChannel = null;

            User checkUser = message.getJDA().retrieveUserById(checkId).complete();

            try {
                message.getPrivateChannel().sendMessage("User: " + checkUser.getName()).queue();
            } catch (NullPointerException ex){
                message.getPrivateChannel().sendMessage("**User does not exist**").queue();
                return;
            }

            try {
                dmChannel = checkUser.openPrivateChannel().complete();
            } catch (NullPointerException e) {
                message.getPrivateChannel().sendMessage("*Chat does not exist!*").queue();
                return;
            }

            message.getPrivateChannel().sendMessage("**DM CHAT LOG**\n*USER:* " + dmChannel.getName()).queue();
            try {
                history = dmChannel.getHistoryAround(dmChannel.getLatestMessageId(), limit).complete();
            } catch (Exception e) {
                message.getPrivateChannel().sendMessage("*Unable to retrieve history*").queue();
                return;
            }
            for(Message historyMessage : history.getRetrievedHistory()){
                message.getPrivateChannel().sendMessage("User: " + historyMessage.getAuthor().getName() + "\n" + historyMessage.getContentRaw()).queue();
            }
            return;

        }
    }


}
