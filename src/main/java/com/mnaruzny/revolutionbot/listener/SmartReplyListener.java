package com.mnaruzny.revolutionbot.listener;

import de.daslaboratorium.machinelearning.classifier.Classification;
import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.*;
import java.util.Arrays;
import java.util.Hashtable;

public class SmartReplyListener extends ListenerAdapter {

    private final Classifier<String, String> messageMeaning;
    private final Hashtable<String, String[]> autoReplies;

    public SmartReplyListener() throws IOException {
        messageMeaning = new BayesClassifier<>();
        // Load data
        BufferedReader repliesFile = new BufferedReader(new FileReader("learningData.csv"));
        String row;
        while((row = repliesFile.readLine()) != null){
            String[] data = row.split(",");
            String[] text = data[1].split("\\s");
            messageMeaning.learn(data[0], Arrays.asList(text));
        }
        repliesFile.close();

        Hashtable<String, String[]> autoReplies = new Hashtable<>();
        repliesFile = new BufferedReader(new FileReader("autoreply.csv"));
        while((row = repliesFile.readLine()) != null){
            String[] data = row.split(",");
            String[] temp;
            if(autoReplies.containsKey(data[0])){
                temp = new String[autoReplies.get(data[0]).length + 1];
                System.arraycopy(autoReplies.get(data[0]),0, temp, 0, autoReplies.get(data[0]).length);
                temp[autoReplies.get(data[0]).length] = data[1];
            } else {
                temp = new String[]{data[1]};
            }
            autoReplies.put(data[0], temp);
        }
        repliesFile.close();

        this.autoReplies = autoReplies;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        if(message.getAuthor().isBot()) return;
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
            String[] temp = autoReplies.get(data.getCategory());
            int i;
            try {
                i = (int) (Math.random() * temp.length);
            } catch (NullPointerException ex) {
                System.out.println("NOT FOUND: " + data.getCategory());
                return;
            }
            String sendMessage = temp[i];
            StringBuilder s = new StringBuilder();
            s.append("<@").append(message.getAuthor().getId()).append("> ");
            s.append(sendMessage);
            message.getTextChannel().sendMessage(s.toString()).queue();
        }

    }

    private void addToDataFile(String category, String[] features) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("learningData.csv", true));
        writer.append("\n");
        writer.append(category).append(",");
        for(int i = 0; i < features.length; i++){
            writer.append(features[i]);
            if(i != features.length-1) writer.append(" ");
        }
        writer.close();
    }

}
