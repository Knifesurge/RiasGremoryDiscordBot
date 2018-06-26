package com.knifesurge.riasgremory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knifesurge.riasgremory.commands.games.Game;
import com.knifesurge.riasgremory.commands.games.GameCategory;
import com.knifesurge.riasgremory.currency.Profile;
import com.knifesurge.riasgremory.games.GuessingGame;
import com.knifesurge.riasgremory.games.WarGame;
import com.knifesurge.riasgremory.music.RiasAudioListener;
import com.knifesurge.riasgremory.music.RiasAudioManager;
import com.knifesurge.riasgremory.permissions.PermissionManager;
import com.knifesurge.riasgremory.roleplaying.Die;
import com.knifesurge.riasgremory.roleplaying.characters.AbilityScore;
import com.knifesurge.riasgremory.roleplaying.characters.Alignment;
import com.knifesurge.riasgremory.roleplaying.characters.Attack;
import com.knifesurge.riasgremory.roleplaying.characters.Background;
import com.knifesurge.riasgremory.roleplaying.characters.CharacterClass;
import com.knifesurge.riasgremory.roleplaying.characters.Proficiency;
import com.knifesurge.riasgremory.roleplaying.characters.RPCharacter;
import com.knifesurge.riasgremory.roleplaying.characters.Race;
import com.knifesurge.riasgremory.roleplaying.characters.Skills;
import com.knifesurge.riasgremory.roleplaying.characters.Trait;
import com.knifesurge.riasgremory.roleplaying.characters.items.Item;
import com.knifesurge.riasgremory.roleplaying.characters.spells.Spell;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

public class RiasGremoryListener implements EventListener
{
	
	public static final String PRECURSOR = ";>";
	
	public static boolean cmdRun = false;	//Whether or not a command is running
	public static boolean isGame = false;	//Whether or not a game is running
	
	public static Game currentGame = null;
	
	private static PermissionManager permManager = new PermissionManager();
	
	public static Message msg;
	public static Guild currentGuild;
	private List<Guild> guilds = RiasGremoryBot.getGuilds();
	private Map<Long, Profile> userProfiles = new HashMap<Long, Profile>();
	
	private static String prevMsgID = "";
	private static String currMsgID;
	
	private int minutes;
	private int hours;
	private int days;
	
	private ObjectOutputStream objOutput;
	private FileOutputStream fileOutput;
	private ObjectInputStream objInput;
	private FileInputStream fileInput;
	
	/* ROLEPLAYING STUFF */
	private Map<Long, RPCharacter> playerCharacters;	// Maps a User's Snowflake to a Character, meaning that everyone can only have one character at most
	/* END */
	
	public RiasGremoryListener()
	{
		minutes = 0;
		hours = 0;
		days = 0;
		
		userProfiles = (Map<Long, Profile>) load("currency\\profiles.dat");
		playerCharacters = new HashMap<Long, RPCharacter>();
		
		RPCharacter mordaiKhalid = createKnifesCharacter();
		long key;
		for(Guild g : guilds)
			for(Member m : g.getMembers())
			{
				key = m.getUser().getIdLong();
				if(key == 205166483284819969L && g.getId().equals("286309199946973184"))	// If its me (identified by my Snowflake ID) and its my guild (Knife's Utopia)
				{
					playerCharacters.put(m.getUser().getIdLong(), mordaiKhalid);
					System.out.println(playerCharacters.get(key));
					break;
				} else
				{
					key = 0;	// "Throw out" whatever Snowflake we just processed
				}
			}
		
		if(userProfiles == null)
		{
			userProfiles = new HashMap<Long, Profile>();
			for(Guild g : guilds)
				for(Member m : g.getMembers())
					userProfiles.put(m.getUser().getIdLong(), new Profile(m.getUser().getIdLong()));
		}
	}
	
