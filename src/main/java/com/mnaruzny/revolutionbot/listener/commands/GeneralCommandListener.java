package com.mnaruzny.revolutionbot.listener.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GeneralCommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(event.getMessage().getContentRaw().startsWith("r!")){
            Message message = event.getMessage();
            String[] args = message.getContentRaw().split(" ");
            String command = args[0].substring(2);

            if(command.equals("saysorry")){
                message.getTextChannel().sendMessage("Sorry... :disappointed_relieved: ").queue();
                return;
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
