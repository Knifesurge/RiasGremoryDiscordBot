package com.knifesurge.riasgremory.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class RiasAudioManager {

	public AudioPlayer player;
	
	public RiasAudioScheduler scheduler;
	
	public RiasAudioManager(AudioPlayerManager manager)
	{
		player = manager.createPlayer();
		scheduler = new RiasAudioScheduler(player);
		player.addListener(scheduler);
	}
	
	public RiasAudioSendHandler getSendHandler()
	{
		return new RiasAudioSendHandler(player);
	}
}
