package com.carson.commands.main.tic;

public class Tac {
	//0 is blank
	//1 is user
	//2 is AI
	
	private Board b = new Board();

	public Tac() {

	}
	
	public String start(int turn) {
		if(turn == -1) {
			move();
		}			
		
		return b.printBoard();
	}
	
	public String moveNext(int turn) {
		if(b.set(turn, 2)) {
			move();
			if(b.boardWin() == 2) {
				return b.printBoard() + "\n You Won!!!!!! please contact the dev, this shouldn't happen lol";
			}else {
				return b.printBoard();
			}
		}else {
			move();
			return "Not a valued option. It's the bot's turn\n" + b.printBoard();
		}
		
	}
	
	
	private void move() {
		b.set(choseNext(b), 1);
	}
	
	private double testLayout(Board b,int player, double value) {
		double score = 0;
		
		for(int i = 0;i<9;i++) {
			Board bTest = b.copy();
			if(bTest.set(i, player)) {
				if(bTest.boardWin() == 1) {
					if(value == 1) {
						score += 10000;
					}else {
						//score++;
					}
					
				}else if(bTest.boardWin() == 2) {
					if(value == 1) {
						score -= 1000;
					}else {
						score--;
					}
				}else if(bTest.boardWin() == 0) {
					if(player == 1) {
						score+= testLayout(bTest,2,2) ;
					}else {
						score+= testLayout(bTest,1,2);
					}
				
				}
			}
		}
		
		return score;
	}
	
	private int choseNext(Board b) {
		if(b.get(0) == 2 && 
			b.get(1) == 0 && 
			b.get(2) == 0 && 
			b.get(3) == 0 && 
			b.get(4) == 1 && 
			b.get(5) == 0 && 
			b.get(6) == 0 && 
			b.get(7) == 0 &&
			b.get(8) == 2 ) {
			System.out.println("EVENT: hardcode tictactoe one ran");
			return 3;
		}
		
		if(b.get(0) == 0 && 
				b.get(1) == 0 && 
				b.get(2) == 2 && 
				b.get(3) == 0 && 
				b.get(4) == 1 && 
				b.get(5) == 0 && 
				b.get(6) == 2 && 
				b.get(7) == 0 &&
				b.get(8) == 0 ) {
				System.out.println("EVENT: hardcode tictactoe two ran");
				return 3;
			}
		
		
		
		
		double[] scores = new double[9];
		
		for(int i = 0;i<9;i++) {
			Board bTest = b.copy();
			if(bTest.set(i, 1)){
				scores[i] = testLayout(bTest, 2,1);
			}else {
				scores[i] = -1000000;
			}
		}
		
		
		int best = 0;
		
		for(int i = 0;i<9;i++) {
			if(scores[i] > scores[best]) {
				best = i;
			}
		}
	//	System.out.println("scores");
		boolean allTenK = true;
		for(int q = 0;q<9;q++) {
			double i = scores[q];
			
			if(i != -1000000 || i!=0) {
				allTenK = false;
			}else{
				
			}
			
//		System.out.println(q+")" + i);
		}
		
		if(allTenK) {
			
		//	System.out.println("\nI've calculated that there is no way for anyone to win. ");
		}
		
		
//		System.out.println("best move found:" + best);
		return best;
	}


	public int checkWin() {
		return b.boardWin();
	}





}