	public RPCharacter createKnifesCharacter()
	{
		String name = "Mordai Khalid";
		int level = 3;
		CharacterClass cClass = CharacterClass.WARLOCK;
		Race race = Race.TIEFLING;
		Background bg = new Background("Cultist-Assassin", "Insert Background Description Here!");
		Alignment al = Alignment.CHAOTIC_EVIL;
		int speed = 30;
		int mHP = 20;
		Map<String, AbilityScore> aS = new HashMap<String, AbilityScore>();
		{
			aS.put("STR", new AbilityScore(12));
			aS.put("DEX", new AbilityScore(13));
			aS.put("CON", new AbilityScore(15));
			aS.put("INT", new AbilityScore(16));
			aS.put("WIS", new AbilityScore(15));
			aS.put("CHA", new AbilityScore(19));
		}
		Skills skills = new Skills();
		List<Attack> attacks = new ArrayList<Attack>();
		{
			attacks.add(new Attack("Stab"));
		}
		List<Spell> spells = new ArrayList<Spell>();
		{
			spells.add(new Spell("Freeze Ball"));
		}
		List<Item> equipment = new ArrayList<Item>();
		{
			equipment.add(new Item("Dagger"));
		}
		List<Trait> traits = new ArrayList<Trait>();
		{
			traits.add(new Trait("Dark Sight"));
		}
		List<String> lang = new ArrayList<String>();
		{
			lang.add("Common");
			lang.add("Infernal");
			lang.add("Abyssal");
		}
		List<Proficiency> profs = new ArrayList<Proficiency>();
		{
			profs.add(Proficiency.THIEVES_TOOLS);
		}
		RPCharacter rpC = new RPCharacter(name, level, cClass, race, bg, al, speed, mHP, aS, skills,
				attacks, spells, equipment, traits, lang, profs);
		return rpC;
	}
	
