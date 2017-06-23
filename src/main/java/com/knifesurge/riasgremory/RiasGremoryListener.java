package com.knifesurge.riasgremory;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.knifesurge.riasgremory.commands.games.Game;
import com.knifesurge.riasgremory.commands.games.GameCategory;
import com.knifesurge.riasgremory.games.GuessingGame;
import com.knifesurge.riasgremory.games.WarGame;
import com.knifesurge.riasgremory.music.RiasAudioListener;
import com.knifesurge.riasgremory.music.RiasAudioManager;
import com.knifesurge.riasgremory.permissions.PermissionManager;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

public class RiasGremoryListener implements EventListener
{
	
	public static final String PRECURSOR = ";>";
	
	public static boolean cmdRun = false;	//Whether or not a command is running
	public static boolean isGame = false;	//Whether or not a game is running
	private static boolean guiSetup = false;	//Whether or not the GUI has been set up
	
	public static Game currentGame = null;
	
	private static PermissionManager permManager = new PermissionManager();
	
	public static Message msg;
	public static Guild currentGuild;
	public static String[] guilds = {"Bromania", "Rias Gremory Official Testing Server"};
	
	private static String prevMsgID = "";
	private static String currMsgID;
	
	private static JFrame frame;
	private static JPanel contentPane;
	private static JLabel channel;

	private static JLabel label;
	private static JButton button;
	public static JTextField input;
	public static JComboBox<String> combo;
	
	public static void setUpGUI()
	{
		cmdRun = true;
		frame = new JFrame("Rias Gremory Command Interface");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(
			new WindowAdapter()
			{
				@Override
				public void windowClosing(WindowEvent e)
				{
					guiSetup = false;
					frame.dispose();
				}
			}
		);
		
		contentPane = new JPanel();
		contentPane.setLayout(new GridLayout(4, 2, 2, 5));
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		channel = new JLabel("Choose a Channel:");
		contentPane.add(channel);
		
		combo = new JComboBox<String>(guilds);
		contentPane.add(combo);

		label = new JLabel("Enter a command:");
		contentPane.add(label);
		
		input = new JTextField("");
		contentPane.add(input);
		
		button = new JButton("Send Command");
		button.setActionCommand("send");
		button.addActionListener(new RiasActionListener());
		contentPane.add(button);
		
		frame.add(contentPane);
		frame.setSize(420, 380);
		frame.setVisible(true);
		
		guiSetup = true;
		cmdRun = false;
	}
	
	public void commandPing(MessageReceivedEvent e)
	{
		long start = System.currentTimeMillis();
		e.getChannel().sendTyping().queue(v -> {
			long ping = System.currentTimeMillis() - start;
			e.getChannel().sendMessage("\uD83D\uDCE3 The ping is " + ping + " ms").queue();
		});
		cmdRun = false;
	}
	
	public void commandHelp(MessageReceivedEvent e)
	{
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("Rias Gremory Available Commands", "https://github.com/Knifesurge/");
		builder.setDescription(";>hello - Says Hello!\n"
							 + ";>ping - Gets the ping of the bot\n" 
							 + ";>help - Shows this message\n"
							 + ";>guessingGame - Plays a guessing game!\n"
							 + ";>guess - Make a guess for the guessing game\n\n");
		builder.appendDescription("--------MUSIC COMMANDS--------\n"
							 + ";>play <URL> - Plays the specified Youtube video as an audio stream.\n"
							 + ";>pause - Pause the current track\n"
							 + ";>resume - Resume the current track\n"
							 + ";>skip - Skip to the next song in the queue\n"
							 + ";>stop - Stops the queue completely and disconnects Rias from the voice channel. If you want to stop a track and play it again afterwards, use ;>pause and ;>resume!\n"
							 + ";>queue - Shows the current tracks in the queue!\n"
							 + ";>nowPlaying - Shows what Track is currently playing!\n"
							 + ";>np - See ;>nowPlaying");
		sendMessage(e, builder);
		cmdRun = false;
	}
	
