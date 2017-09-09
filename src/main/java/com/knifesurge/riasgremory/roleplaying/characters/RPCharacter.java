package com.knifesurge.riasgremory.roleplaying.characters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knifesurge.riasgremory.roleplaying.characters.items.Item;
import com.knifesurge.riasgremory.roleplaying.characters.spells.Spell;

/**
 * A Character is any <b>player controlled</b> character. Each Character has various attributes related to said Character. These include:<br>
 * <ul style="list-style-type:disc">
 * 		<li> A <i>name</i>, represented by a String
 * 		<li> A <i>level</i>, represented by an Integer
 * 		<li> The <i>Experience Points</i> that the Character has, represented by an Integer
 * 		<li> A <i>Class</i> that the Character is part of, represented by the {@linkplain CharacterClass} enum.
 * 		<li> A <i>Race</i> that the Character belongs to, represented by the {@linkplain Race} enum.
 * 		<li> A <i>Background</i> that defines how the Character reacts to different events, among other things. Represented by the {@linkplain Background} class
 * 		<li> An <i>Alignment</i> that helps to define how the Character behaves. Represented by the {@linkplain Alignment} class
 * 		<li> A <i>speed</i> in feet, represented by an Integer
 * 		<li> <i>Hit Points</i>, all of which are represented as an Integer:
 * 			<ul>
 * 				<li> The maximum amount of Hit Points for the Character.
 * 				<li> The Character's current amount of Hit Points.
 * 				<li> The Character's temporary amount of Hit Points.
 * 			</ul>
 * 		<li> <i>Ability Scores</i>, all of which are represented by the {@linkplain AbilityScore} class
 * 			<ul>
 * 				<li> Strength (STR): Measures natural athleticism and bodily power. 
 * 				<li> Dexterity (DEX): Measures physical agility, reflexes, balance, and poise.
 * 				<li> Constitution (CON): Measures health, stamina, and vital force.
 * 				<li> Intelligence (INT): Measures mental acuity, information recall, and analytical skill.
 * 				<li> Wisdom (WIS): Measures awareness, intuition, and insight.
 * 				<li> Charisma (CHA): Measures confidence, eloquence, and leadership.
 * 			</ul>
 * 		<li> Various <i>Skills</i> that the Character knows, represented by the {@linkplain Skills} class.
 * 		<li> Various <i>Attacks</i> that the Character knows, represented by a {@linkplain List} of {@linkplain Attack}s.
 * 		<li> Any <i>Spells</i> that the Character might know, represented by a {@linkplain List} of {@linkplain Spell}s.
 * 		<li> Any <i>Equipment</i> that the Character has, represented by a {@linkplain List} of {@linkplain Item}s.
 * 		<li> Any <i>Traits</i> that the Character has, represented by a {@linkplain List} of {@linkplain Trait}s.
 * 		<li> Any <i>Languages</i> that the Character knows, represented by a {@linkplain List} of Strings.
 * 		<li> Any <i>Proficiences</i> that the Character has, represented by a {@linkplain List} of {@linkplain Proficiency}.
 * </ul>
 * @author Knifesurge
 */
public class RPCharacter 
{

	private String name;
	private int level;
	private int xp;
	private CharacterClass charClass;
	private Race race;
	private Background background;
	private Alignment alignment;
	
	private int speed;
	private int maxHitPoints;
	private int currentHitPoints;
	private int temporaryHitPoints;
	
	/* Ability Scores */
	private Map<String, AbilityScore> abilityScores = new HashMap<String, AbilityScore>();
	
	/* Skills */
	private Skills skills;
	
	/* Attacks & Spellcasting */
	private List<Attack> attacks = new ArrayList<Attack>();
	private List<Spell> spells = new ArrayList<Spell>();
	
	/* Equipment */
	private List<Item> equipment = new ArrayList<Item>();
	
	/* Features & Traits */
	private List<Trait> traits = new ArrayList<Trait>();
	
	/* Proficiences & Languages */
	private List<String> languages = new ArrayList<String>();
	private List<Proficiency> proficiencies = new ArrayList<Proficiency>();
	
