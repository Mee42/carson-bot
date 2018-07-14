package com.carson.commands.gg;

public class UserGG {
    private final long id;
    private int money = 0;

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
}
