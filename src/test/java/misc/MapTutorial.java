package misc;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;

public class MapTutorial {

	static Map<String, Integer> myMap = new HashMap<String, Integer>();
	static String name1 = "joey";
	static String name2 = "knife";
	static String name3 = "brom";
	
	static int joeysSuperSecretID = 1;
	static int knifesMildlyConfidentialID = 2;
	static int bromsID = 3;
	
	public static void main(String[] args)
	{
		// now we want all the names to link to the ID of that person, so we populate it like this:
		myMap.put(name1, joeysSuperSecretID);	//Now, name1 ("joey") maps to joeySuperSecretID
		myMap.put(name2, knifesMildlyConfidentialID);
		myMap.put(name3, bromsID);
		
		// Now to get the stored value
		out.println("ID for " + name1 + " is " + myMap.get(name1));
		out.println("ID for " + name2 + " is " + myMap.get(name2));
		out.println("ID for " + name3 + " is " + myMap.get(name3));
		
	}
	
}
