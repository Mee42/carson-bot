package com.carson.dataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.carson.classes.FileIO;
import com.carson.commands.gg.Bank;
import com.carson.commands.main.tac.RunningTacGame;
import com.carson.commands.gg.UserGG;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import sx.blah.discord.handle.obj.IUser;

public class GuildDataOrginizer {
	   /*there should only every be one instance of this class
	  	*the purpose is to keep track of all GuildData objects, and make sure they are backed up properly in the event of an unorthodox shutdown
	    *constructor should be empty, as no arguments are needed. 
	    *updating data from json is not done in constructor to allow for better testing
	    *this could potentially be used to store command prefixes, however that would have to be incorporated with another big change to make rewriting commands worth it
	    */
    private List<GuildData> guilds = new ArrayList<>();
    private List<UserDataNoGuild> users = new ArrayList<>();
    private List<UserGG> userGGs = new ArrayList<>();
    private Bank bank = new Bank();

    private transient  List<Long> easter = new ArrayList<>();
    private transient String jsonFile = "/home/carson/java/files/json/jsonGuildDataDump.json";
    private transient List<RunningTacGame> games = new ArrayList<>();


    public GuildDataOrginizer importFromJson(){
            Gson gson = new GsonBuilder().create();
            String json = FileIO.use(jsonFile).readString();
            GuildDataOrginizer newGuildOrginizerData = gson.fromJson(json,GuildDataOrginizer.class);
            if(newGuildOrginizerData == null){
                System.err.println("null json");
                System.exit(0);
            }
            this.guilds = newGuildOrginizerData.guilds;
            this.users = newGuildOrginizerData.users;
            this.userGGs = newGuildOrginizerData.userGGs;
            this.bank = newGuildOrginizerData.bank;
            return this;
        }

    public  String privateSterilize(){ //TODO nice how the "privateSterilize" method is public
        Gson gson = new GsonBuilder().create();
        String json =gson.toJson(this);
        FileIO.use(jsonFile).write(json);
        FileIO.use(jsonFile + "..." + UUID.randomUUID()).write(json);
//	        System.err.println("sterilized"); //i think this should getDatabase everything something changed /shrug
        new DBHandler().getDatabase();
        return json;
    }

    public String getSterilze() {
        return privateSterilize();
    }

//	    public void getDatabase() { //sterilizes the object on a specerate thread, returns instantly
//	    	executer.execute(
//	    	new Runnable() {
//	    		@Override
//				public void run() {
//					privateSterilize();
//				}
//	    		
//	    	});
//	    	
//	    }
    public UserGG getUser(IUser user){
        return getUser(user.getLongID());
    }
    public UserGG getUser(long id){
	        for(UserGG user : userGGs){
	            if(user.getId() == id){
	                return user;
                }
            }
            UserGG newUser = new UserGG(id);
	        userGGs.add(newUser);
	        return newUser;
        }

    public List<UserGG> getUserGGs() {
        return userGGs;
    }

    public List<Long> getEaster() {
        return easter;
    }

    public void setEaster(List<Long> easter) {
        this.easter = easter;
    }

    public void setUserGGs(List<UserGG> userGGs) {
        this.userGGs = userGGs;
    }

    public GuildData getGuild(long id){
	        for(GuildData guildData : guilds){
	            if(guildData.getId() == id){return guildData;}//returns if there is an exisitng guild data instance
	        }
	        GuildData justMadeInstance = new GuildData(id); //makes a new one if no one is found
	        guilds.add(justMadeInstance);//adds it to the array
	        return justMadeInstance; //returns it
        }
	    
//	    

    public UserDataNoGuild getUserData(long id){
        for(UserDataNoGuild testingUser : users){
                if(testingUser.getId() == id){
                      return testingUser;
                }
         }
        UserDataNoGuild newUser = new UserDataNoGuild(id);
         users.add(newUser);
         return newUser;
    }

    public long getXPForUser(long id) {
        return getUserData(id).getXP();
    }

    public long increaseXPForUser(long id, int amount) {
        long newXP = getUserData(id).incrimentXP(amount);
        privateSterilize(); //TODO has been switched off of threaded mode
        return newXP;
    }

    public List<UserDataNoGuild> getUsers() {
        return users;
    }



    //these methods deal with running tac games

    public RunningTacGame getGameWithUser(long id) {//NEEDS TO CHECK FOR NULL WHEN CALLED
        for(RunningTacGame game : games) {
            if(game.getIdP1() == id || game.getIdP2() == id) {
                return game;
            }
        }
        return null;
    }



    /**
     * MAY RETURN NULL
     */
    public RunningTacGame getGameWithID(long id) {//NEEDS TO CHECK FOR NULL WHEN CALLED
        for(RunningTacGame game : games) {
            if(game.getGameId() == id) {
                return game;
            }
        }
        return null;
    }

    public RunningTacGame registerGame(RunningTacGame game) {
        games.add(game);
        return game;
    }

    public boolean endGame(RunningTacGame toRemove) {
	    	if(games.contains(toRemove)) {
	    		games.remove(toRemove);
	    		return true;
	    	}
	    	return false;
	    	
	    }

    public Bank getBank() {
	        return bank;
    }
    public void setBank(Bank b){
	        this.bank = b;
    }



    public class UserDataNoGuild extends UserData{
//		public String publicKey;
		
		public UserDataNoGuild(long id) {
			super(id);
		}

		private int XP;

		public int getXP() {
			return XP;
		}
		
		
//		public String getPublicKey() {
//			return publicKey;
//		}


//		public void setPublicKey(String publicKey) {
//			this.publicKey = publicKey;
//		}


		@Override
		public String toString() {
			return "ID:" + id + " XP:" + XP;
		}
		
		public long incrimentXP(int amount) {
			XP+=amount;
			return XP;
		}
		
		
	}
}