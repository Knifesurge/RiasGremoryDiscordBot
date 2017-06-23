package com.knifesurge.riasgremory.commands.games;

public enum GameCategory {

	GUESS("Guessing"), CARD("Card"), NONE("None");
	
	String name;
	
	GameCategory(String n)
	{
		name = n;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
}
