package com.mnaruzny.revolutionbot.listener;

import com.mnaruzny.revolutionbot.audio.AudioController;
import com.mnaruzny.revolutionbot.audio.AudioPlayerSendHandler;
import com.mnaruzny.revolutionbot.audio.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

public class RandomSpeak extends ListenerAdapter {

    private final AudioController audioController;

    public RandomSpeak(AudioController audioController){
        this.audioController = audioController;
    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        int chance = (int)(Math.random() * 100);
        //event.getGuild().getTextChannelById("796466460834267156").sendMessage("VC Chance Val: " + chance).queue();
        if(chance > 50 && chance < 70){
            // Join Voice
            VoiceChannel channel = event.getChannelJoined();
            if(event.getMember().getUser().isBot()) return;
            if(!event.getGuild().getSelfMember().hasPermission(channel, Permission.VOICE_CONNECT)) return;

            AudioPlayer player = audioController.getNewPlayer();
            AudioPlayerSendHandler audioPlayerSendHandler = new AudioPlayerSendHandler(player);
            AudioManager manager = event.getGuild().getAudioManager();
            player.addListener(new TrackScheduler(manager));
            manager.setSendingHandler(audioPlayerSendHandler);
            manager.openAudioConnection(channel);
            audioController.playerManager.loadItem("https://www.youtube.com/watch?v=-nibRWOghPs", new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack audioTrack) {
                    player.playTrack(audioTrack);
                }

                @Override
                public void playlistLoaded(AudioPlaylist audioPlaylist) {

                }

                @Override
                public void noMatches() {
                    System.out.println("No Matches");
                }

                @Override
                public void loadFailed(FriendlyException e) {
                    System.out.println("Load Failed");
                }
            });


        }
    }
}
