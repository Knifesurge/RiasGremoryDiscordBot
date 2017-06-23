package com.knifesurge.riasgremory.games;

import com.knifesurge.riasgremory.RiasGremoryListener;
import com.knifesurge.riasgremory.commands.games.Game;
import com.knifesurge.riasgremory.commands.games.GameCategory;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class WarGame extends Game
{
	public WarGame(String n, GameCategory gc)
	{
		super(n, gc);
	}

	private static final int MAX_HAND_SIZE = 52;	//Max hand size for a Hand
	
	 Deck deck;	//The initial Deck that holds all the cards that will be used in the game
	
	 int numRounds = 0;	//How long the game was, in rounds
	 int numWar = 0;		//How many wars happened during the game
	 int numGames = 1;	//Always plays at least 1 game
	
	 boolean bailed = false;	//Whether or not we had to bail out of the game because there were too many rounds passed (stop infinite games)
	 boolean playing = true;	//Whether or not we are playing the game
	
	 Hand player;	//Player's hand
	 Hand computer;	//Computer's hand
	 Hand pot = new Hand("war",MAX_HAND_SIZE);	//Holds all the Cards that are played during a War
	
	 private String name = "";
	
	/**
	 * Creates a new Deck, shuffles the Deck, then creates a Player hand and a Computer hand, filling both with half the Deck size (26)
	 */
	private void initializeGame(MessageReceivedEvent e)
	{
		deck = new Deck();
		deck.shuffle();
		
		/* Reinitialize variables that could have changed during the game */
		bailed = false;
		numRounds = 0;
		numWar = 0;
		
		if(numGames == 1)
		{
			name = e.getAuthor().getName();
			player = new Hand(name, MAX_HAND_SIZE);
		} else
		{
			player = new Hand(name, MAX_HAND_SIZE);
		}
		
		for(int i=0;i<26;i++)
			player.addCard(deck.drawCard());
		
		computer = new Hand("Hal", MAX_HAND_SIZE);
		for(int i=0;i<26;i++)
			computer.addCard(deck.drawCard());
		
	}
	
	/**
	 * Plays a round of War. Both players play a card. Cards are compared and whoever has the higher card takes the other's played card
	 */
	private void playRound()
	{
		
		Card a = player.playCard();	//Player's played card
		Card b = computer.playCard();	//Computer's played card
		
		switch(a.compareTo(b))
		{
			case -1:	//Player's card is less than Computer's card
				computer.addCard(a);
				computer.addCard(b);
				break;
			case 0:		//Cards are equal
				playWar();
				break;
			case 1:		//Player's card is greater than Computer's card
				player.addCard(a);
				player.addCard(b);
				break;
		}
		numRounds++;
	}
	
	/**
	 * Plays a war, where each player plays 3 cards, showing the last one. Whoever has the higher card value wins all of the cards
	 */
	private void playWar()
	{
		
		/* Since both players need to play 3 cards, if they do not have enough, they automatically lose */
		if(player.size() < 3)
		{
			player.makeEmpty();	//Triggers a game over
		} else if(computer.size() < 3)
		{
			computer.makeEmpty();	//Triggers a game over
		} else	//Both players have enough cards
		{
			for(int i=0;i<2;i++)	//Add two Cards
				pot.addCard(player.playCard());
			
			for(int i=0;i<2;i++)	//Add two Cards
				pot.addCard(computer.playCard());
			
			//Add the other Cards that will be compared to determine the winner for the War
			pot.addCard(computer.playCard());
			pot.addCard(player.playCard());
			
			switch(pot.peekCard().compareTo(pot.getAt(1)))
			{
				case -1:	//Player's card is less than the Computer's
					while(!pot.isEmpty())	//Add all cards in the pot to the computer's winnings
						computer.addCard(pot.playCard());
					break;
				case 0:		//Player's card and Computer's card are the same, another war ensues
					playWar();
					break;
				case 1:		//Player's card is greater than the Computer's
					while(!pot.isEmpty())	//Add all cards in the pot to the computer's winnings
						player.addCard(pot.playCard());
					break;
			}
			numWar++;
		}
	}
	
	/**
	 * Checks whether or not the game is over
	 * @return	true if either the Player or the Computer's hand is empty, false otherwise
	 */
	private boolean isGameOver()
	{
		return player.isEmpty() || computer.isEmpty();
	}
	
	/**
	 * Prints out who won the game (based on who's hand is empty
	 */
	private void getWinner(MessageReceivedEvent e)
	{
		if(bailed)
			RiasGremoryListener.sendMessage(e, "Game went on for too long, so the player's decided to call it a tie");
		else if(player.isEmpty())	//Computer won
			RiasGremoryListener.sendMessage(e, computer.getName() + " won in " + numRounds + " rounds, with " + numWar + " wars!");
		else
			RiasGremoryListener.sendMessage(e, name + " won in " + numRounds + " rounds, with " + numWar + " wars!");
	}
	
	/**
	 * Play the game
	 */
	public void play(MessageReceivedEvent e)
	{
		if(numGames == 1)
		{
			initializeGame(e);
			
			System.out.println("Starting game 1 ...");
		}
		
		if(numGames > 1)	//Every game past the first
			initializeGame(e);
		while(!isGameOver() && !bailed)
		{
			if(numRounds >= 3500)
				bailed = true;
			playRound();
		}
		numGames++;
		
		while(playing)
		{
			getWinner(e);
			playAgain(e);
		}
		
	}

	@Override
	public void playAgain(MessageReceivedEvent e) { playing = false; }
}

