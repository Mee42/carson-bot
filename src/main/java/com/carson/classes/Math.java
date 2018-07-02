package com.carson.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Math {

	public class MathException extends RuntimeException{
		private static final long serialVersionUID = 1L;
		
	}
	public static final boolean printDubug = false;
	
	public double calculate(String str) {
		try {
			return run(str).i;
		}catch(MathException e) {
			throw e;
		}
	}
	
	public Segment run(String str) {
		str = str.replace(" ", "");//strip out spaces
		List<Segment> parts = parse(str);//only happens once :D
		return dealWith(parts);//lol 
	}
	
	private Segment dealWith(List<Segment> segments) {
		segments = copy(segments);
		if(printDubug) {
			System.out.println("dealing with:");
		}
		print(segments);
		if(!(segments.contains(new Segment('(')) || segments.contains(new Segment(')')))) {
			return run(segments);
		}
		for(int initalIndex = 0;initalIndex<segments.size();initalIndex++) {
			Segment possibleInital = segments.get(initalIndex);
			if(possibleInital.isNumber) {
				continue;
			}
			if(possibleInital.c == '(') {
				int falsePos = 0;
				for(int lastIndex = initalIndex+1;lastIndex<segments.size();lastIndex++) {
					if(segments.get(lastIndex).isNumber) {
						continue;
					}
					if(segments.get(lastIndex).c == '(') {
						falsePos++;
						if(printDubug) {
							System.out.println("falsePos++");
						}
						continue;
					}
					if(segments.get(lastIndex).c == ')') {
						if(falsePos == 0) {
							if(printDubug) {
								System.out.println("from " + initalIndex + " to " + lastIndex);
								System.out.println("sending " + (initalIndex + 1) + " to " + (lastIndex - 1));
							}
							Segment result = dealWith(copy(segments.subList(initalIndex+1, lastIndex))); //NOTE
							//SUBLIST is INCLUSVIVE, EXCLUSIVE
							//REMEMBER FOR ALL ETURNITY
							List<Segment> firstHalf;
							List<Segment>  lastHalf;
//							if(initalIndex == 0) {//to prevent arrayindexoutofbounds
//								System.out.println("firstHalf is empty");
//								firstHalf = new ArrayList<>();
//							}else {
								firstHalf = copy(segments.subList(0, initalIndex));//take the part of the list before the (
//							}
//							if(lastIndex == segments.size()) {
//								System.out.println("lastHalf is empty");
//								lastHalf = new ArrayList<>();
//							} //TODO i dont belive this is nessesary with the stuff on lines ~50-51
							lastHalf = copy(segments.subList(lastIndex + 1, segments.size()));//the part after the )
													//-1 and +1 and to get rid of the () while not passing them to dealWith();
							//format:
							//firstHalf result lastHalf
							List<Segment> newSegments = new ArrayList<>();
							newSegments.addAll(firstHalf);
							newSegments.add(result);
							newSegments.addAll(lastHalf);
							segments = newSegments;
						}else {
							if(printDubug) {
								System.out.println("falsePos--");
							}
							falsePos--;
						}
					}
				}
			}
			
		}
		
		return run(segments);
	}
	
	private Segment run(List<Segment> parts){
		if(printDubug) {
			System.out.println("running");
		}
		print(parts);
		for(int operation = 0;operation<3;operation++) {
			List<Character> operands = null;
			switch(operation) {
				case 0:
					operands = Arrays.asList(new Character[] {'^'});
					break;
				case 1:
					operands = Arrays.asList(new Character[] {'*', '/'});
					break;
				case 2:
					operands = Arrays.asList(new Character[] {'+', '-'});
					break;
				default:
					System.err.println("errororororr");
					throw new RuntimeException("messed up the switch");
			}
			boolean changed = true;
			while(changed) {
				changed = false;
				for(int i = 1;i<parts.size() - 1;i++) {
					if(!parts.get(i).isNumber) {
						Segment op = parts.get(i);
						if(!operands.contains(op.c)) {
//							System.out.println("not covering operand " + op.c);
							continue; 
						}
						Segment a = parts.get(i-1);
						Segment b = parts.get(i+1);
						if(!(a.isNumber || b.isNumber)) {
							System.err.println("surrounding numbers are not numbers");
							continue;
						}
						changed = true;
						double c = doOp(a.i,b.i,op.c);
						parts.set(i, new Segment(c));
						parts.remove(a);
						parts.remove(b);
					}
				}
			}
		}
		if(printDubug) {
			System.out.println("returning");
		}
		print(parts);
		if(parts.size() == 0) {
			throw new MathException();
		}
		return parts.get(0);
	}
	
	private void print(List<Segment> strs){
		if(printDubug) {
			System.out.println("---");
			int index = 0;
			for(Segment str : strs) {
				System.out.println(index + " " + str.toString());
				index ++;
			}
			System.out.println("---");
		}
	}
	
	private List<Segment> parse(String str){
		List<Segment> parts = new ArrayList<>();
		String temp = "null";
		for(char c : str.toCharArray()) {
			if(String.valueOf(c).matches("[0-9]") || c == '.' || (c == '-' && temp == "null")) {
				if(temp != "null") {
					temp+= String.valueOf(c);
				}else {
					temp = String.valueOf(c);
				}
			}else {
				if(temp != "null") {
					try {
						parts.add(new Segment(Double.valueOf(temp)));
					}catch (NumberFormatException e) {
						throw new MathException();
					}
					temp = "null";
				}
				parts.add(new Segment(c));
			}
		}
		if(temp != "null") {
			parts.add(new Segment(Double.valueOf(temp)));
			temp = "null";
		}
		return parts;
	}
	
	private double doOp(double a, double b, char c) {
		switch(c) {
			case '+':
				return a + b;
			case '-':
				return a - b;
			case '*':
				return a * b;
			case '/':
				return a / b;
			case '^':
				return java.lang.Math.pow(a, b);
			default:
				return 0;
		}
	}
	
	private List<Segment> copy(List<Segment> arr){
		List<Segment> segments = new ArrayList<>();
		for(Segment s : arr) {
			segments.add(s.copy());
		}
		return segments;
	}
	
	class Segment{
		public double i;
		public char c;
		public boolean isNumber;
		Segment(double c2){
			this.i = c2;
			isNumber = true;
		}
		Segment(char c){
			this.c = c;
			isNumber = false;
		}
		
		@Override
		public String toString() {
			return isNumber?("int:  " + i):("char: " + c); 
		}
		public Segment copy() {
			return isNumber?new Segment(i):new Segment(c);
		}
		
		@Override
		public boolean equals(Object o) {
			Segment s = (Segment)o;
			if(s.isNumber != isNumber) { 
				return false;
			}
			return isNumber?(s.i == i) : (s.c == c);
		}
	}
	
	
	
	public static String format(double d) {
		if(d % 1 == 0 ){
			return String.valueOf((int)d);
		}
		String str = String.valueOf(d);
		return str.substring(0, 
				String.valueOf(d).length() > 5?
						5:String.valueOf(d).length());
	}
	
	public static String format(String str, int spaces, boolean alightRight) {
		if(str.length() >= spaces) {
			return str;
		}
		while(str.length() < spaces) {
			str = (alightRight?" ":"") + str + (alightRight?"":" "); 
		}
		return str;
	}	
}