	public void commandShutdown(MessageReceivedEvent e)
	{
		if(e.getAuthor().getName().contains("Knifesurge"))
		{
			System.out.println(">> Going to sleep!");
			sendMessage(e, "Going to sleep!");
			System.exit(0);
		}
	}

	
	/*
	 * Method to make it easy to send a message
	 */
	public static void sendMessage(MessageReceivedEvent e, String msg)
	{
		e.getChannel().sendMessage(msg).queue();
		cmdRun = false;
	}
	
	/**
	 * Method to make it easy to send a message to a specified Channel
	 * @param c
	 * @param msg
	 */
	public static void sendMessage(MessageChannel c, String msg)
	{
		c.sendMessage(msg).queue();
	}

	/**
	 * Method to make it easy to send an EmbeddedMessage
	 * @param e
	 * @param emb
	 */
	public static void sendMessage(MessageReceivedEvent e, EmbedBuilder emb)
	{
		e.getChannel().sendMessage(emb.build()).queue();
	}
	
	/**
	 * Handles all events that this bot is listening to
	 */
	@Override
	public void onEvent(Event event) {
		if(event instanceof MessageReceivedEvent)
		{
			MessageReceivedEvent e = (MessageReceivedEvent) event;
			if(e.getAuthor().isBot())	//Don't let other bots trigger commands
				return;
			try
			{
				msg = e.getMessage();
				currMsgID = e.getMessage().getId();
				currentGuild = e.getGuild();
				if(!prevMsgID.equals(currMsgID))	//If they aren't the same message
				{
					String rawMsg = e.getMessage().getRawContent();
					System.out.println(e.getAuthor().getName()+": "+rawMsg);
					if(!cmdRun)
					{
						switch(rawMsg)	//Every command case
						{
							case PRECURSOR+"hello":
									cmdRun = true;
									System.out.println(">> "+e.getAuthor().getName()+" requested hello!");
									sendMessage(e, e.getAuthor().getAsMention()+" Hello!");
									cmdRun = false;
									break;
							case PRECURSOR+"guessingGame":
								if(currentGame == null)
								{
										cmdRun = true;
										isGame = true;
										System.out.println(">> "+e.getAuthor().getName()+" requested Guessing Game!");
										currentGame = new GuessingGame("Guessing Game", GameCategory.GUESS);
										System.out.println("Now Playing: "+currentGame);
										sendMessage(e, "Guess the number!");
										cmdRun = false;
								} else
									sendMessage(e, "Game is already running!");
								break;
							case PRECURSOR+"war":
								if(currentGame == null)
								{
									cmdRun = true;
									isGame = true;
									System.out.println(">> "+e.getAuthor().getName()+" requested War Game!");
									currentGame = new WarGame("War Game", GameCategory.CARD);
									System.out.println("Now Playing: "+currentGame);
									currentGame.play(e);
									currentGame = null;
									cmdRun = false;
									isGame = false;
								}
								else
									sendMessage(e, "Game is already running!");
								break;
							case PRECURSOR+"ping":
								commandPing(e);
								break;
							case PRECURSOR+"help":
								cmdRun = true;
								System.out.println(">> "+e.getAuthor().getName()+" requested Help!");
								commandHelp(e);
								cmdRun = false;
								break;
							case PRECURSOR+"shutdown":
								cmdRun = true;
								System.out.println(">> "+e.getAuthor().getName()+" requested shutdown!");
								commandShutdown(e);
								break;
							case PRECURSOR+"gui":
								if(!guiSetup)
								{
									setUpGUI();
									cmdRun = false;
								}
								break;
							case PRECURSOR+"skip":
								cmdRun = true;
								if(!RiasAudioListener.hasTrack(e.getTextChannel()))
								{
									sendMessage(e, "There are no more tracks left! Closing audio stream...");
									RiasAudioManager musicManager = RiasAudioListener.getGuildAudioPlayer(e.getGuild());
									musicManager.player.stopTrack();
									e.getGuild().getAudioManager().closeAudioConnection();
								}
								else
									RiasAudioListener.skipTrack(e.getTextChannel());
								cmdRun = false;
								break;
							case PRECURSOR+"pause":
								cmdRun = true;
								RiasAudioListener.pauseTrack(e.getTextChannel());
								sendMessage(e, "Pausing current track...");
								cmdRun = false;
								break;
							case PRECURSOR+"resume":
								cmdRun = true;
								RiasAudioListener.resumeTrack(e.getTextChannel());
								sendMessage(e, "Resuming track...");
								cmdRun = false;
								break;
							case PRECURSOR+"stop":
								cmdRun = true;
								RiasAudioListener.stopTrack(e.getTextChannel());
								sendMessage(e, "Closing audio stream...");
								e.getGuild().getAudioManager().closeAudioConnection();
								cmdRun = false;
								break;
							case PRECURSOR+"play":
								cmdRun = true;
								sendMessage(e, "Make sure to enter a Youtube URL!");
								cmdRun = false;
								break;
							case PRECURSOR+"np":
							case PRECURSOR+"nowPlaying":
								cmdRun = true;
								if(RiasAudioListener.getGuildAudioPlayer(currentGuild).player.getPlayingTrack() != null)
									sendMessage(e, RiasAudioListener.getGuildAudioPlayer(currentGuild).player.getPlayingTrack().getInfo().title + " Duration: "
												 + RiasAudioListener.getGuildAudioPlayer(currentGuild).player.getPlayingTrack().getDuration() / 1000);
								else
									sendMessage(e, "Nothing is playing!");
								cmdRun = false;
								break;
							case PRECURSOR+"queue":
								cmdRun = true;
								EmbedBuilder embed = new EmbedBuilder();
								embed.setTitle("Queued Songs:", null);
								embed.setDescription(RiasAudioListener.getGuildAudioPlayer(currentGuild).scheduler.getList());
								e.getChannel().sendMessage(embed.build()).queue();
								cmdRun = false;
								break;
							case PRECURSOR+"create":
								cmdRun = true;
								System.out.println(">> Create new permission file for guild " + currentGuild.getId());
								permManager.createPermissionFile(currentGuild);
								System.out.println(">> Permission file created!");
								sendMessage(e, "Permission File created!");
								cmdRun = false;
								break;
							case PRECURSOR+"check":
								cmdRun = true;
								permManager.updatePermissionFile(currentGuild, "Hi there! I just wrote to a file!");
								System.out.println(">> Updated permission file for guild " + currentGuild);
								cmdRun = false;
								break;
/*							case PRECURSOR+"remind":	//Old Ayano bug exploit
								cmdRun = true;
								while(true)
									sendMessage(e, "==Reminder 5 I set this Reminder a loooooooooooooooooooong time ago");
*/							case PRECURSOR+"createJoey":
								cmdRun = true;
								sendMessage(e, "==CreateCustom~GetJoey~Echo @joey101937#6532 Get in here!");
								cmdRun = false;
								break;
							case PRECURSOR+"lel":
								cmdRun = true;
								while(true)
									sendMessage(e, "==GetJoey");
						default:
								if(rawMsg.substring(0, 7).equals(PRECURSOR+"guess"))
								{
									cmdRun = true;
									String guess = rawMsg.substring(8);
									if(GuessingGame.check(e, guess))
										sendMessage(e, "You got it!");
									else
										sendMessage(e, "Maybe you will get it next time!");
									currentGame = null;
									cmdRun = false;
									isGame = false;
								}
								if(rawMsg.startsWith(PRECURSOR+"play https://www.youtube.com/watch"))	//Playing some Youtube *VIDEO* URL
								{
									cmdRun = true;
									String track = rawMsg.substring(39);	//Only get the Video ID
									RiasAudioListener.loadAndPlay(e.getTextChannel(), track);	//Pass video ID into the AudioListener and play the track
									cmdRun = false;
								}
								if(rawMsg.startsWith(PRECURSOR+"play https://www.youtube.com/playlist"))	//Playing some Youtube *PLAYLIST* URL
								{
									cmdRun = true;
									System.out.println(e.getAuthor().getName()+" requested to play the playlist: " + rawMsg.substring(7));
									String list = rawMsg.substring(7);
									RiasAudioListener.loadAndPlay(e.getTextChannel(), list);
									cmdRun = false;
								}
						} 
					} else
					{
						System.out.println(">> Command already running!");
						sendMessage(e, "Command already running!");
					}
				}
			} catch (Exception ex)
			{
				System.out.println("Exception caught!");
				System.err.println(ex.getMessage());
				ex.printStackTrace();
			}
			prevMsgID = currMsgID;
		}
	}
}
