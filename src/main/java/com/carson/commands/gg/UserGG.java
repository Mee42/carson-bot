package com.carson.commands.gg;

public class UserGG {
    private final long id;
    private int money = 0;
    private int educationLevel;
    private int debt = 0;
    private double interest = 0;
    private int coins = 0;
    private int invested = 0;
    private int gotten = 0;



    public UserGG(long id){
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public int getMoney() {
        return money;
    }

    public int setMoney(int money) {
        this.money = money;
        return money;
    }

    public void increaseMoney(int i) {
        money+=i;
    }


    public int setEducation(int i){
        educationLevel = i;
        return educationLevel;
    }

    public int getEducationLevel(){
        return educationLevel;
    }
    public void increaseEducationLevel(int i){
        educationLevel+=i;
    }
    public int getCost(){
        double cost = 100;
        for(int i = 1;i<educationLevel + 1;i++){
            cost = cost * 1.5;
        }
        return (int)cost;
    }
    public int getWork(){
//        double work = (Math.random()*100);
        double work = 100;
        for(int i = 1;i<educationLevel;i++){
            work = (work * 1.2);
        }
        return (int)work;
    }

    public String toString(double worth){
        return GGHandler.condense(money)+ " " + educationLevel + " edu level\n" +
                GGHandler.condense(debt) + " debt with " + (int)(interest*100) + "% interest\n" +
                coins + " BTC worth " + GGHandler.condense((int) (worth * coins)) + "\n" +
                "has invested: " + GGHandler.condense(invested) + " and gotten " + GGHandler.condense(gotten) + "\n" +
                "profit from BTC: " + GGHandler.condense(gotten - invested);
    }

    public void midnight() {
        money = (int)(money  * Taxation.TAX_AMOUNT);
        if(debt != 0){
            debt = (int)(debt + (debt* interest));
        }
    }
    
    public void loan(int amount, double interest){
        this.interest += interest;
        this.debt+=amount;
        this.money+=amount;
        debt*=money;
    }

    public void payBack(int amountToPayBack){
        this.money-=amountToPayBack;
        this.debt-=amountToPayBack;
        if(debt == 0){
            interest = 0;
        }
    }














    public void setEducationLevel(int educationLevel) {
        this.educationLevel = educationLevel;
    }
    public int getInvested() {
        return invested;
    }
    public void setInvested(int invested) {
        this.invested = invested;
    }
    public int getGotten() {
        return gotten;
    }
    public void setGotten(int gotten) {
        this.gotten = gotten;
    }
    public int getDebt() {
        return debt;
    }
    public void setDebt(int debt) {
        this.debt = debt;
    }
    public double getInterest() {
        return interest;
    }
    public void setInterest(double interest) {
        this.interest = interest;
    }
    public int getCoins() {
        return coins;
    }
    public void setCoins(int coins) {
        this.coins = coins;
    }
}
