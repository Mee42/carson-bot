package com.carson.commands.main.ps;

import java.util.ArrayList;
import java.util.List;

public class Person {

	private String id;
	private List<String> planets;
	
	public Person(String id) {
		this.id = id;
		planets = new ArrayList<String>();
	}
	
	public Person(String id, List<String> p) {
		this.id = id;
		this.planets = p;
	}
	
	public Person addPlanet(String planet) {
		planets.add(planet);
		return new Person(id, planets);
		
	}
	
	public List<String> getPlanets(){
		return planets;
	}

	public String getId() {
		return id;
	}
	
}
