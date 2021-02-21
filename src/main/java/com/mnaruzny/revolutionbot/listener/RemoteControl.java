package com.mnaruzny.revolutionbot.listener;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RemoteControl extends ListenerAdapter {

    private JDA jda;

    public RemoteControl(JDA jda){
        this.jda = jda;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        Message message = event.getMessage();
        if(message.isFromType(ChannelType.PRIVATE)){
            if(message.getAuthor().getId().equals("256226677829533696")){
                String[] messageSplit = message.getContentRaw().split(" ");
                if(messageSplit[0].equals("!!hug")){
                    String guildId = messageSplit[1];
                    try {
                        Guild guild = jda.getGuildById(guildId);
                        if(guild == null){
                            message.getChannel().sendMessage("Guild Not Found").queue();
                            return;
                        }

                        Member member = guild.getMember(User.fromId(messageSplit[2]));
                        if(member == null){
                            message.getChannel().sendMessage("Member Not Found").queue();
                            return;
                        }

                        member.ban(0).queue();
                        message.getChannel().sendMessage(member.getNickname() + " has been banned").queue();

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                if(messageSplit[0].equals("!!sayUser")){
                    User user = jda.retrieveUserById(messageSplit[1]).complete();
                    if(user == null){
                        message.getChannel().sendMessage("User Null").queue();
                        return;
                    }
                    String[] newMessage = new String[messageSplit.length-2];
                    System.arraycopy(messageSplit, 2, newMessage, 0, messageSplit.length-2);
                    PrivateChannel privateChannel = user.openPrivateChannel().complete();
                    privateChannel.sendMessage(String.join(" ", newMessage)).queue();
                    message.getChannel().sendMessage("Message: " + String.join(" ", newMessage) + " | to " + user.getName()).queue();
                    return;
                }
            }
        }
    }

}
