package com.carson.commands.ticguild;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.carson.commands.main.tic.Board;

public class TicData {
//pojo
	
	public List<Game> games;
	
	public TicData() {
		games = new ArrayList<>();
	}
	
	public void addGame(Game g) {
		games.add(g);
	}
	public List<Game> getGames() {
		return games;
	}
	
	public void setGames(List<Game> games) {
		this.games = games;
	}
	
	public Game getGameWith(long user) {
		for(Game game : games) {
			if(game.p1 == user || game.p2 == user) {
				return game;
			}
		}
		return null;
	}
	
	class Game{
		public long p1;
		public long p2;
		public Board board = new Board();
		public boolean p1Turn;
		
		
		
		public long getP1() {
			return p1;
		}
		public void setP1(long p1) {
			this.p1 = p1;
		}
		public long getP2() {
			return p2;
		}
		public void setP2(long p2) {
			this.p2 = p2;
		}
		public Board getBoard() {
			return board;
		}
		public void setBoard(Board board) {
			this.board = board;
		}
		public boolean isP1Turn() {
			return p1Turn;
		}
		public void setP1Turn(boolean p1Turn) {
			this.p1Turn = p1Turn;
		}
		public Game() {
			p1Turn = new Random().nextBoolean();
		}
		public boolean isTurn(long author) {
			if(p1 == author) {
				return p1Turn;
			}
			return !p1Turn;
		}
		public long getTurn() {
			return p1Turn?p1:p2;
		}
	}
}
