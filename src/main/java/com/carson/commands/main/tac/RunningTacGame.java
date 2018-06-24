package com.carson.commands.main.tac;

import java.util.concurrent.ThreadLocalRandom;

import com.carson.dataObject.DataGetter;

public class RunningTacGame {
	
	private final long idP1;  //all vars marked P1 and P2 refer to Player 1 and Player 2, the former being the play who started.
	private final long idP2;  //P1 is 'X', P2 is 'O'
	
	private final long gameID; //seperate games by id
	
	private int turn = 1; //player one always goes first
	
	private BoardForTac board;
	
	
	public RunningTacGame(long idP1, long idP2) {
		super();
		this.idP1 = idP1;
		this.idP2 = idP2;
		long tempGameID = ThreadLocalRandom.current().nextLong(1000L, 9999L);
		
		while(DataGetter.getInstance().getGameWithID(tempGameID) != null){
			tempGameID = ThreadLocalRandom.current().nextLong(1000L, 9999L);
		}
		gameID = tempGameID;
		board = new BoardForTac();
	}
	
	public long getGameId() {
		return gameID;
	}
	
	public long getIdP1() {
		return idP1;
	}
	public long getIdP2() {
		return idP2;
	}
	public int getTurn() {
		return turn;
	}
	
	public String getPlayingMessage() {
		return  board.printBoard(); //printBoard does the code block formating
	}
	
	@Override
	public boolean equals(Object obj) {
		RunningTacGame newGame = (RunningTacGame)obj;
		if(newGame.getGameId() == getGameId()) {
			return true;
		}
		return false;
		
	}
	
	public boolean canPlay(long id) {
		if(id == idP1 && turn == 1) {
			return true;
		}
		if(id == idP2 && turn == 2) {
			return true;
		}
		return false;
		
	}

	public boolean play(int move) {
		int turnTemp = turn;
		turn = (turn == 1) ? 2 : 1;
		return board.set(move, turnTemp);
	}

	public int hasBeenWon() {
		return board.boardWin();
	}

}
