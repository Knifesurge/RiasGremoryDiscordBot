package com.knifesurge.riasgremory.music;

import java.util.HashMap;
import java.util.Map;

import com.knifesurge.riasgremory.RiasGremoryListener;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;

public class RiasAudioListener extends ListenerAdapter{

	private static AudioPlayerManager playerManager;
	private static Map<Long, RiasAudioManager> musicManagers;
	
	public RiasAudioListener()
	{
		this.musicManagers = new HashMap<>();
		
		this.playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
	}
	
	public synchronized static RiasAudioManager getGuildAudioPlayer(Guild guild)
	{
		long guildID = Long.parseLong(guild.getId());
		RiasAudioManager musicManager = musicManagers.get(guildID);
		
		if(musicManager == null)
		{
			musicManager = new RiasAudioManager(playerManager);
			musicManagers.put(guildID, musicManager);
		}
		
		guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
		
		return musicManager;
	}
	
	public static void loadAndPlay(final TextChannel channel, final String trackURL)
	{
		RiasAudioManager musicManager = getGuildAudioPlayer(channel.getGuild());
		
		playerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler()
			{
				@Override
				public void trackLoaded(AudioTrack track)
				{
					channel.sendMessage("Adding to queue " + track.getInfo().title).queue();
					if(track.isSeekable())
						track.setPosition(0L);
					else
					{
						long guildID = Long.parseLong(channel.getGuild().getId());
						musicManagers.remove(guildID);
					}
					
					play(channel.getGuild(), musicManager, track);
				}
				
				@Override
			      public void playlistLoaded(AudioPlaylist playlist) {
			        AudioTrack firstTrack = playlist.getSelectedTrack();

			        if (firstTrack == null) {
			          for(AudioTrack track : playlist.getTracks())
				          play(channel.getGuild(), musicManager, track);
			        }

			        channel.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")").queue();

			        play(channel.getGuild(), musicManager, firstTrack);
			      }

			      @Override
			      public void noMatches() {
			        channel.sendMessage("Nothing found by " + trackURL).queue();
			      }

			      @Override
			      public void loadFailed(FriendlyException exception) {
			        channel.sendMessage("Could not play: " + exception.getMessage()).queue();
			      }
			}
		);
	}
	
	public static void play(Guild guild, RiasAudioManager musicManager, AudioTrack track)
	{
		connectToVoiceChannel(guild.getAudioManager());
		
		if(musicManager.player.isPaused())
			musicManager.player.setPaused(false);
		else
			musicManager.scheduler.queue(track);
	}
	
	public static void skipTrack(TextChannel channel)
	{
		RiasAudioManager musicManager = getGuildAudioPlayer(channel.getGuild());
		musicManager.scheduler.nextTrack();
		
		channel.sendMessage("Skipped to next track!").queue();
	}
	
	public static void resumeTrack(TextChannel channel)
	{
		RiasAudioManager musicManager = getGuildAudioPlayer(channel.getGuild());
		musicManager.player.setPaused(false);
	}
	
	public static void pauseTrack(TextChannel channel)
	{
		RiasAudioManager musicManager = getGuildAudioPlayer(channel.getGuild());
		musicManager.player.setPaused(true);
	}
	
	public static void stopTrack(TextChannel channel)
	{
		RiasAudioManager musicManager = getGuildAudioPlayer(channel.getGuild());
		musicManager.player.stopTrack();
		musicManager.scheduler.queue.clear();
		channel.sendMessage("Stopping track...").queue();
	}
	
	public static boolean hasTrack(TextChannel channel)
	{
		RiasAudioManager musicManager = getGuildAudioPlayer(channel.getGuild());
		if(!musicManager.scheduler.queue.isEmpty())
			return true;
		return false;
	}
	
	private static void connectToVoiceChannel(AudioManager audioManager)
	{
		if(!audioManager.isConnected() && !audioManager.isAttemptingToConnect())
		{
			for(VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannelsByName("general", true))
			{
				audioManager.openAudioConnection(voiceChannel);
				break;
			}
		}
	}
	
}
