package com.knifesurge.riasgremory.currency;

import java.io.Serializable;

import com.knifesurge.riasgremory.RiasGremoryListener;

public class Profile implements Serializable
{

	private static final long serialVersionUID = 71058813905990616L;
	
	private long credits;
	private long memberID;
	
	public Profile(long id)
	{
		this.memberID = id;
		credits = 0;
	}
	
	public long getCredits()
	{
		return credits;
	}
	
	public void addCredits(long amt)
	{
		credits += Math.floor(amt);
	}
	
	public void setBalanceEmpty()
	{
		credits = 0;
	}
	
	@Override
	public String toString()
	{
		return RiasGremoryListener.currentGuild.getMemberById(memberID).getEffectiveName() + " with a balance of " + credits + " credits.";
	}
	
}
