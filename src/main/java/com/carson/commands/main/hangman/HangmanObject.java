package com.carson.commands.main.hangman;

import java.util.ArrayList;
import java.util.List;

import com.carson.classes.VerifyAwaiting;

public class HangmanObject {

	public VerifyAwaiting hangman = new VerifyAwaiting();
	public String hangmanText = "";
	public boolean hangmanBoolean = false;
	public List<Character> hangmanDone = new ArrayList<Character>();
	
}
