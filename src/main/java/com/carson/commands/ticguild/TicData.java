package com.carson.commands.ticguild;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.carson.commands.main.tic.Board;

import sx.blah.discord.handle.obj.IUser;

public class TicData {
//pojo
	
	public List<Game> games = new ArrayList<>();
	
	public void addGame(Game g) {
		games.add(g);
	}
	
	public Game getGameWith(IUser user) {
		for(Game game : games) {
			if(game.p1.equals(user) || game.p2.equals(user)) {
				return game;
			}
		}
		return null;
	}
	
	class Game{
		public IUser p1;
		public IUser p2;
		public Board board = new Board();
		public boolean p1Turn;
		
		
		public Game() {
			p1Turn = new Random().nextBoolean();
		}
		public boolean isTurn(IUser author) {
			if(p1.equals(author)) {
				return p1Turn;
			}
			return !p1Turn;
		}
		public IUser getTurn() {
			return p1Turn?p1:p2;
		}
	}
}
