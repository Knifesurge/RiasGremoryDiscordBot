package com.knifesurge.riasgremory.roleplaying;

public class Die
{

	/*
	 * Main D&D dice:
	 * d4, d6, d8, d10, d12, d20, d100
	 */
	private int sides;
	
	public Die(int sides)
	{
		this.sides = sides;
	}
	
	/**
	 * Rolls the die <code>times</code> amount of times, adding each result to an array of ints. This array is returned once the Die 
	 * is rolled the correct amount of times
	 * @param times - Number of times to roll the dice
	 * @return an array of integers holding each roll result
	 */
	public int[] roll(int times)
	{
		int sum = 0;	// The sum of the dice
		int[] results = new int[times];	// Holds the results of each die roll
		for(int i = 0; i < times; i++)
		{
			results[i] = (int)((sides - 1) * Math.random() + 1);
			sum += results[i];
			System.out.println("Roll " + i + ": " + results[i]);
		}
		System.out.println(sum);
		return results;
	}
	
	public int getSides()
	{
		return sides;
	}
	
}