enum Suit
{
	NULL, SPADES, HEARTS, CLUBS, DIAMONDS;
}

enum Face
{
/* ACES ARE HIGH */
	
	NULL(0), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13), ACE(14);
	
	private int value;
	
	/**
	 * Constructor
	 * @param val Value of the card
	 */
	Face(int val)
	{
		value = val;
	}
	
	/**
	 * Used to assign the correct Face when creating the cards
	 * @param value - integer representation of the face
	 * @return Face - that represents what face it should be. Defaults to null
	 */
	public static Face getFace(int value)
	{
		switch(value)
		{
			case 2:
				return TWO;
			case 3:
				return THREE;
			case 4:
				return FOUR;
			case 5:
				return FIVE;
			case 6:
				return SIX;
			case 7:
				return SEVEN;
			case 8:
				return EIGHT;
			case 9:
				return NINE;
			case 10:
				return TEN;
			case 11:
				return JACK;
			case 12:
				return QUEEN;
			case 13:
				return KING;
			case 14:
				return ACE;
			default:
				return NULL;
		}
	}
	
	/**
	 * Gets the value that the Face is worth (2 for TWO, 3 for THREE, etc)
	 * @return
	 */
	public int getValue()
	{
		return value;
	}
}

class Card implements Comparable<Card>{

	Suit suit;
	Face face;
	
	/**
	 * Constructor to create a new Card.
	 * @param s	Suit of the Card
	 * @param f Face of the Card
	 */
	public Card(Suit s, Face f)
	{
		suit = s;
		face = f;
	}
	
	/**
	 * Returns this Card's Suit
	 * @return The Suit of this Card
	 */
	public Suit getSuit()
	{
		return suit;
	}
	
	/**
	 * Returns this card's Face
	 * @return The Face of this Card
	 */
	public Face getFace()
	{
		return face;
	}
	
	/**
	 * Returns this object represented as a String.
	 */
	@Override
	public String toString()
	{
		return getSuit() + ": " + getFace(); 
	}

	/**
	 *	Compares this object with another object to see if they are equal.
	 *	@return True if they have same face and suit, false otherwise
	 */
	@Override
	public boolean equals(Object obj)
	{
		Card card = (Card) obj;
		if(getSuit() == card.getSuit())
			if(getFace() == card.getFace())
				return true;
		return false;
	}

	/**
	 * UNUSED
	 */
	@Override
	public int compareTo(Card card) {
		if(getFace().getValue() > card.getFace().getValue())
			return 1;										//This Card is greater than the specified Card
		if(getFace().getValue() < card.getFace().getValue())
			return -1;										//This Card is less than the specified Card
		else
			return 0;										//Cards are equal 

	}
}

class Queue
{
	private Card[] data;
	private int front, rear;
	private int maxSize;
	
	/**
	 * Constructor
	 */
	public Queue(int maxItems)
	{
		data = new Card[maxItems];
		front = -1;
		rear = -1;
		maxSize = maxItems;
	}
	
	/**
	 * Returns the front item without removing it from the queue.
	 * @return Card at the front of the queue
	 */
	public Card front()
	{
		return data[front];
	}
	
	/**
	 * Returns the item at the specified index without removing it from the queue.
	 * @return Card at the specified index
	 */
	public Card getAt(int index)
	{
		return data[index];
	}
	
	public void add(int index, Card obj)
	{
		data[index] = obj;
	}
	
	/**
	 * Removes the front item from the queue and returns it
	 * @return Card at the front of the queue
	 */
	public Card dequeue()
	{
		Card num;
		
		num = data[front];	//Get front item
		//if dequeuing last item make the queue empty
		if(front == rear)
			makeEmpty();
		else	//Move pointer to next item
			front = (front + 1) % maxSize;
		
		return num;
	}
	
	/**
	 * Adds an item to the queue if there is room
	 */
	public void enqueue(Card card)
	{
		if(isEmpty())	//First item queued
		{
			rear = 0;
			front = 0;
			data[rear] = card;
		} else
		{
			rear = (rear + 1) % maxSize;
			data[rear] = card;
		}
	}
	
	/**
	 * Determines if there are items on the queue
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty()
	{
		if(front == -1 && rear == -1)
			return true;
		else
			return false;
	}
	
	/**
	 * Returns the number of items in the queue
	 * @return int representation of how many items are in the queue
	 */
	public int size()
	{
		if(isEmpty())
			return 0;
		else	
			if(rear > front)			//Front item is "in front" of rear item
				return rear - front + 1;
			else if(front == rear + 1)	//Queue is full
				return maxSize;
			else						//front item is "behind" rear item
				return front - rear + 1;
	}
	
