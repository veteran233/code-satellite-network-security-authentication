package com.example;

public class LEOApp {

    public static LEO leo1;

    public static void main(String[] args) throws Exception {

        leo1 = new LEO(
                "leo1",
                __GLOBAL__.__ID_KEY__,
                __GLOBAL__.__MAIN_KEY__,
                __GLOBAL__.__TCC_ADDR__,
                __GLOBAL__.__TCC_PORT__,
                "geo1");

        leo1.firstOperation();
    }
}