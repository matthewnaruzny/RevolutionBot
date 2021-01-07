package com.mnaruzny.revolutionbot.listener;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

public class LoveListener extends ListenerAdapter {

    private final HashMap<String, String> autoReplies;

    public LoveListener() throws IOException {

        // Load data
        HashMap<String, String> autoReplies = new HashMap<>();
        BufferedReader repliesFile = new BufferedReader(new FileReader("autoreply.csv"));
        String row;
        while((row = repliesFile.readLine()) != null){
            String[] data = row.split(",");
            autoReplies.put(data[0], data[1]);
        }
        repliesFile.close();

        this.autoReplies = autoReplies;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        Message message = event.getMessage();

        if(message.getAuthor().isBot()) return;
        if(autoReplies.containsKey(message.getContentRaw().toLowerCase(Locale.ROOT))){
            StringBuilder s = new StringBuilder();
            s.append("<@").append(message.getAuthor().getId()).append("> ");
            s.append(autoReplies.get(message.getContentRaw().toLowerCase(Locale.ROOT)));
            event.getChannel().sendMessage(s.toString()).queue();
        }
    }

}
