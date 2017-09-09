package com.knifesurge.riasgremory.roleplaying.characters;

public class AbilityScore
{

	private int modifier;
	private int value;
	
	public AbilityScore(int value)
	{
		this.value = value;
		createModifier();
	}
	
	private void createModifier()
	{
		modifier = Math.floorDiv(this.value - 10, 2);
	}
	
	public int getModifier()
	{
		return modifier;
	}
	
	public int getValue()
	{
		return value;
	}
}
