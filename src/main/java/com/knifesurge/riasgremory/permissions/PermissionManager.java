package com.knifesurge.riasgremory.permissions;

import static java.lang.System.out;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.knifesurge.riasgremory.utils.FileManager;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

public class PermissionManager {
	
	private FileManager fileManager;
	
	private Map<Guild, File> guildPermFile = new HashMap<Guild, File>();	//Map a Permission File to each Guild
	private Map<Member, PermissionLevel> permLevel = new HashMap<Member, PermissionLevel>();	//Map a Permission level to each member in the Guild
	
	private static final String datFolder = "D:\\Programming\\Java\\DiscordBots\\RiasGremoryDiscordBot\\dat\\";
	
	public void createPermissionFile(Guild g)
	{
		try
		{
			File permFolder = new File(datFolder+g.getId());	//Permission folder for the Guild g
			File permFile = new File(permFolder, "\\perms.pm");	// Actual Permission File for Guild g
			boolean creation = permFolder.mkdirs();	//Create any missing directories
			if(!permFile.exists())	//Check to make sure that the file doesn't already exist
			{
				if(creation)	//If the missing directories were successfully created
				{
					permFile.createNewFile();	//Create a new empty file
					out.println("Permission File initialized");	//DEBUG
				}
				else	//If the missing directories were not created
				{
					out.println("Directory creation failed!");
					return;
				}
				guildPermFile.put(g, permFile);	//Map the file to the Guild
				
				fileManager.writeFile(permFile, "Initializer", false);	//Write to the file
				out.println("Wrote to the permission file for guild " + g.getId() + " and mapped them!");
				
			} else	//File exists!
			{
				out.println("Permisison file " + permFile + " already exists!");
			}
		} catch(IOException ioe)
		{
			out.println("Error creating the perm file!");
			System.err.println(ioe.getMessage());
		}
	}
	
	public File getPermissionFile(Guild g)
	{
		File permFile = new File(this.getClass().getClassLoader().getResource(File.separatorChar+"dat"+File.separatorChar+g.getId()+File.separatorChar+"perms.txt").getFile());
		return permFile;
	}

	public void addMember(Member m, PermissionLevel permL)
	{
		permLevel.put(m, permL);
	}
	
	public void updatePermissionFile(Guild g, String changes)
	{
		File permFile = getPermissionFile(g);
		fileManager.writeFile(permFile, fileManager.readFile(guildPermFile.get(g)) + changes, false);
	}
	
	public void savePermissionFile(Guild g)
	{
		File permFile = getPermissionFile(g);
		fileManager.writeFile(permFile, fileManager.readFile(guildPermFile.get(g)), true);
	}
	
}
