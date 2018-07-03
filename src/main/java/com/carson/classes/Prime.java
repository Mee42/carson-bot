package com.carson.classes;

import java.util.Arrays;

public class Prime {
    private static Prime ourInstance = new Prime();

    public static Prime getInstance() {
        return ourInstance;
    }
//
    private boolean[] numbers = new  boolean[2_100_000_000];
//    private boolean[] numbers = new boolean[Integer.MAX_VALUE - 1]; //throws OutOfMemoryError
    private boolean done;
    private Prime() {
        done = false;
    }

    public void initulize(){
        Arrays.fill(numbers, true);
        numbers[0] = false;
        numbers[1] = false;
        numbers[2] = true;
        filter(2);
        for(int i = 3;i<numbers.length / 2;i++){
            if(numbers[i]){
                filter(i);
            }
        }
        System.out.println("Prime number sieve complete");
        done = true;
    }


    private void filter(int n){
        for(int i= (int)java.lang.Math.pow(n,2);i< numbers.length ;i+= n){
            numbers[i] = false;
        }
    }

    public boolean isDone(){
        return done;
    }
    public boolean isPrime(int n){
        if(!done){
            return false;
        }
        return numbers[n];
    }

}
