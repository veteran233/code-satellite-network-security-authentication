package com.example;

public class TCCApp {
    public static void main(String[] args) throws Exception {

        TCC tcc = new TCC("127.11.45.14", 19198);
        tcc.run();
    }
}