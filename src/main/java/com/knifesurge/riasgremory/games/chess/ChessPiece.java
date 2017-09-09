package com.knifesurge.riasgremory.games.chess;

public class ChessPiece {

	private String name;
	
	enum Side
	{
		WHITE, BLACK;
	}
	
	private Side side;
	private int rank;
	
	public ChessPiece(int side, int rank)
	{
		this.side = side == 1 ? Side.WHITE : Side.BLACK;
		this.rank = rank;
		name = genName();
	}
	
	public Side getSide()
	{
		return side;
	}
	
	public int getRank()
	{
		return rank;
	}
	
	public String getName()
	{
		return name;
	}
	
	private String genName()
	{
		switch(rank)
		{
			case 1:
				return "KING";
			case 2:
				return "QUEEN";
			case 3:
				return "ROOK";
			case 4:
				return "BISHOP";
			case 5:
				return "KNIGHT";
			case 6:
				return "PAWN";
			default:
				return "UNKNOWN";
		}
	}
	
	@Override
	public String toString()
	{
		return name + ": " + side + ": " + rank;
	}
}
