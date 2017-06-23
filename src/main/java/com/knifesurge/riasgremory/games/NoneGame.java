package com.knifesurge.riasgremory.games;

import com.knifesurge.riasgremory.commands.games.Game;
import com.knifesurge.riasgremory.commands.games.GameCategory;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class NoneGame extends Game{

	public NoneGame(String n, GameCategory gc) {
		super(n, gc);
	}

	@Override
	public void play(MessageReceivedEvent e) {}

	@Override
	public void playAgain(MessageReceivedEvent e) {}

}
