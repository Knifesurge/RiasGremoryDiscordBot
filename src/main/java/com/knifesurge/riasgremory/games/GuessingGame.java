package com.knifesurge.riasgremory.games;

import com.knifesurge.riasgremory.commands.games.Game;
import com.knifesurge.riasgremory.commands.games.GameCategory;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class GuessingGame extends Game{
	
	private static int answer;
	
	public GuessingGame(String n, GameCategory gc)
	{
		super(n, gc);
	}
	
	public static boolean check(MessageReceivedEvent e, String s)
	{
		answer = (int)((10 * Math.random()) + 1);
		System.out.println(">> Number is " + answer);
		e.getChannel().sendMessage("Number is "+answer).queue();
		if(Integer.parseInt(s) == answer)
			return true;
		else
			return false;
	}

	@Override
	public void play(MessageReceivedEvent e) {}
	
	@Override
	public void playAgain(MessageReceivedEvent e) {}
}
