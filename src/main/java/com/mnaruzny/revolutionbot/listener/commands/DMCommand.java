package com.mnaruzny.revolutionbot.listener.commands;

import com.mnaruzny.revolutionbot.registry.DataConnector;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
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
    }


}
