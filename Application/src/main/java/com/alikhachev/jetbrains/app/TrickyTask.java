package com.alikhachev.jetbrains.app;

public class TrickyTask implements Runnable {
    @Override
    public void run() {
        System.out.println("I'm doing some tricky calculations. May be...");
    }
}
