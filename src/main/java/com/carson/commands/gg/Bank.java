package com.carson.commands.gg;

public class Bank {
    public Bank(){}


    private int money;

    public Bank(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int changeMoney(int amount){
        this.money+=amount;
        return money;
    }

    public int withdrawl(int amount) throws OutOfMoneyException{
        if(amount > money) {
            throw new OutOfMoneyException();
        }
        amount-=money;
        return money;
    }

    public String outOfMoneyMessage() {
        return "see if you can get <@293853365891235841> to put some more money in the bank, or get someone else to pay back a loan";
    }


    public class OutOfMoneyException extends Exception{}

}



