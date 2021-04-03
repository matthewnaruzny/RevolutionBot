package com.mnaruzny.revolutionbot.listener;

import com.mnaruzny.revolutionbot.audio.AudioController;
import com.mnaruzny.revolutionbot.audio.AudioPlayerSendHandler;
import com.mnaruzny.revolutionbot.audio.TrackScheduler;
import com.mnaruzny.revolutionbot.registry.DataConnector;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class RandomSpeak extends ListenerAdapter {

    private final DataConnector dataConnector;
    private final AudioController audioController;

    public RandomSpeak(DataConnector dataConnector, AudioController audioController){
        this.dataConnector = dataConnector;
        this.audioController = audioController;
    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        int chance = (int)(Math.random() * 100);
        boolean always = false;

        List<Role> roles = event.getMember().getRoles();
        for(Role role : roles){
            if(role.getName().equalsIgnoreCase("introduce")) always = true;
        }

        if(chance > 50 && chance < 70 || always){
            // Join Voice
            VoiceChannel channel = event.getChannelJoined();
            if(event.getGuild().getSelfMember().equals(event.getMember())) return;
            if(!event.getGuild().getSelfMember().hasPermission(channel, Permission.VOICE_CONNECT)) return;

            AudioPlayer player = audioController.getNewPlayer();
            AudioPlayerSendHandler audioPlayerSendHandler = new AudioPlayerSendHandler(player);
            AudioManager manager = event.getGuild().getAudioManager();
            player.addListener(new TrackScheduler(manager));
            manager.setSendingHandler(audioPlayerSendHandler);
            manager.openAudioConnection(channel);
            List<String> songList;
            try {
                songList = getSongList();
                audioController.playerManager.loadItem(songList.get((int)(Math.random() * songList.size())), new AudioLoadResultHandler() {
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
                        manager.closeAudioConnection();
                    }
                });
            } catch (SQLException e) {
                e.printStackTrace();
            }



        }
    }

    public List<String> getSongList() throws SQLException {
        return dataConnector.getMusicList().getList();
    }
}
