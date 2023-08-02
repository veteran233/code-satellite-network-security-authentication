package com.example;

public class GEOApp {

    public static GEO geo1;

    public static void main(String[] args) throws Exception {

        geo1 = new GEO(
                "geo1",
                __GLOBAL__.__ID_KEY__,
                __GLOBAL__.__MAIN_KEY__,
                __GLOBAL__.__TCC_ADDR__,
                __GLOBAL__.__TCC_PORT__);

        geo1.response();
    }
}