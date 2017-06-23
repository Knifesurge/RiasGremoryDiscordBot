package com.knifesurge.riasgremory.commands.games;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class Game {

	private String name;
	private GameCategory category;
	protected String prevMsgID = "";
	protected String currMsgID;
	
	protected String input;
	
	
	public Game(String n, GameCategory gc)
	{
		name = n;
		category = gc;
	}
	
	public String getName()
	{
		return name;
	}
	
	public GameCategory getCategory()
	{
		return category;
	}
	
	@Override
	public String toString()
	{
		return category+": "+name;
	}
	
	public abstract void play(MessageReceivedEvent e);
	public abstract void playAgain(MessageReceivedEvent e);
}
