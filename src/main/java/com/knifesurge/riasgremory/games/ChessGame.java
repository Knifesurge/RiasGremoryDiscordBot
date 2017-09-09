package com.knifesurge.riasgremory.games;

import com.knifesurge.riasgremory.commands.games.Game;
import com.knifesurge.riasgremory.commands.games.GameCategory;
import com.knifesurge.riasgremory.games.chess.Board;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ChessGame extends Game {

	public ChessGame(String n, GameCategory gc) {
		super(n, gc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void play(MessageReceivedEvent e)
	{
		Board board = new Board();
		board.initialize();
	}

	@Override
	public void playAgain(MessageReceivedEvent e) {
		// TODO Auto-generated method stub

	}

}
