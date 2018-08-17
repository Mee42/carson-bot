package com.carson.dataObject;

import com.carson.classes.DB;
import com.carson.commandManagers.Command;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.*;


public class DBHandler {
    public DBHandler() {
    }

    public MongoDatabase getDatabase() {
        MongoClient mongoClient = Mongo.getInstance().getMongoClient();
        MongoDatabase database = mongoClient.getDatabase("carson-bot");
        return database;
    }

    public static DBHandler get() {
        return new DBHandler();
    }

    public MongoCollection<Document> getGlobalDB(){
        return getDatabase().getCollection("global");
    }
    public MongoCollection<Document> getGuildsDB(){
        return getDatabase().getCollection("guilds");
    }
    public MongoCollection<Document> getUsersDB(){
        return getDatabase().getCollection("users");
    }
    public MongoCollection<Document> getGGDB(){
        return getDatabase().getCollection("gg");
    }
    public MongoCollection<Document> getPermissionDB(){
        return getDatabase().getCollection("permissions");
    }
    public MongoCollection<Document> getMessagesDB(){return getDatabase().getCollection("messages");}
    public MongoCollection<Document> getAttachmentsDB(){return getDatabase().getCollection("attachments");}
    public MongoCollection<Document> getDB(String name){return getDatabase().getCollection(name);}


    public class UserData  {
        private final long id;

        UserData(long id) {
            this.id = id;
        }
        public long getId() {
            return id;
        }
    }

    public List<UserData> getUserData() {
        MongoCollection<Document> globalCollection = getDatabase().getCollection("global");
        List<UserData> userData = new ArrayList<>();
        for (Document document : globalCollection.find()) {
            userData.add(toUserData(document));
        }
        return userData;
    }

    public UserData getUserDataBy(long id) {
        for (UserData data : getUserData()) {
            if (data.getId() == id) {
                return data;
            }
        }
        return new UserData(id);
    }



    public void update(UserData data) {
        DB.createOrReplace(fromUserData(data), getGlobalDB());
    }
    public Document fromUserData(UserData data){
        return new Document().append("_id", data.getId());
    }
    public UserData toUserData(Document document){
        return new UserData((long) document.get("_id"));
    }






    public class GuildData {
        private String leaveMessage;
        private final long id;
        private long leaveChannel;

        public GuildData(long id, String leaveMessage, long leaveChannel) {
            this.leaveMessage = leaveMessage;
            this.id = id;
            this.leaveChannel = leaveChannel;
        }
        public GuildData(long id){
            this.leaveMessage = "[name] has left the server";
            this.id = id;
            this.leaveChannel = -1;
        }


        public String getLeaveMessage() {
            return leaveMessage;
        }

        public long getId() {
            return id;
        }

        public long getLeaveChannel() {
            return leaveChannel;
        }

        public void setLeaveMessage(String leaveMessage) {
            this.leaveMessage = leaveMessage;
        }

        public void setLeaveChannel(long leaveChannel) {
            this.leaveChannel = leaveChannel;
        }
    }

    public List<GuildData> getGuidData() {
        List<GuildData> guildData = new ArrayList<>();
        for (Document document : getGuildsDB().find()) {
            guildData.add(toGuildData(document));
        }
        return guildData;
    }

    public GuildData getGuildDataBy(long id) {
        for (GuildData data : getGuidData()) {
            if (data.getId() == id) {
                return data;
            }
        }
        return new GuildData(id);
    }

    public void update(GuildData data) {
        DB.createOrReplace(fromGuildData(data), getGuildsDB());
    }
    public GuildData toGuildData(Document document){
        String leave = "[name] has left the server";
        if (document.keySet().contains("leave_message")) {
            leave = String.valueOf(document.get("leave_message"));
        }
        Long leaveChannel = (Long)document.get("leave_channel");
        if(leaveChannel == null)leaveChannel = -1L;

        return new GuildData((long) document.get("_id"), leave, leaveChannel);
    }
    public Document fromGuildData(GuildData data){
        return new Document()
                .append("_id", data.getId())
                .append("leave_message", data.getLeaveMessage())
                .append("leave_channel", data.getLeaveChannel());
    }







