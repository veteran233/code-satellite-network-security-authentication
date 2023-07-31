package com.example;

public class LEOApp {

    public static LEO leo1;
    public static GEO geo1;

    public static void main(String[] args) throws Exception {

        Thread threadLEO1 = new Thread(() -> {
            try {
                leo1 = new LEO("leo1", __GLOBAL__.__ID__KEY__, __GLOBAL__.__MAIN__KEY__, "127.11.45.14", 19198, "geo1");
                leo1.firstOperation();
            } catch (Exception e) {
                System.err.println(e);
                System.exit(1);
            }
        });

        Thread threadGEO1 = new Thread(() -> {
            try {
                geo1 = new GEO("geo1", __GLOBAL__.__ID__KEY__, __GLOBAL__.__MAIN__KEY__, "127.11.45.14", 19198);
                geo1.response();
            } catch (Exception e) {
                System.err.println(e);
                System.exit(1);
            }
        });

        threadGEO1.start();
        Thread.sleep(3000);
        threadLEO1.start();

        threadGEO1.join();
        threadLEO1.join();
    }
}