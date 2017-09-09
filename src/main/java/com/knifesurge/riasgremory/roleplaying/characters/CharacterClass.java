package com.knifesurge.riasgremory.roleplaying.characters;

public enum CharacterClass
{

	WARLOCK("Warlock");
	
	private String name;
	
	CharacterClass(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
	
}
