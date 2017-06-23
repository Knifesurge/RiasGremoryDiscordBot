package com.knifesurge.riasgremory.commands;

import com.knifesurge.riasgremory.permissions.PermissionLevel;

public abstract class Command {

	protected String name;
	protected CommandType type;
	protected PermissionLevel permLevel;
	
	public String getName()
	{
		return name;
	}
	
	public CommandType getType()
	{
		return type;
	}
	
	public PermissionLevel getPermLevel()
	{
		return permLevel;
	}
	
	public abstract void evalCommand();
}
