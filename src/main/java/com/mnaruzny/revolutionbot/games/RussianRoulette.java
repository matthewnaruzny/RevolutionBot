package com.mnaruzny.revolutionbot.games;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;

public class RussianRoulette extends ListenerAdapter {

    private final JDA jda;
    private final Guild guild;
    private final TextChannel textChannel;

    private final Message lastMessage;

    private ArrayList<User> users;

    public RussianRoulette(Guild guild, TextChannel textChannel){
        this.jda = guild.getJDA();
        this.guild = guild;
        this.textChannel = textChannel;
        jda.addEventListener(this);
        lastMessage = textChannel.sendMessage(startEmbed()).complete();
        this.users = new ArrayList<>();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        Message message = event.getMessage();
        if(message.getTextChannel() == textChannel){
            String[] args = message.getContentRaw().split(" ");
            if(args[0].equals("join")){
                users.add(message.getAuthor());
                return;
            }

            if(args[0].equals("start")){

            }
        }
    }



    private void end(){
        jda.removeEventListener(this);
    }

    public MessageEmbed startEmbed(){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("**Russian Roulette**");
        for(User user : this.users){
            eb.addField("User", user.getName(), true);
        }

        return eb.build();
    }



}
