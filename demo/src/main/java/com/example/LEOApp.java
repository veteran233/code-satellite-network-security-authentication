package com.example;

public class LEOApp {
    public G2L_AKA_LEO testleo;
    public G2L_AKA_GEO testgeo;

    public class ThreadLEO extends Thread {
        @Override
        public void run() {
            try {
                testleo = new G2L_AKA_LEO("LEO1", __GLOBAL__.__ID__KEY__, __GLOBAL__.__MAIN__KEY__);
                testleo.firstOperation("GEO1");
            } catch (Exception e) {
                System.err.println(e);
                System.exit(1);
            }
        }
    }

    public class ThreadGEO extends Thread {
        @Override
        public void run() {
            __GLOBAL__.init();
            try {
                testgeo = new G2L_AKA_GEO("GEO1", __GLOBAL__.__ID__KEY__, __GLOBAL__.__MAIN__KEY__);
                testgeo.response();
            } catch (Exception e) {
                System.err.println(e);
                System.exit(1);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ThreadLEO a = new LEOApp().new ThreadLEO();
        ThreadGEO b = new LEOApp().new ThreadGEO();
        b.start();
        Thread.sleep(3000);
        a.start();

        a.join();
        b.join();
    }
}