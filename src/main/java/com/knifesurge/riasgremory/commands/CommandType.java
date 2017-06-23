package com.knifesurge.riasgremory.commands;

public enum CommandType {
	GAME("Game"), GENERIC("Generic"), ADMIN("Admin"), MOD("Moderator"), MISC("Miscellaneous"), MUSIC("Audio");
	
	String type;
	
	CommandType(String type)
	{
		this.type = type;
	}
	
	public String getType()
	{
		return type;
	}
}