    public class GuildUserData  {
        private final long userId;
        private final long guildId;
        private final String id;
        private int xp;

        public GuildUserData(long userId, long guildId, int xp) {
            this.userId = userId;
            this.guildId = guildId;
            this.xp = xp;
            this.id = userId + "" + guildId;
        }


        public long getUserId() {
            return userId;
        }
        public long getGuildId() {
            return guildId;
        }
        public String getId() {
            return id;
        }
        public int getXp() {
            return xp;
        }
        public void setXp(int xp) {
            this.xp = xp;
        }
    }

    public List<GuildUserData> getGuildUserData() {
        List<GuildUserData> data = new ArrayList<>();
        for (Document document : getUsersDB().find()) {
            data.add(toGuildUserData(document));
        }
        return data;
    }
    public List<GuildUserData> getGuildUserDataForGuild(long guildId) {
        List<GuildUserData> data = new ArrayList<>();
        for (Document document : getUsersDB().find(Filters.eq("guild_id", guildId))) {
            data.add(toGuildUserData(document));
        }
        return data;
    }


    public void update(GuildUserData data) {
        DB.createOrReplace(fromGuildUserData(data),getUsersDB());
    }
    public GuildUserData toGuildUserData(Document document){
        if(document == null){
            throw new NullPointerException();
        }
        long userId = document.get("user_id") == null?-1:(long)document.get("user_id");
        return new GuildUserData(
                userId,
                (long) document.get("guild_id"),
                (int) document.get("xp"));
    }
    public Document fromGuildUserData(GuildUserData data){
        return new Document()
                .append("_id", data.id)
                .append("xp", data.xp)
                .append("user_id", data.userId)
                .append("guild_id", data.getGuildId());
    }



     public class UserGG {
        private final long id;
        private int money;
        private int eduLevel;
        private int debt;
        private double interest;
        private int coins;
        private int invested;
        private int gotten;

        public UserGG(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }


        public int getMoney() {
            return money;
        }


        public int getEduLevel() {
            return eduLevel;
        }

        public int getDebt() {
            return debt;
        }


        public double getInterest() {
            return interest;
        }


        public int getCoins() {
            return coins;
        }


        public int getInvested() {
            return invested;
        }


        public int getGotten() {
            return gotten;
        }

        public UserGG setMoney(int money) {
            this.money = money;
            return this;
        }

        public UserGG setEduLevel(int eduLevel) {
            this.eduLevel = eduLevel;
            return this;
        }

        public UserGG setDebt(int debt) {
            this.debt = debt;
            return this;
        }

        public UserGG setInterest(double interest) {
            this.interest = interest;
            return this;
        }

        public UserGG setCoins(int coins) {
            this.coins = coins;
            return this;
        }

        public UserGG setInvested(int invested) {
            this.invested = invested;
            return this;
        }

        public UserGG setGotten(int gotten) {
            this.gotten = gotten;
            return this;
        }
    }

    public List<UserGG> getUserGG() {
        List<UserGG> data = new ArrayList<>();
        for (Document document : getGGDB().find(Filters.not(Filters.eq("_id", "bank")))){
            data.add(toUserGG(document));
        }
        return data;
    }

    public void update(UserGG user) {
        DB.createOrReplace(fromUserGG(user), getGGDB());
    }

    public Document fromUserGG(UserGG user){
        return new Document()
                .append("_id",user.getId())
                .append("money", user.getMoney())
                .append("edu_level", user.getEduLevel())
                .append("debt", user.getDebt())
                .append("interest", user.getInterest())
                .append("coins", user.getCoins())
                .append("invested", user.getInvested())
                .append("gotten", user.getGotten());
    }

    public UserGG toUserGG(Document document){
        if(document == null){
            throw new NullPointerException();
        }
        return new UserGG((long) document.get("_id"))
                .setMoney((int) document.get("money"))
                .setEduLevel((int) document.get("edu_level"))
                .setDebt((int) document.get("debt"))
                .setInterest((double) document.get("interest"))
                .setCoins((int) document.get("coins"))
                .setInvested((int) document.get("invested"))
                .setGotten((int) document.get("gotten"));
    }







