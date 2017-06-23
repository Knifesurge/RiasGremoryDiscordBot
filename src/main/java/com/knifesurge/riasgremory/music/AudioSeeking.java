package com.knifesurge.riasgremory.music;

public enum AudioSeeking {
	START, MIDDLE, END;
	
	long value;
	
	public void setValue(int v)
	{
		value = v;
	}
}
