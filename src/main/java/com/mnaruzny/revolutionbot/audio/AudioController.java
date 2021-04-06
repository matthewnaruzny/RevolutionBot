package com.mnaruzny.revolutionbot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;

public class AudioController {

    public final AudioPlayerManager playerManager;

    public AudioController(){
        this.playerManager = new DefaultAudioPlayerManager();

        this.playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        this.playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());

        AudioSourceManagers.registerRemoteSources(playerManager);
    }

    public AudioPlayer getNewPlayer(){
        return playerManager.createPlayer();
    }

}