	public Object load(String path)
	{
		try
		{
			File file = new File("C:\\Users\\carlo\\Documents\\Programming\\Programming\\Java\\DiscordBots\\RiasGremoryDiscordBot\\dat\\" + path);
		
			
			if(!file.exists())
			{
				System.out.println("<><> Can't find the File you are looking for: " + file.getPath());
			}
			
			fileInput = new FileInputStream(file);
			objInput = new ObjectInputStream(fileInput);
			
			return objInput.readObject();
		} catch(IOException ioe)
		{
			ioe.printStackTrace();
			return null;
		} catch(ClassNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public void save(Object o, String path)
	{
		try
		{
			File dir = new File("C:\\Users\\carlo\\Documents\\Programming\\Programming\\Java\\DiscordBots\\RiasGremoryDiscordBot\\dat\\");
			boolean creation = dir.mkdirs();
			
			File file = new File(dir, path);
			
			System.out.println("<><> " + file.getPath());
			
			if(!file.exists())
			{
				System.out.println("<><> File " + file.getPath() + " doesn't exist! Creating it now...");
				if(creation)
					file.createNewFile();
			}
			
			fileOutput = new FileOutputStream(file);
			objOutput = new ObjectOutputStream(fileOutput);
			
			objOutput.writeObject(o);
			
			System.out.println("<><> Object written to " + file.getPath() + "!");
			
			objOutput.flush();
			fileOutput.flush();
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static boolean isKnifesurge(MessageReceivedEvent e)
	{
		return e.getAuthor().getName().contains("Knifesurge");
	}
	
	public void commandPing(MessageReceivedEvent e)
	{
		long start = System.currentTimeMillis();
		e.getChannel().sendTyping().queue(v -> {
			long ping = System.currentTimeMillis() - start;
			e.getChannel().sendMessage("\uD83D\uDCE3 The ping is " + ping + " ms").queue();
		});
	}
	
	public void commandHelp(MessageReceivedEvent e)
	{
		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription("***Rias Gremory Available Commands***\n\n"
							 + "**General Commands**\n"
							 + ";>hello - Says Hello!\n"
							 + ";>ping - Gets the ping of the bot\n" 
							 + ";>help - Shows this message\n"
							 + ";>guessingGame - Plays a guessing game!\n"
							 + ";>guess - Make a guess for the guessing game\n\n");
		builder.appendDescription("**MUSIC COMMANDS**\n"
							 + ";>play <URL> - Plays the specified Youtube video as an audio stream.\n"
							 + ";>pause - Pause the current track\n"
							 + ";>resume - Resume the current track\n"
							 + ";>skip - Skip to the next song in the queue\n"
							 + ";>stop - Stops the queue completely and disconnects Rias from the voice channel. If you want to stop a track and play it again afterwards, use ;>pause and ;>resume!\n"
							 + ";>queue - Shows the current tracks in the queue!\n"
							 + ";>nowPlaying - Shows what Track is currently playing!\n"
							 + ";>np - See ;>nowPlaying\n\n"
							 + "**Currency Commands**\n"
							 + ";>profile - Shows your own profile. If you mention someone after the command, it shows their profile. Ie. ;>profile @RiasGremory#1516 will show"
							 + " Rias' profile!\n"
							 + ";>add <user> <amt> - Adds the specified amount of credits to the mentioned user\n"
							 + ";>gamble <amt> - Gambles the specified amount of credits. Use 'all' to gamble all of your credits!\n");
		sendEmbed(e, builder);
	}
	
	public void commandShutdown(MessageReceivedEvent e)
	{
		if(isKnifesurge(e))
		{
			System.out.println(">> Going to sleep!");
			sendMessage(e, "Going to sleep!");
			save(userProfiles, "currency\\profiles.dat");
			RiasGremoryBot.getJDA().shutdown();
			System.exit(0);
		}
	}

	public void sendMessageToAllGuilds(String message)
	{
		guilds = RiasGremoryBot.getGuilds();
		for(Guild g : guilds)
		{
			MessageChannel c = g.getDefaultChannel();
			sendMessage(c, message);
		}
	}
	
	/*
	 * Method to make it easy to send a message
	 */
	public static void sendMessage(MessageReceivedEvent e, String msg)
	{
		e.getChannel().sendMessage(msg).queue();
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
	public static void sendEmbed(MessageReceivedEvent e, EmbedBuilder emb)
	{
		e.getChannel().sendMessage(emb.build()).queue();
	}
	
	/**
	 * Handles all events that this bot is listening to
	 */
	@Override
	public void onEvent(Event event) {
		if(event instanceof GuildJoinEvent)
		{
			System.out.println("Guild Join Event!");
		}
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
					String rawMsg = e.getMessage().getContentRaw();
					System.out.println(currentGuild.getName() + ":" + e.getAuthor().getName() + ": " + rawMsg);
					if(rawMsg.startsWith(PRECURSOR+"profile"))
					{
						EmbedBuilder profileEmbed = new EmbedBuilder();
						if(rawMsg.equalsIgnoreCase(PRECURSOR+"profile"))	// Get own profile (id starts with <@ and ends with > )
						{
							System.out.println("Getting " + e.getAuthor().getName() + "'s profile");
							profileEmbed.setTitle(e.getAuthor().getName() + "'s profile", "https://riasgremorybot.epizy.com");
							profileEmbed.setDescription("Credits: " + userProfiles.get(e.getMember().getUser().getIdLong()).getCredits());
							sendEmbed(e, profileEmbed);
						} else	// Get anyone else's profile
						{
							String[] users = rawMsg.split(" ");
							String getUser = users[1];
							Member giveTo;
							if(getUser.startsWith("<@!"))
								giveTo = currentGuild.getMemberById(Long.parseLong(getUser.trim().substring(3, 21)));
							else
								giveTo = currentGuild.getMemberById(Long.parseLong(getUser.trim().substring(2, 20)));
							String name = giveTo.getEffectiveName();
							System.out.println(giveTo);
							System.out.println("Getting " + name + "'s profile");
							profileEmbed.setTitle(name + "'s profile", "https://riasgremorybot.epizy.com");
							if(userProfiles.get(giveTo.getUser().getIdLong()) == null)
							{
								userProfiles.put(giveTo.getUser().getIdLong(), new Profile(giveTo.getUser().getIdLong()));
							}
							profileEmbed.setDescription("Credits: " + userProfiles.get(giveTo.getUser().getIdLong()).getCredits());
							sendEmbed(e, profileEmbed);
						}
					} else if(rawMsg.startsWith(PRECURSOR+"gamble"))
					{
						String amount = rawMsg.split(" ")[1].trim();
						long toBet;
						if(userProfiles.get(e.getMember().getUser().getIdLong()).getCredits() == 0)
						{
							sendMessage(e, "You don't have any credits! Try ;>loot for some more!");
							return;
						}
						if(amount.equalsIgnoreCase("all"))
						{
							toBet = userProfiles.get(e.getMember().getUser().getIdLong()).getCredits();
							int randomNum = (int)((100 - 0) * Math.random() + 0);
							if(randomNum >= 50)
							{
								long winnings = (long) (toBet / Math.random());	// Dividing cause Math.random() returns a decimal, so really we are multiplying by a value between 0 and 1 inclusive
								sendMessage(e, e.getAuthor().getAsMention() + " won " + winnings + " credits!");
								userProfiles.get(e.getMember().getUser().getIdLong()).addCredits(winnings);
							} else
							{
								sendMessage(e, "You lost all of your credits!");
								userProfiles.get(e.getMember().getUser().getIdLong()).setBalanceEmpty();
							}
						} else if(Integer.parseInt(amount) < 0)
						{
							sendMessage(e, "You can't bet negative credits!");
							return;
						} else
						{
							toBet = Long.parseLong(amount);
							int randomNum = (int)((100 - 0) * Math.random() + 0);
							if(randomNum >= 50)
							{
								long winnings = (long)(toBet / Math.random());
								sendMessage(e, e.getAuthor().getAsMention() + " won " + winnings + " credits!");
								userProfiles.get(e.getMember().getUser().getIdLong()).addCredits(winnings);
							}
						}
					} else if(rawMsg.startsWith(PRECURSOR+"loot"))
					{
						long looted = (long)((500 - 100) * Math.random() + 100);
						sendMessage(e, e.getAuthor().getAsMention() + " looted " + looted + " credits!");
						userProfiles.get(e.getMember().getUser().getIdLong()).addCredits(looted);
					} else if(rawMsg.startsWith(PRECURSOR+"sendToAll"))
					{
						String message = rawMsg.substring(12);
						sendMessageToAllGuilds(message);
					} else if(rawMsg.startsWith(PRECURSOR+"guess"))
					{
						String guess = rawMsg.substring(8);
						int guessedNumber = -1;
						try
						{
							guessedNumber = Integer.parseInt(guess);
						} catch(NumberFormatException nfe)
						{
							sendMessage(e, "Please enter an Integer value!");
							guessedNumber = -1;
							return;
						}
						if(GuessingGame.check(e, guessedNumber))
							sendMessage(e, "You got it!");
						else
							sendMessage(e, "Maybe you will get it next time!");
						currentGame = null;
						isGame = false;
					} else if(rawMsg.equals(PRECURSOR+"uptime"))
					{
						long runningTime;
						long currentTime = System.nanoTime();
						long startTime = RiasGremoryBot.getStartTime();
						int seconds;
						runningTime = currentTime - startTime;
						seconds = (int) (runningTime / 1_000_000_000D);
//						minutes =  (int) (runningTime / (60 * 1_000_000_000D) % 60);
//						hours = (int) (runningTime / (60 * 60 * 1_000_000_000D) % 60);
//						days = (int) (runningTime / (24 * 60 * 60 * 1_000_000_000D) % 24);

						System.out.println("RUNNING TIME: " + runningTime + "\nSECONDS: " + seconds + "\nMINUTES: " + minutes + "\nHOURS: " + hours + "\nDAYS: " + days);
						if((seconds - (minutes * 60)) >= 60)
						{
							minutes++;
						}
						if((minutes - (hours * 60)) >= 60)
						{
							hours++;
						}
						if((hours - (days * 24)) >= 24)
						{
							days++;
						}
						
						seconds -= minutes * 60;
						minutes -= hours * 60;
						hours -= days * 24;
						
						sendMessage(e, "Current uptime: " + days + " days, " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds.");
					} else if(rawMsg.startsWith(PRECURSOR+"play https://www.youtube.com/watch"))	//Playing some Youtube *VIDEO* URL
					{
						String track = rawMsg.substring(39);	//Only get the Video ID
						RiasAudioListener.loadAndPlay(e.getTextChannel(), track, e.getMember());	//Pass video ID into the AudioListener and play the track
					} else if(rawMsg.startsWith(PRECURSOR+"play https://www.youtube.com/playlist"))	//Playing some Youtube *PLAYLIST* URL
					{
						System.out.println(e.getAuthor().getName()+" requested to play the playlist: " + rawMsg.substring(7));
						String list = rawMsg.substring(7);
						RiasAudioListener.loadAndPlay(e.getTextChannel(), list, e.getMember());
					} else if(rawMsg.startsWith(PRECURSOR+"roll"))
					{
						String restOfString = rawMsg.substring(7);
						String[] pieces = restOfString.split("d");
						int numDice = Integer.parseInt(pieces[0]);
						int sides = Integer.parseInt(pieces[1]);
						System.out.println("Num Dice: " + numDice + "\nSides: " + sides);
						
						Die die = new Die(sides);
						EmbedBuilder rollBuild = new EmbedBuilder();
						rollBuild.setDescription("Results of rolling **"+numDice+"d"+sides+"**\n\n");
						int[] results = die.roll(numDice);
						int sum = 0;
						try
						{
							for(int i = 0; i < results.length; i++)
							{
								rollBuild.appendDescription("Roll " + i + ": " + results[i] + "\n");
								sum += results[i];
							}
							rollBuild.appendDescription("\n**Sum**: " + sum);
						} catch(IllegalArgumentException iae)
						{
							System.err.println(iae.getMessage());
							System.err.println("Reseting the EmbedBuilder's description and only showing the sum of the rolls!");
							rollBuild.setDescription(null);	// Reset the Embed's Description
							rollBuild.setDescription("Results of rolling **"+numDice+"d"+sides+"**\n\n");
							rollBuild.appendDescription("**Too many results to show, so here's the sum**\n");
							rollBuild.appendDescription("\n**Sum**: " + sum);
						}
						sendEmbed(e, rollBuild);
					} else if(rawMsg.startsWith(PRECURSOR+"character"))
					{
						EmbedBuilder characterBuild = new EmbedBuilder();
						long key = e.getAuthor().getIdLong();
						if(playerCharacters.containsKey(key))
						{
							characterBuild.setDescription(playerCharacters.get(key).toString());
						} else
						{
							characterBuild.setDescription("Character not found for " + key);
						}
						sendEmbed(e, characterBuild);
					}
					switch(rawMsg)	//Every command case
					{
						case PRECURSOR+"hello":
								System.out.println(">> "+e.getAuthor().getName()+" requested hello!");
								sendMessage(e, e.getAuthor().getAsMention()+" Hello!");
								break;
						case PRECURSOR+"guessingGame":
							if(currentGame == null)
							{
									isGame = true;
									System.out.println(">> "+e.getAuthor().getName()+" requested Guessing Game!");
									currentGame = new GuessingGame("Guessing Game", GameCategory.GUESS);
									System.out.println("Now Playing: "+currentGame);
									sendMessage(e, "Guess the number!");
							} else
								sendMessage(e, "Game is already running!");
							break;
						case PRECURSOR+"war":
							if(currentGame == null)
							{
								isGame = true;
								System.out.println(">> "+e.getAuthor().getName()+" requested War Game!");
								currentGame = new WarGame("War Game", GameCategory.CARD);
								System.out.println("Now Playing: "+currentGame);
								currentGame.play(e);
								currentGame = null;
								isGame = false;
							}
							else
								sendMessage(e, "Game is already running!");
							break;
						case PRECURSOR+"ping":
							commandPing(e);
							break;
						case PRECURSOR+"help":
							System.out.println(">> "+e.getAuthor().getName()+" requested Help!");
							commandHelp(e);
							break;
						case PRECURSOR+"shutdown":
							System.out.println(">> "+e.getAuthor().getName()+" requested shutdown!");
							commandShutdown(e);
							break;
						case PRECURSOR+"guilds":
							guilds = RiasGremoryBot.getGuilds();
							EmbedBuilder guildsBuilder = new EmbedBuilder();
							guildsBuilder.setTitle("I Am Currently in " + RiasGremoryBot.getNumGuilds() + " Guilds!", "https://riasgremorybot.epizy.com");
							for(Guild g : guilds)
							{
								guildsBuilder.appendDescription(g.getName() + "\n");
							}
							sendEmbed(e, guildsBuilder);
							break;
						case PRECURSOR+"invite":
							sendMessage(e, "https://discordapp.com/oauth2/authorize?&client_id=288558232991956992&scope=bot");
							break;
						case PRECURSOR+"skip":
							if(!RiasAudioListener.hasTrack(e.getTextChannel()))
							{
								sendMessage(e, "There are no more tracks left! Closing audio stream...");
								RiasAudioManager musicManager = RiasAudioListener.getGuildAudioPlayer(e.getGuild());
								musicManager.player.stopTrack();
								e.getGuild().getAudioManager().closeAudioConnection();
							}
							else
								RiasAudioListener.skipTrack(e.getTextChannel());
							break;
						case PRECURSOR+"pause":
							RiasAudioListener.pauseTrack(e.getTextChannel());
							sendMessage(e, "Pausing current track...");
							break;
						case PRECURSOR+"resume":
							RiasAudioListener.resumeTrack(e.getTextChannel());
							sendMessage(e, "Resuming track...");
							break;
						case PRECURSOR+"stop":
							RiasAudioListener.stopTrack(e.getTextChannel());
							sendMessage(e, "Closing audio stream...");
							e.getGuild().getAudioManager().closeAudioConnection();
							break;
						case PRECURSOR+"play":
							sendMessage(e, "Make sure to enter a Youtube URL!");
							break;
						case PRECURSOR+"np":
						case PRECURSOR+"nowPlaying":
							if(RiasAudioListener.getGuildAudioPlayer(currentGuild).player.getPlayingTrack() != null)
								sendMessage(e, RiasAudioListener.getGuildAudioPlayer(currentGuild).player.getPlayingTrack().getInfo().title + " Duration: "
											 + RiasAudioListener.getGuildAudioPlayer(currentGuild).player.getPlayingTrack().getDuration() / 1000);
							else
								sendMessage(e, "Nothing is playing!");
							break;
						case PRECURSOR+"queue":
							EmbedBuilder embed = new EmbedBuilder();
							embed.setTitle("Queued Songs:", null);
							embed.setDescription(RiasAudioListener.getGuildAudioPlayer(currentGuild).scheduler.getList());
							e.getChannel().sendMessage(embed.build()).queue();
							break;
						case PRECURSOR+"create":
							System.out.println(">> Create new permission file for guild " + currentGuild.getId());
							permManager.createPermissionFile(currentGuild);
							System.out.println(">> Permission file created!");
							sendMessage(e, "Permission File created!");
							break;
						case PRECURSOR+"check":
							permManager.updatePermissionFile(currentGuild, "Hi there! I just wrote to a file!");
							System.out.println(">> Updated permission file for guild " + currentGuild);
							break;
						case PRECURSOR+"createJoey":
							sendMessage(e, "==CreateCustom~GetJoey~Echo @joey101937#6532 Get in here!");
							break;
						case PRECURSOR+"version":
							EmbedBuilder versionBuild = new EmbedBuilder();
							versionBuild.setDescription("**Version:** " + RiasGremoryBot.getVersion() + "\n");
							versionBuild.appendDescription("***Last Implemented:*** " + RiasGremoryBot.getLastImplemented() + "\n");
							sendEmbed(e, versionBuild);
							break;
/* ROLEPLAYING COMMANDS */
					} 
				}
			}  catch(StringIndexOutOfBoundsException siobe)
			{
				System.out.println("Can't take substring of message");
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
