package com.mnaruzny.revolutionbot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.managers.AudioManager;

public class TrackScheduler extends AudioEventAdapter {

    private final AudioManager audioManager;

    public TrackScheduler (AudioManager audioManager){
        this.audioManager = audioManager;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        audioManager.closeAudioConnection();
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {

    }
}
