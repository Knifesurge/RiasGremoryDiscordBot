package com.knifesurge.riasgremory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.knifesurge.riasgremory.music.RiasAudioListener;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.managers.Presence;

public class RiasGremoryBot {

	private static JDA jda;
	private static String version;
	private static String BOT_TOKEN;
	private static String lastImplemented;
	private static long startTime;
	private List<File> files = new ArrayList<File>();
	
	public static void main(String[] args)
	{
		try
		{
			BOT_TOKEN = args[0];
			version = "1.3";
			lastImplemented = "do ;>guilds!\nCurrency is now (somewhat) implemented! Check ;>help!";
			jda = new JDABuilder(AccountType.BOT).setToken(BOT_TOKEN).buildBlocking();
			jda.addEventListener(new RiasGremoryListener());
			jda.addEventListener(new RiasAudioListener());
			Presence game = jda.getPresence();
			game.setGame(Game.of(GameType.DEFAULT, ";>help"));
/*			javax.swing.SwingUtilities.invokeLater(
				new Runnable()
				{
					public void run()
					{
						RiasGremoryListener.setUpGUI();
					}
				}
			);*/
			startTime = System.nanoTime();
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static long getStartTime()
	{
		return startTime;
	}
	
	public static List<Guild> getGuilds()
	{
		return jda.getGuilds();
	}
	
	public static int getNumGuilds()
	{
		return jda.getGuilds().size();
	}
	
	public static String getVersion()
	{
		return version;
	}
	
	public static String getLastImplemented()
	{
		return lastImplemented;
	}
	
	public static JDA getJDA()
	{
		return jda;
	}
	
}
