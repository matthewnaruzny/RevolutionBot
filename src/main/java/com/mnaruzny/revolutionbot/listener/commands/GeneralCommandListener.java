package com.mnaruzny.revolutionbot.listener.commands;

import com.mnaruzny.revolutionbot.meme.MemeRequest;
import com.mnaruzny.revolutionbot.meme.RandomMeme;
import com.mnaruzny.revolutionbot.registry.DataConnector;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.io.IOException;
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

            if(command.equals("hi")){
                for(Member member : message.getMentionedMembers()){
                    message.getTextChannel().sendMessage("Hi <@" + member.getId() + "> !").queue();
                }
            }

            if(command.equals("meme")){
                try {
                    boolean denyNSFW = dataConnector.getGuildSettings(message.getGuild().getIdLong()).isChildSafe();

                    MemeRequest memeRequest = RandomMeme.getMeme();

                    if(denyNSFW){
                        while (memeRequest.memes[0].nsfw){
                            memeRequest = RandomMeme.getMeme();
                        }
                    }

                    if(memeRequest.memes[0].nsfw){
                        String mUrl = ("|| " + memeRequest.memes[0].url + " ||");
                        message.getTextChannel().sendMessage(mUrl).queue();
                    } else {
                        message.getTextChannel().sendMessage(memeRequest.memes[0].url).queue();
                    }

                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }

            if(command.equals("suffer")){
                try {
                    boolean denyNSFW = dataConnector.getGuildSettings(message.getGuild().getIdLong()).isChildSafe();

                    MemeRequest memeRequest = RandomMeme.getMeme("MakeMeSuffer");

                    if(denyNSFW){
                        while (memeRequest.memes[0].nsfw){
                            memeRequest = RandomMeme.getMeme();
                        }
                    }

                    if(memeRequest.memes[0].nsfw){
                        String mUrl = ("|| " + memeRequest.memes[0].url + " ||");
                        message.getTextChannel().sendMessage(mUrl).queue();
                    } else {
                        message.getTextChannel().sendMessage(memeRequest.memes[0].url).queue();
                    }

                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }

            if(command.equals("suicide")){
                message.getTextChannel().sendMessage("Goodbye Cruel Word...").queue();
                message.getMember().kick().queue();
                return;
            }

            if(command.equals("ihatematt")){
                message.getTextChannel().sendMessage("Goodbye Cruel Word...").queue();
                message.getMember().kick().queue();
                return;
            }

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
