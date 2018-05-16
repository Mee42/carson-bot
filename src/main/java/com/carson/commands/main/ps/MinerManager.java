package com.carson.commands.main.ps;

import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.carson.classes.Messanger;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
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
	
	public MinerManager(IDiscordClient c){
		client = c;
		mClient = MinerManager.getClient();
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
		
		if(objects.size() == 0 || mode == -4) {
			System.out.println("DEBUG: mode 4 passed or no objects. returning mode:" + 4 + " objects.size:" + objects.size());
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
		
		System.out.println("DEBUG:mode:" + mode);
		for(int i = 0;i<mode;i++) {
			
			Miner m = DBOtoMiner(objects.get(i));
			System.out.println("DEBUG:m.empty:" + m.empty());
			if(!m.empty()) {
				
				long id = Long.valueOf(
						String.valueOf(
						objects.get(i)
						.get("_id")));
				
				System.out.println("DEBUG:id:" + id);
//				String name =client.getUserByID(id).getName();
				String name = client.fetchUser(id).getName();
				System.out.println("DEBUG:id:"  + id + " name: " + name);
				
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
	
	public static boolean SmineFor(MessageReceivedEvent event) {
		
		if(event.getChannel().getLongID() != 434858429618847751L) {
			new Messanger(event.getClient()).sendMessage(event.getChannel(), "boi ur trying to mine here lol u can only mine in the channel called mining lol");
			System.out.println("DEBUG: mined in wrong channel");
			return false;
		}
		
		
		return new MinerManager(event.getClient()).mineFor(event);
	}
	
	public EmbedObject getEntrys(String collection) {
		DB database = mClient.getDB("miningDB");
		DBCollection c = database.getCollection(collection);
		
		return new PsListParser(client).parseBuilder(c.find().toArray(),collection).build();
		
		
		
		
	}
	
	public boolean mineFor(String id) {
		
		DBCollection miners = mClient.getDB("miningDB").getCollection("miners");
		
		DBCursor cursorMiners = miners.find(new BasicDBObject("_id", id));
	
		DBObject minerDBO = cursorMiners.one();
		cursorMiners.close();
		
		if(String.valueOf(minerDBO.get("time")).equals(getHour())) {
			return false;
		}
		RequestBuffer.request(() -> {
			try{
	            
	        
			
			client.getChannelByID(443168645510856704L).sendMessage( //this is the cb-log channel in ps
				client.getUserByID(Long.parseLong(id)).getDisplayName( //gets the name for the user
							client.getGuildByID(428613801269788687L) //for the planet sim guild
							) + " mined at :" + getHour()); //sends the hour
	        } catch (DiscordException e){
	            System.err.println("Message could not be sent with error: ");
	            e.printStackTrace();
	        }
	
			});
		
		return mine(id);
	}
	
	
	

	private boolean mine(String id) {
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
		cursorMiners.close();
		cursorPeople.close();
		
		
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
	
	public boolean adminMineFor(String id) {
		return mine(id);
	}
	
	public void hardcodePopulatePeople() {
		DB database = mClient.getDB("miningDB");
		DBCollection collection_people = database.getCollection("people");
		collection_people.drop();
		Person[] people = new Person[] {
				new Person("364897415637106700").addPlanet("earth"), //ace skipper
				new Person("293853365891235841").addPlanet("earth"), //me
				new Person("422191638736142346").addPlanet("earth"), //karxn
				new Person("260739564895731715").addPlanet("earth"), //daniel
				new Person("317104272405823489").addPlanet("earth"),//alex
				new Person("318783502768144384").addPlanet("earth"), //yellowtoad
				new Person("378915849391570964").addPlanet("earth"), //Blastoise
				new Person("334513103838511115").addPlanet("earth"), //fodder
				new Person("294903134902419456").addPlanet("earth") //aaron
				
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
		
		DBCollection people = database.getCollection("people");
		
		DBCursor peoples = people.find();
		
		List<DBObject> pplDBO = peoples.toArray();
		
		for(DBObject d : pplDBO) {
			collection.insert(
					minerToDBOclean(new Miner(), String.valueOf(d.get("_id")))
							);
		}
		
	}
	
	private DBObject minerToDBOclean(Miner miner, String id) {
		DBObject a = minerToDBO(miner,id);
		a.put("time", -1);
		return a;
		
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
 		people.findAndModify(people.find(query).one(), person);
		
	}
	
	private String getHour() {
		
		Date date = new Date();   // given date
		Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
		calendar.setTime(date);   // assigns calendar to given date 
		       // gets hour in 12h format
		return String.valueOf(calendar.get(Calendar.HOUR));
	}
	
	public static EmbedObject Sextract(int mode) {
		return new MinerManager().extract(mode);
		
	}
	
	public boolean fix(String id, String type, int ammount) throws Exception{
		
		
		System.out.println("fixing:" +  id + " type:" + type + " amount" + ammount);
				
		DBObject idObject = new BasicDBObject("_id", id);
		
		DBCursor cursorMiners = mClient.getDB("miningDB").getCollection("miners").find(idObject);
		
		
		
		DBObject minerDBO = cursorMiners.one();
		cursorMiners.close();
		
		
		Miner miner = DBOtoMiner(minerDBO);
		
		
		int[] items = miner.StoreMiner();
		int item;
		
		if(type.equals("metal")) {
			item = 1;
		}else if(type.equals("oil")) {
			item = 0;
		}else if(type.equals("coal")) {
			item = 2;
		}else if(type.equals("silicon")) {
			item = 3;
		}else if(type.equals("neo")) {
			item = 4;
		}else {
			throw new Exception();
		}
		
		items[item] = items[item] + ammount;
		Miner updated = new Miner(items);
		
		mClient.getDB("miningDB").getCollection("miners").findAndModify(minerDBO, minerToDBO(updated, id));
		
		return true;
	}
	
	public static boolean SAdminMineFor(String id) {
		return new MinerManager().adminMineFor(id);
	}
	
	//used for extraction
	public MinerManager() {
		mClient = MinerManager.getClient();
	}
	
	
	
	private DBObject minerToDBO(Miner m, String id) {
		return new BasicDBObject("_id", id)
				.append("oil", m.StoreMiner()[0])
				.append("metal", m.StoreMiner()[1])
				.append("coal", m.StoreMiner()[2])
				.append("silicon", m.StoreMiner()[3])
				.append("neo", m.StoreMiner()[4])
				.append("time", getHour())
				;
				
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











