package com.knifesurge.riasgremory.music;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public class RiasAudioScheduler extends AudioEventAdapter{

	 private final AudioPlayer player;
	 public final BlockingQueue<AudioTrack> queue;

	  /**
	   * @param player The audio player this scheduler uses
	   */
	  public RiasAudioScheduler(AudioPlayer player) {
	    this.player = player;
	    this.queue = new LinkedBlockingQueue<>();
	  }

	  /**
	   * Add the next track to queue or play right away if nothing is in the queue.
	   *
	   * @param track The track to play or add to queue.
	   */
	  public void queue(AudioTrack track) {
	    // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
	    // something is playing, it returns false and does nothing. In that case the player was already playing so this
	    // track goes to the queue instead.
	    if (!player.startTrack(track, true)) {
	      queue.offer(track);
	    }
	  }

	  public String getList()
	  {
		  String output = "";
		  int sentinel = 0;
		  int num = 12;
		  for(AudioTrack track : queue)
		  {
			  if(sentinel > num)
				  break;
			  output = output.concat((++sentinel) + ". " + track.getInfo().title + "\nDuration: " + track.getDuration() / 1000 + "\n");
		  }
		  output.concat("\n Total Number of Tracks: " + queue.size());
		  return output;
	  }
	  
	  /**
	   * Start the next track, stopping the current one if it is playing.
	   */
	  public void nextTrack() {
	    // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
	    // giving null to startTrack, which is a valid argument and will simply stop the player.
	    player.startTrack(queue.poll(), false);
	  }

	  @Override
	  public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason)
	  {
		  if(endReason == AudioTrackEndReason.FINISHED)
		  {
			  RiasAudioManager manager;
			  queue.clear();
			  player.destroy();
		  }
	    // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
	    if (endReason.mayStartNext) {
	      nextTrack();
	    }
	  }
}
