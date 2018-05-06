package com.carson.commands.main.ps;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;
import sx.blah.discord.util.RequestBuffer.RequestFuture;

public class MinerManager {
	IDiscordClient client;
	MongoClient mClient;
	
	
	/*
	 * 
	 * in miningDB there is miners and people
	 * 
	 * 
	 */
	
	public static MongoClient getClient() {
		try {
			return new MongoClient();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public MinerManager(IDiscordClient a, MongoClient client) {
		this.client = a;
		mClient = client;
	}
	
	public boolean mineFor(MessageReceivedEvent event) {
		return mineFor(event.getAuthor().getStringID());
	}
	
	
	
	public EmbedObject extract(int mode) {  
		DB database = mClient.getDB("miningDB");
		DBCollection minersCollection = database.getCollection("miners");
		EmbedBuilder builder = new EmbedBuilder();
		builder.withColor((int)(Math.random()*245+1), (int)(Math.random()*245+1),(int)(Math.random()*245+1));
		builder.withAuthorName("Carson-Bot");
		builder.withTitle("Mining records");

		
		DBCursor miners = minersCollection.find();
		List<DBObject> objects= miners.toArray();
		
		if(objects.size() == 0) {
			return builder.build();
		}
		
		
		if(mode == -2) { // ALL
			mode = objects.size();
		}else if(mode == -1) {  //HALF
			mode = objects.size() / 2- 1;
		}
		
		if(mode == 0) {
			mode = 1;
		}else if(mode == -1) {
			mode = 1;
		}
		
		System.out.println("mode:" + mode);
		for(int i = 0;i<mode;i++) {
			
			Miner m = DBOtoMiner(objects.get(i));
			System.out.println("m.empty:" + m.empty());
			if(!m.empty()) {
				
				long id = Long.valueOf(
						String.valueOf(
						objects.get(i)
						.get("_id")));
				RequestFuture<String> nameFuture =  RequestBuffer.request(() -> {
					 return client.fetchUser(id).getName();
				 });
				String name = nameFuture.get();
				
				
				 
				System.out.println("id:"  + id + " name: " + name);
				
				builder.appendField(
						name,   //gets the name of the user with the id in the miner object
						m.getStuff(), //the mining log
						false);
				
				
				
				minersCollection.findAndModify(objects.get(i), minerToDBO(new Miner(), (String)(objects.get(i).get("_id"))));
				
			}else {
				if(mode < objects.size()) {
					mode++;
				}
			}
		}
				
				
				
		return builder.build();
	}
	
	
	
	
	public String getEntrys(String collection) {
		DB database = mClient.getDB("miningDB");
		DBCollection c = database.getCollection(collection);
		
		return c.find().toArray().toString();
		
		
		
		
	}
	
	public boolean mineFor(String id) {
		System.out.println("mining for :" + id);
		DB database = mClient.getDB("miningDB");
		
		DBCollection miners = database.getCollection("miners");
		DBCollection collection_people = database.getCollection("people");

		DBObject idObject = new BasicDBObject("_id", id);
		
		DBCursor cursorMiners = miners.find(idObject);
		DBCursor cursorPeople = collection_people.find(idObject);
		
		if(cursorPeople.size() != 1){
			System.out.println("error: could not find user");
			return false;
		}else {
			System.out.println("found one user");
		}
		
		DBObject minerDBO = cursorMiners.one();
		DBObject personDBO = cursorPeople.one();
		
		Miner miner = DBOtoMiner(minerDBO);
		Person person = DBOtoPerson(personDBO);
		
		List<String> planets = person.getPlanets();
		System.out.println("planets:" + planets);
		for(String planet : planets) {
			switch(planet) {
				case "earth":
					miner.mineEarth();
					System.out.println("mined on earth");
					break;
				case "sol":
					miner.mineSol();
					System.out.println("mined on sol");
					
					break;
				case "kepler":
					miner.mineKepler();
					System.out.println("mined on kepler");
					break;
				case "celphy":
					miner.mineC();
					System.out.println("mined on celphy");
					break;
					
				default:
					System.out.println("couldn't find " + planet);
					break;
			}
			
		}
		miners.findAndModify(minerDBO, minerToDBO(miner, person.getId()));
		return true;
	}
	
	public void hardcode_populatePeople() {
		DB database = mClient.getDB("miningDB");
		DBCollection collection_people = database.getCollection("people");
		collection_people.drop();
		Person[] people = new Person[] {
				new Person("279412525051674624").addPlanet("earth").addPlanet("sol"),
				new Person("293853365891235841").addPlanet("earth").addPlanet("kepler"),
				new Person("317104272405823489")
				
		};
		
		for(Person person : people) {
			collection_people.insert(personToDBO(person));
		}
		System.out.println(collection_people.count() + " people added");
		
	}
	
	public void populateMinersBlank() {
		DB database = mClient.getDB("miningDB");
		DBCollection collection = database.getCollection("miners");
		collection.drop();
		String[] playerIDs = new String[] {
				"279412525051674624",
				"293853365891235841",
				"317104272405823489"};
		
		for(String id : playerIDs) {
			collection.insert(minerToDBO(new Miner(), id));
		}
	}
	
	public void addPlanet(String id, String planet) {
		DB database = mClient.getDB("miningDB");
		DBCollection people = database.getCollection("people");
		BasicDBObject query = new BasicDBObject("_id", id);
		DBObject person = people.find(query).one();
		for(int i = 0;1<2;i++) {
			if(person.containsField("planet_" + i)) {
				
			}else {
				person.put("planet_"+i,planet);
				System.out.println("added " +planet + " to id  " + id);
				break;
			}
			
			
		}
//		client.getChannelByID(123L).sendMessage(id + " just got planet:" + planet + " added to the db. hardcode him !");
 		people.findAndModify(people.find(query).one(), person);
		
	}
	
	
	
	
	
	
	
	
	private DBObject minerToDBO(Miner m, String id) {
		return new BasicDBObject("_id", id)
				.append("oil", m.StoreMiner()[0])
				.append("metal", m.StoreMiner()[1])
				.append("coal", m.StoreMiner()[2])
				.append("silicon", m.StoreMiner()[3])
				.append("neo", m.StoreMiner()[4]);
				
	}
	private Miner DBOtoMiner(DBObject o) {
		return new Miner( new Object[] {
				o.get("oil"),
				o.get("metal"),
				o.get("coal"),
				o.get("silicon"),
				o.get("neo")
				});
		
		
	}
	
	private DBObject personToDBO(Person p) {
		/*
		 * Person has an List<String> of planets, and a ID
		 */
		BasicDBObject b = new BasicDBObject("_id",p.getId());
		
		List<String> planets = p.getPlanets();
		int i = 0;
		for(String planet : planets) {
			b.append("planet_" + String.valueOf(i), planet);
			i++;
		}
		
		return b;
	}
	
	private Person DBOtoPerson(DBObject o) {
		Person p = new Person((String)o.get("_id"));
		int i = 0;
		while(true) {
			if(o.containsField("planet_" + i)) {
				p.addPlanet((String)o.get("planet_" + i));
			}else {
				break;
			}
			i++;
			
			
		}
		return p;
	}
	
	
	
}











