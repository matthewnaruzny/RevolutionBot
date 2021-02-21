package com.mnaruzny.revolutionbot.listener;

import com.mnaruzny.revolutionbot.registry.DataConnector;
import com.mnaruzny.revolutionbot.registry.SmartReplies;
import de.daslaboratorium.machinelearning.classifier.Classification;
import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class SmartReplyListener extends ListenerAdapter {

    private final Classifier<String, String> messageMeaning;
    private final DataConnector dataConnector;

    public SmartReplyListener(DataConnector dataConnector){
        this.dataConnector = dataConnector;
        messageMeaning = new BayesClassifier<>();
        // Load data

        // Learning Data

        SmartReplies smartReplies;
        HashMap<String, ArrayList<String>> training;

        try{
            smartReplies = dataConnector.getSmartReplies();
            training = smartReplies.getTrainingData();
        } catch (SQLException ex){
            System.out.println("** Smart Reply Listener fail to start **\n** DB Connection Error **");
            ex.printStackTrace();
            return;
        }

        training.forEach((category, wordList) -> {
            for(String word : wordList){
                String[] wordSplit = word.split(" ");
                messageMeaning.learn(category, Arrays.asList(wordSplit));
            }
        });

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        if(message.getAuthor().isBot()) return;
        if(event.getChannelType() != ChannelType.TEXT) return;
        if(!event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_WRITE)) return;
        String[] words = message.getContentRaw().split("\\s");
        if(words[0].equals("r!train") && message.getTextChannel().getId().equals("796466460834267156")){
            String category = words[1];
            String[] features = new String[words.length-2];
            System.arraycopy(words, 2, features, 0, words.length-2);
            message.getTextChannel().sendMessage(Arrays.toString(features)).queue();
            messageMeaning.learn(category, Arrays.asList(features));
            try {
                addToDataFile(category, features);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        if(!message.getAuthor().isBot() && (message.getTextChannel().getId().equals("786481114545520650") || message.getTextChannel().getId().equals("796600359312162856"))) {
            String data = messageMeaning.classify(Arrays.asList(words)).toString();
            message.getChannel().sendMessage(data).queue();
        }

        int chance = (int) (Math.random() * (101));
        if(message.getContentRaw().contains("rev")) chance = 0;
        if(chance < 2){
            Classification<String, String> data = messageMeaning.classify(Arrays.asList(words));
            int i;
            String sendMessage = " ";
            try {
                sendMessage = getSmartReply(data.getCategory());
            } catch (NullPointerException ex) {
                System.out.println("NOT FOUND: " + data.getCategory());
                return;
            } catch (SQLException ex){
                ex.printStackTrace();
            }
            String s = "<@" + message.getAuthor().getId() + "> " +
                    sendMessage;
            message.getTextChannel().sendMessage(s).queue();
        }

    }

    private String getSmartReply(String category) throws SQLException {
        SmartReplies smartReplies = dataConnector.getSmartReplies();
        List<String> words = smartReplies.getReplies(category);
        int index = (int) (Math.random()*words.size());
        return words.get(index);

    }

    private void addToDataFile(String category, String[] features) throws IOException {
        try{
            SmartReplies smartReplies = dataConnector.getSmartReplies();
            smartReplies.addTrainingData(category, features);
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

}
