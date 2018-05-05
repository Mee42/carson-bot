package com.carson.tic;

public class Board {
	
	private int[] board;
	
	public Board(){
		board = new int[] {0,0,0,0,0,0,0,0,0};
		printBoard();
	}
	
	public Board(int a0,int a1, int a2,int a3,int a4,int a5,int a6,int a7,int a8) {
		this.board = new int[] {a0,a1,a2,a3,a4,a5,a6,a7,a8};
	}
	
	
	public int get(int i) {
		return board[i];
	}
	
	
	
	
	public String printBoard() {
		return "**Game board:\t\t\tPositions:**\n```"
				+ getChar(board[0]) + " | " + getChar(board[1]) + " | " + getChar(board[2]) + "\t\t0 | 1 | 2\n"
				+ "----------\t\t----------\n"
				+ getChar(board[3]) + " | " + getChar(board[4]) + " | " + getChar(board[5]) + "\t\t3 | 4 | 5\n"
				+ "----------\t\t----------\n"
				+ getChar(board[6]) + " | " + getChar(board[7]) + " | " + getChar(board[8]) + "\t\t6 | 7 | 8"
				+"```"
				;
	}
	
	
	private Character getChar(int i) {
		if(i == 1) {
			return 'X';
		}
		if(i == 2) {
			return 'O';
		}
		return ' ';
	}
	
	
	
	public boolean set(int pos, int player) {
		if(pos>8) {
			return false;
		}
		if(board[pos] == 0) {
			board[pos] = player;
			return true;
		}
		return false;
	}
	
	public int boardWin() {
		//sideways
		if(board[0] == board[1] && board[0] == board[2] && board[0] != 0) {
			return board[0];
		}
		if(board[3] == board[4] && board[3] == board[5]&& board[3] != 0) {
			return board[3];
		}
		if(board[6] == board[7] && board[6] == board[8]&& board[6] != 0) {
			return board[6];
		}
		
		//up/down
		if(board[0] == board[3] && board[0] == board[6]&& board[0] != 0) {
			//System.out.println(board[0] + ":"+board[3]+":"+board[6]);
			return board[0];
		}
		if(board[1] == board[4] && board[1] == board[7]&& board[1] != 0) {
			return board[1];
		}
		if(board[2] == board[5] && board[2] == board[8]&& board[2] != 0) {
			return board[2];
		}
		
		//diaginal
		if(board[0] == board[4] && board[0] == board[8]&& board[0] != 0) {
			return board[0];
		}
		if(board[2] == board[4] && board[2] == board[6] && board[2] != 0) {
			return board[2];
		}
		
		if(full()) {
			return -1;
		}
		return 0;
	}
	
	
	public Board copy() {
		return new Board(board[0],board[1],board[2],board[3],board[4],board[5],board[6],board[7],board[8]);
	}
	
	
	public boolean full() {
		for(int i = 0;i<9;i++) {
			if(board[i] == 0) {
				return false;
			}
		}
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