	public RPCharacter(String name)
	{
		this.name = name;
	}
	
	
	/**
	 * <strong>Constructor</strong><br><br>
	 * A full constructor to create a Character. This constructor takes everything that a character needs to be fully complete. This will most likely never be used, but its here just in case
	 * @param name - Name of the Character
	 * @param level - The Character's starting level
	 * @param charClass - The Character's {@linkplain CharacterClass} class
	 * @param race - The Character's {@linkplain Race}
	 * @param background - The {@linkplain Background} of the Character
	 * @param alignment - The {@linkplain Alignment} of the Character
	 * @param speed - The Character's speed, in feet
	 * @param maxHitPoints - The Character's max and starting Hit Points
	 * @param abilityScores - A {@linkplain Map} of the Character's Ability Scores. The key of the Map is the Ability Score in capitalized shortened form (STR for Strength, INT for Intelligence, etc) to get
	 *  the {@linkplain AbilityScore} value.
	 * @param skills - The Character's {@linkplain Skills}
	 * @param attacks - A {@linkplain List} of the Character's various {@linkplain Attack}s
	 * @param spells - A {@linkplain List} of the Character's various {@linkplain Spell}s.
	 * @param equipment - A {@linkplain List} of the {@linkplain Item}s that the Character has
	 * @param traits - A {@linkplain List} of the Character's various {@linkplain Trait}s.
	 * @param languages - A {@linkplain List} of all the languages that the Character knows. A Language is represented by a String of the name of the Language}
	 * @param proficiences - A {@linkplain List} of any {@linkplain Proficiency} that the Character has.
	 */
	public RPCharacter(String name, int level, CharacterClass charClass, Race race, Background background, Alignment alignment, int speed, int maxHitPoints, Map<String, AbilityScore> abilityScores, Skills skills,
					 List<Attack> attacks, List<Spell> spells, List<Item> equipment, List<Trait> traits, List<String> languages, List<Proficiency> proficiences)
	{
		this.name = name;
		this.level = level;
		this.charClass = charClass;
		this.race = race;
		this.background = background;
		this.alignment = alignment;
		this.speed = speed;
		this.maxHitPoints = maxHitPoints;
		this.currentHitPoints = this.maxHitPoints;
		this.abilityScores = abilityScores;
		this.skills = skills;
		this.attacks = attacks;
		this.spells = spells;
		this.equipment = equipment;
		this.traits = traits;
		this.languages = languages;
		this.proficiencies = proficiences;
	}


	public String getName() {
		return name;
	}


	public int getLevel() {
		return level;
	}


	public int getXp() {
		return xp;
	}


	public CharacterClass getCharClass() {
		return charClass;
	}


	public Race getRace() {
		return race;
	}


	public Background getBackground() {
		return background;
	}


	public Alignment getAlignment() {
		return alignment;
	}


	public int getSpeed() {
		return speed;
	}


	public int getMaxHitPoints() {
		return maxHitPoints;
	}


	public int getCurrentHitPoints() {
		return currentHitPoints;
	}


	public int getTemporaryHitPoints() {
		return temporaryHitPoints;
	}


	public Map<String, AbilityScore> getAbilityScores() {
		return abilityScores;
	}


	public Skills getSkills() {
		return skills;
	}


	public List<Attack> getAttacks() {
		return attacks;
	}


	public List<Spell> getSpells() {
		return spells;
	}


	public List<Item> getEquipment() {
		return equipment;
	}


	public List<Trait> getTraits() {
		return traits;
	}


	public List<String> getLanguages() {
		return languages;
	}


	public List<Proficiency> getProficiencies() {
		return proficiencies;
	}
	
	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		str.append("Name: " + name);
		if(race != null)
			str.append("\nRace: " + race);
		if(charClass != null)
			if(level != 0)
				str.append("\nClass & Level: " + charClass + " " + level);
			else
			{
				this.level = 0;
				str.append("\nClass & Level: " + charClass + " " + level);
			}
		if(xp != 0)
			str.append("\nExperience Points: " + xp);
		else
		{
			xp = 0;
			str.append("\nExperience Points: " + xp);
		}
		if(background != null)
			str.append("\nBackground: " + background);
		if(alignment != null)
			str.append("\nAlignment: " + alignment);
		if(!abilityScores.isEmpty())
		{
			str.append("\nAbility Scores:");
			str.append("\nSTR: " + abilityScores.get("STR") + ", DEX: " + abilityScores.get("DEX") + ", CON: " + abilityScores.get("CON")
					 + ", INT: " + abilityScores.get("INT") + ", WIS: " + abilityScores.get("WIS") + ", CHA: " + abilityScores.get("CHA"));
		}
		return str.toString();
	}
}
