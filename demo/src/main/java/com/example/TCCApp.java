package com.example;

public class TCCApp {
    public static void main(String[] args) throws Exception {

        TCC tcc = new TCC(
                __GLOBAL__.__TCC_ADDR__,
                __GLOBAL__.__TCC_PORT__);

        tcc.run();
    }
}