package com.carson.commands.gg;

public class UserGG {
    private final long id;
    private int money = 0;
    private int educationLevel;

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

    /*

     */

    public int setEduation(int i){
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

    @Override
    public String toString(){
        return getMoney() + GGHandler.GG;
    }
}
