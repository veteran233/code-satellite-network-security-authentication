package com.example;

import java.net.InetAddress;
import java.util.HashMap;

public class IdToAddr {
    protected class AddrPortPair {
        InetAddress __addr;
        int __sendPort;
        int __recvPort;

        public AddrPortPair(InetAddress __addr, int __sendPort, int __recvPort) {
            this.__addr = __addr;
            this.__sendPort = __sendPort;
            this.__recvPort = __recvPort;
        }

        public InetAddress getAddr() {
            return __addr;
        }

        public int getSendPort() {
            return __sendPort;
        }

        public int getRecvPort() {
            return __recvPort;
        }
    }

    private static HashMap<String, AddrPortPair> __list = new HashMap<String, AddrPortPair>();

    public static AddrPortPair get(String __str) {
        return __list.get(__str);
    }

    public static void add(String __id, String __addr, int __sendPort, int __recvPort) {
        try {
            __list.put(__id, new IdToAddr().new AddrPortPair(InetAddress.getByName(__addr), __sendPort, __recvPort));
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }
}