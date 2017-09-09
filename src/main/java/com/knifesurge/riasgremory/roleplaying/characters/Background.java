package com.knifesurge.riasgremory.roleplaying.characters;

public class Background
{

	private String name;
	private String description;
	
	public Background(String name)
	{
		this.name = name;
	}
	
	public Background(String name, String description)
	{
		this.name = name;
		this.description = description;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		str.append(getName() + "\n");
		if(description != null)
			str.append(getDescription() + "\n");
		return str.toString();
	}
	
}
