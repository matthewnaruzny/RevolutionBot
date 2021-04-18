package com.mnaruzny.revolutionbot.listener.commands;

import com.mnaruzny.revolutionbot.registry.DataConnector;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;
import java.util.List;

public class GeneralCommandListener extends ListenerAdapter {

    private final DataConnector dataConnector;

    public GeneralCommandListener(DataConnector dataConnector){
        this.dataConnector = dataConnector;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(event.getMessage().getContentRaw().startsWith("r!")){
            Message message = event.getMessage();
            String[] args = message.getContentRaw().split(" ");
            String command = args[0].substring(2);

            if(command.equals("mrping")){
                message.getTextChannel().sendMessage("https://tenor.com/view/discord-bongo-cat-bongo-cat-at-everyone-gif-12569290").queue();
                return;
            }

            if(command.equals("saysorry")){
                message.getTextChannel().sendMessage("Sorry... :disappointed_relieved: ").queue();
                return;
            }

            if(command.equals("insult")){
                try {
                    List<String> insults = dataConnector.getSmartReplies().getReplies("insult");
                    String insult = insults.get((int) (Math.random() * insults.size()));
                    message.getTextChannel().sendMessage("<@" + message.getMentionedMembers().get(0).getId() + "> " + insult).queue();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

            if(command.equals("help")){
                String helpMessage = "--Help Menu--\n" +
                        "so.. i don't really want to help you.. but anyways...\n" +
                        "----\n" +
                        "saysorry - force me to say sorry\n" +
                        "help - display this menu";
                message.getTextChannel().sendMessage(helpMessage).queue();
            }

        }
    }

}
