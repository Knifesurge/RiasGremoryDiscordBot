package com.knifesurge.riasgremory;

import com.knifesurge.riasgremory.music.RiasAudioListener;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.managers.Presence;

public class RiasGremoryBot {

	public static JDA jda;
	
	private static String BOT_TOKEN;
	
	public static void main(String[] args)
	{
		try
		{
			BOT_TOKEN = args[0];
			jda = new JDABuilder(AccountType.BOT).setToken(BOT_TOKEN).buildBlocking();
			jda.addEventListener(new RiasGremoryListener());
			jda.addEventListener(new RiasAudioListener());
			Presence game = jda.getPresence();
			game.setGame(Game.of(";>help"));
/*			javax.swing.SwingUtilities.invokeLater(
				new Runnable()
				{
					public void run()
					{
						RiasGremoryListener.setUpGUI();
					}
				}
			);*/
		} catch (Exception e) {e.printStackTrace();}
	}
	
}