    public class Bank {
        private int money;
        public int getMoney() {
            return money;
        }
        public Bank(int money) {
            this.money = money;
        }
        public void setMoney(int money) {
            this.money = money;
        }
    }

    public Bank getBank() {

        Document doc = getGGDB().find(Filters.eq("_id", "bank")).first();
        if (doc == null) {
            return new Bank(0);
        }
        return toBank(doc);
    }
    public void update(Bank bank) {
        DB.createOrReplace(fromBank(bank), getGGDB());
    }

    public Bank toBank(Document document){
        return new Bank((int) document.get("money"));
    }
    public Document fromBank(Bank bank){
        return new Document()
                .append("_id","bank")
                .append("money", bank.getMoney());
    }



    public class Entry{
        private final String id;
        private long user_id = -1;
        private long role_id = -1;
        private long guild_id;

        private Command.PermissionLevel level;

        public String getId() {
            return id;
        }
        public long getUser_id() {
            return user_id;
        }
        public void setUser_id(long user_id) {
            this.user_id = user_id;
        }
        public long getRole_id() {
            return role_id;
        }
        public void setRole_id(long role_id) {
            this.role_id = role_id;
        }
        public long getGuild_id() {
            return guild_id;
        }
        public void setGuild_id(long guild_id) {
            this.guild_id = guild_id;
        }
        public Command.PermissionLevel getLevel() {
            return level;
        }
        public void setLevel(Command.PermissionLevel level) {
            this.level = level;
        }

        public Entry(String id) {
            this.id = id;
        }
        public Entry(){
            this.id = UUID.randomUUID().toString();
        }

        public Entry(String id, long user_id, long role_id, long guild_id,Command.PermissionLevel level) {
            this.id = id;
            this.user_id = user_id;
            this.role_id = role_id;
            this.guild_id = guild_id;
            this.level = level;
        }
    }

    public List<Entry> getEntrys(){
        List<Entry> data = new ArrayList<>();
        for (Document document : getPermissionDB().find()){
            data.add(toEntry(document));
        }
        return data;
    }
    public void update(Entry entry){
        DB.createOrReplace(fromEntry(entry),getPermissionDB());
    }

    public Entry toEntry(Document document){
        Command.PermissionLevel level = String.valueOf(document.get("permission")).equals("BOT_ADMIN")?Command.PermissionLevel.BOT_ADMIN:
                String.valueOf(document.get("permission")).equals("MOD")?Command.PermissionLevel.MOD:Command.PermissionLevel.USER;
        return new Entry((String)document.get("_id"),
                document.containsKey("user_id")?(long)document.get("user_id"):-1,
                document.containsKey("role_id")?(long)document.get("role_id"):-1,
                (long)document.get("guild_id"),
                level);
    }
    public Document fromEntry(Entry e){
        return new Document()
                .append("_id",e.getId())
                .append("user_id",e.getUser_id())
                .append("role_id",e.getRole_id())
                .append("guild_id",e.getGuild_id())
                .append("permission",
                        e.getLevel() == Command.PermissionLevel.BOT_ADMIN?"BOT_ADMIN":
                                e.getLevel() == Command.PermissionLevel.MOD?"MOD":"USER");
    }



    //prefix handler. keeps a cache to avoid 50 calls a message
    static Map<Long, String> prefixes;
    static{
        prefixes = new HashMap<>();
    }
    public String getPrefix(long id){
        if(prefixes.containsKey(id))return prefixes.get(id);
        Document doc = DB.getById(id, getGuildsDB());
        if(doc == null || !doc.containsKey("prefix")){
            return "~";
        }
        String str = (String)doc.get("prefix");
        if(str == null || str.equals(""))return "~";
        return str;
    }
    public void resetPrefixCache(){
        prefixes = new HashMap<>();
    }




    @Override
    public String toString(){
        String str = "CARSONBOT:\n";
        for(String collection : getDatabase().listCollectionNames()){
            str+="============================================================\n";
            str+="===================" + collection + "=======================\n";
            str+="============================================================\n";
            for(Document document : getDB(collection).find()){
                str+=DB.toString(document) + "\n";
            }
        }


        return str;
    }





}