	/**
	 * Empties the queue
	 */
	public void makeEmpty()
	{
		front = -1;
		rear = -1;
	}
}

class Hand
{
	private String name = "hand";
	private Queue hand;
	public int MAX_SIZE;
	
	public Hand(String n, int startSize)
	{
		name = n;
		hand = new Queue(startSize);
		MAX_SIZE = startSize;
	}
	
	/**
	 * Returns the Card at the 'front' of the hand, but doesn't remove it
	 * @return Card at the 'front' of the hand
	 */
	public Card peekCard()
	{
		return hand.front();
	}
	
	/**
	 * Adds a Card object to the bottom of the hand
	 * @param c	-	Card object to add to the bottom of the hand
	 */
	public void addCard(Card c)
	{
		hand.enqueue(c);
	}
	
	/**
	 * Returns the Card at the 'top' of the hand
	 * @return	Card object at the 'top' of the hand
	 */
	public Card playCard()
	{
		return hand.dequeue();
	}
	
	/**
	 * Returns how many Cards are in the hand
	 * @return int representing how many cards are in the hand
	 */
	public int size()
	{
		return hand.size();
	}
	
	public Card getAt(int i)
	{
		return hand.getAt(i);
	}
	
	/**
	 * Empties this hand
	 */
	public void makeEmpty()
	{
		hand.makeEmpty();
	}
	
	/**
	 * Checks if the hand is empty
	 * @return boolean true if empty, false otherwise
	 */
	public boolean isEmpty()
	{
		return hand.isEmpty();
	}
	
	public String getName()
	{
		return name;
	}
	
	@Override
	public String toString()
	{
		String output = "\n" + name + "'s hand:\n\n";
		
		for(int i=0;i<size();i++)
			output += hand.getAt(i) + "\n";
		
		return output;
	}
}

class Deck
{
private static Queue deck = new Queue(52);
	
	/**
	 * Constructor. Initializes the deck when called
	 */
	public Deck()
	{	
		initialize();	//Fill the Deck with the default Cards (ACE-KING in all Suits (SPADES, HEARTS, DIAMONDS, CLUBS))
	}
	
	/**
	 * Fills the Queue object that represents the deck of cards with Card objects
	 */
	private void initialize()
	{
		for(int i=0;i<4;i++)		//Suits
			for(int j=2;j<15;j++)	//Faces
			{
				switch(i)
				{
					case 0:	//Spades
						deck.enqueue(new Card(Suit.SPADES, Face.getFace(j)));
						break;
					case 1:	//Hearts
						deck.enqueue(new Card(Suit.HEARTS, Face.getFace(j)));
						break;
					case 2:	//Clubs
						deck.enqueue(new Card(Suit.CLUBS, Face.getFace(j)));
						break;
					case 3:	//Diamonds
						deck.enqueue(new Card(Suit.DIAMONDS, Face.getFace(j)));
						break;
				}
			}
	}
	
	/**
	 * Shuffle the deck so the Cards are in random spots
	 */
	public void shuffle()
	{
		Card temp;
		int a;
		for(int i=0;i<10_000;i++)	//Shuffle 10 000 times to really shuffle the deck
			for(int j=0;j<size();j++)
			{
				a = (int)(Math.random() * size());	//Get a random number within the size
				temp = deck.getAt(j);				//Get the card at the current index
				deck.add(j, deck.getAt(a));			//Set the card at the current index to the Card at the random index
				deck.add(a, temp);					//Set the card at the random index to the Card at the current index
			}
		
	}
	
	/**
	 * Returns the Card at the 'top' of the deck and removes it from the deck
	 * @return Card at the 'top' of the deck
	 */
	public Card drawCard()
	{
		if(!isEmpty())
			return deck.dequeue();
		else
			return new Card(Suit.NULL, Face.NULL);
	}
	
	/**
	 * Returns how many Cards are in the deck
	 * @return int representing how many Card objects are in the deck
	 */
	public int size()
	{
		return deck.size();
	}
	
	/**
	 * Returns a String of all of the Card objects that are left in the Deck.
	 */
	@Override
	public String toString()
	{
		String output = "Deck:\n\n";
		
		if(size() == 0)
			return output + "The deck is empty";
		
		for(int i=0;i<deck.size();i++)
			output += deck.getAt(i) + "\n";
		
		return output;
	}

	/**
	 * Check whether or not this Deck has Cards in it
	 * @return	true if Cards are present, false otherwise
	 */
	public boolean isEmpty()
	{
		return deck.isEmpty();
	}
	
	/**
	 * Checks to see if this Deck is the same as deck. The Decks are only equal if they are the same size, and every card is in the same order.
	 * @param deck - Deck to compare to
	 */
	@Override
	public boolean equals(Object obj)
	{
		Deck deck = (Deck) obj;
		boolean result = false;	//Whether or not these two Decks are equal
		if(size() == deck.size())
		{
			for(int i=0;i<size();i++)
			{
				if(this.deck.getAt(i) == deck.deck.getAt(i))
					result = true;
				else
					return false;
			}
		}
		if(result)
			return true;
		else
			return false;
	}
}