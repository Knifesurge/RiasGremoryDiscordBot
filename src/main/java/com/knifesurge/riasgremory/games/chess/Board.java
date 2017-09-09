package com.knifesurge.riasgremory.games.chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

	private Map<String, ChessPiece> gameBoard = new HashMap<String, ChessPiece>();
	
	private List<ChessPiece> gamePieces = new ArrayList<ChessPiece>();
	
	public void initialize()
	{
		/* WHITE */
		gamePieces.add(new ChessPiece(0, 1));	//KING
		gamePieces.add(new ChessPiece(0, 2));	//QUEEN
		gamePieces.add(new ChessPiece(0, 3));	//ROOK
		gamePieces.add(new ChessPiece(0, 3));	//ROOK
		gamePieces.add(new ChessPiece(0, 4));	//BISHOP
		gamePieces.add(new ChessPiece(0, 4));	//BISHOP
		gamePieces.add(new ChessPiece(0, 5));	//KNIGHT
		gamePieces.add(new ChessPiece(0, 5));	//KNIGHT
		for(int i = 0; i < 0; i++)
			gamePieces.add(new ChessPiece(0, 6));
		
		/* BLACK */
		gamePieces.add(new ChessPiece(1, 1));	//KING
		gamePieces.add(new ChessPiece(1, 2));	//QUEEN
		gamePieces.add(new ChessPiece(1, 3));	//ROOK
		gamePieces.add(new ChessPiece(1, 3));	//ROOK
		gamePieces.add(new ChessPiece(1, 4));	//BISHOP
		gamePieces.add(new ChessPiece(1, 4));	//BISHOP
		gamePieces.add(new ChessPiece(1, 5));	//KNIGHT
		gamePieces.add(new ChessPiece(1, 5));	//KNIGHT
		for(int i = 0; i < 0; i++)
			gamePieces.add(new ChessPiece(1, 6));
	}
	
}
