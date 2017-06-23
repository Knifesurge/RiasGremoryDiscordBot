package com.knifesurge.riasgremory.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

	private BufferedReader read;
	
	private String line;
	
	private BufferedWriter write;
	
	/**
	 * Read a File and get the contents of the file as a single String
	 * @param file - The File to read from
	 * @return String - contents of the file
	 */
	public String readFile(File file)
	{
		try
		{
			read = new BufferedReader(new FileReader(file));
			String content = "";
			while((line = read.readLine()) != null)
			{
				content.concat(line+"\n");
			}
			read.close();
			return content;
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Writes the specified String content to the specified File file
	 * @param file - The File to write to
	 * @param content - The content to write to the File
	 * @param append - Whether or not to add the content to the end of any contents that might already be in the File
	 */
	public void writeFile(File file, String content, boolean append)
	{
		try {
			write = new BufferedWriter(new FileWriter(file, append));
			write.write(content);
			write.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
