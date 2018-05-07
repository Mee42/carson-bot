package com.carson.commands.main.ps;

public class Miner {
	
	private int oil;
	private int metal;
	private int coal;
	private int silicon;
	private int neo;
	
	
	public Miner() {
		  oil = 0;
		  metal=0;
		  coal=0;
		  silicon=0;
		  neo=0;
	}
	
	public Miner(String oil, String metal, String coal,String silicon, String neo) {
		this.oil = Integer.parseInt(oil);
		this.metal = Integer.parseInt(metal);
		this.coal = Integer.parseInt(coal);
		this.silicon = Integer.parseInt(silicon);
		this.neo = Integer.parseInt(neo);
		
	}
	
	public Miner(Object[] a) {
		this.oil = Integer.parseInt(a[0].toString());
		this.metal = Integer.parseInt(a[1].toString());
		this.coal = Integer.parseInt(a[2].toString());
		this.silicon = Integer.parseInt(a[3].toString());
		this.neo = Integer.parseInt(a[4].toString());
	}
	
	public int[] StoreMiner() {
		return new int[] {oil,metal,coal,silicon,neo};
	}
	
	

	
	
	
	public void mineEarth() {
		int die = roll();
		if(die>=9)
			metal++;
		if(die>=18) 
			metal++;
		
		
		die = roll();
		if(die>=10)
			coal++;
		if(die>=20) 
			coal++;
		
		
		
		die = roll();
		if(die>=10)
			oil++;
		if(die>=20) 
			oil++;
		
		
		
		die = roll();
		if(die>=17)
			silicon++;
		
		die = roll();
		if(die>=18)
			neo++;
	}
	
	
	public void mineSol() {
		int die = roll();
		if(die>=6)
			metal++;
		if(die>=12)
			metal++;
		if(die>=18) 
			metal++;
		
		
		die = roll();
		if(die>=10)
			coal++;
		if(die>=20) 
			coal++;
		
		
		
		die = roll();
		if(die>=8)
			oil++;
		if(die>=16) 
			oil++;
		
		
		
		die = roll();
		if(die>=16)
			silicon++;
		
		die = roll();
		if(die>=11)
			neo++;

	}
	
	
	public void mineKepler() {
		int die = roll();
		if(die>=5)
			metal++;
		if(die>=10) 
			metal++;
		if(die>=15)
			metal++;
		if(die>=20)
			metal++;
		
		
		die = roll();
		if(die>=7)
			coal++;
		if(die>=14) 
			coal++;
		
		
		
		die = roll();
		if(die>=10)
			oil++;
		if(die>=20) 
			oil++;
		
		
		
		die = roll();
		if(die>=11)
			silicon++;
		
		die = roll();
		if(die>=12)
			neo++;
		
	}
	
	
	
	
	public void mineC() {
		int die = roll();
		if(die>=10)
			metal++;
		if(die>=20) 
			metal++;
		
		
		
		die = roll();
		if(die>=14)
			coal++;
		
		
		
		die = roll();
		if(die>=15)
			oil++;
		
		
		
		die = roll();
		if(die>=5)
			silicon++;
		if(die>=10)
			silicon++;
		if(die>=15)
			silicon++;
		if(die>=20)
			silicon++;
		
		die = roll();
		if(die>=4)
			neo++;
		if(die>=8)
			neo++;
		if(die>=12)
			neo++;
		if(die>=16)
			neo++;
		if(die>=20)
			neo++;
		if(die>=12)
			neo++;
		
	}
	
	private int roll() {
		return (int)(Math.random()*20+1);
	}
	
	
	public String getStuff() {
		return "metal:"  + metal+ "\ncoal" + coal+ "\noil:" + oil+ "\nsilicon" + silicon+ "\nneo:" + neo;
	}
		
	public boolean empty() {
		if(oil !=0)return false;
		if(metal !=0)return false;
		if(coal !=0)return false;
		if(silicon !=0)return false;
		if(neo !=0)return false;
		return true;
	}
		
		
		

	



}
