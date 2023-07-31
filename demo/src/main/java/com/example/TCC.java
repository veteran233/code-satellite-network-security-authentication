package com.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCC {

    private static class SatelliteStatus {

        public Boolean inNet;
        public InetAddress addr;
        public int port;

        public SatelliteStatus(Boolean inNet, InetAddress addr, int port) {

            this.inNet = inNet;
            this.addr = addr;
            this.port = port;
        }
    }

    public final static int TCC_ADD = 0;
    public final static int TCC_ADDR = 1;
    public final static int TCC_PORT = 2;

    private DatagramSocket __socket;

    private HashMap<String, SatelliteStatus> __satelliteList = new HashMap<>();
    private ExecutorService __threadPool = Executors.newCachedThreadPool();

    public TCC(String addr, int port) throws Exception {

        __socket = new DatagramSocket(port, InetAddress.getByName(addr));
    }

    public void add(String id, InetAddress addr, int port) {

        synchronized (this) {
            __satelliteList.put(id, new SatelliteStatus(false, addr, port));
        }

        System.out.println("TCC (" + this.hashCode() + ") Message: " + id + "@" + addr.getHostAddress() + ":" + port
                + " has been added into TCC Satellite List");
    }

    public void getAddr(String queryId, InetAddress addr, int port) throws Exception {

        SatelliteStatus queryStatus;

        DatagramPacket sendData;

        synchronized (this) {
            queryStatus = __satelliteList.get(queryId);
        }

        sendData = new DatagramPacket(queryStatus.addr.getAddress(), queryStatus.addr.getAddress().length, addr, port);
        __socket.send(sendData);

        System.out.println("TCC (" + this.hashCode() + ") Message: " + addr.getHostAddress() + ":" + port
                + " query IP Address of " + queryId);
    }

    public void getPort(String queryId, InetAddress addr, int port) throws Exception {

        SatelliteStatus queryStatus;

        DatagramPacket sendData;

        synchronized (this) {
            queryStatus = __satelliteList.get(queryId);
        }

        final byte[] sendBuf = new byte[2];
        sendBuf[0] = (byte) ((queryStatus.port & (0xff00)) >> 8);
        sendBuf[1] = (byte) (queryStatus.port & (0x00ff));

        sendData = new DatagramPacket(sendBuf, sendBuf.length, addr, port);
        __socket.send(sendData);

        System.out.println("TCC (" + this.hashCode() + ") Message: " + addr.getHostName() + ":" + port
                + " query Port of " + queryId);
    }

    public void run() throws Exception {

        while (true) {
            final CasMsg recvMsg = new CasMsg(new byte[5]);

            DatagramPacket recvData = new DatagramPacket(recvMsg.getBytes(), recvMsg.getBytes().length);
            __socket.receive(recvData);

            final byte flag = recvMsg.getBytes()[0];

            final String queryId = new String(recvMsg.get(1, 4));

            if (flag == TCC_ADD) {
                __threadPool.execute(() -> {
                    add(queryId, recvData.getAddress(), recvData.getPort());
                });

            } else if (flag == TCC_ADDR) {
                __threadPool.execute(() -> {
                    try {
                        getAddr(queryId, recvData.getAddress(), recvData.getPort());
                    } catch (Exception e) {
                        System.err.println(e);
                        System.exit(1);
                    }
                });

            } else if (flag == TCC_PORT) {
                __threadPool.execute(() -> {
                    try {
                        getPort(queryId, recvData.getAddress(), recvData.getPort());
                    } catch (Exception e) {
                        System.err.println(e);
                        System.exit(1);
                    }
                });

            }
        }
    }
}