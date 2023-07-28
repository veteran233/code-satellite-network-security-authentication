package com.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Net {

    private class NetThread extends Thread {
        public void threadRecvMsg() throws Exception {

            // init receive data
            final byte[] recvBytes = new byte[1024];

            __recvDate = new DatagramPacket(recvBytes, recvBytes.length);
            __recvSocket.receive(__recvDate);

            byte[] retBytes = new byte[__recvDate.getLength()];
            System.arraycopy(recvBytes, 0, retBytes, 0, __recvDate.getLength());

            // byte[] to CasMsg
            __recvMsg = new CasMsg(retBytes);
        }

        @Override
        public void run() {
            try {
                threadRecvMsg();
            } catch (Exception e) {
                System.err.println(e);
                System.exit(1);
            }
        }
    }

    private DatagramSocket __sendSocket;
    private DatagramPacket __sendData;
    private DatagramSocket __recvSocket;
    private DatagramPacket __recvDate;

    private NetThread __thread;

    private CasMsg __recvMsg;

    public Net(final String id) throws Exception {

        // init socket
        __sendSocket = new DatagramSocket(IdToAddr.get(id).getSendPort(), IdToAddr.get(id).getAddr());
        __recvSocket = new DatagramSocket(IdToAddr.get(id).getRecvPort(), IdToAddr.get(id).getAddr());
    }

    public void sendMsg(final String id, final CasMsg msg) throws Exception {

        // init send data
        __sendData = new DatagramPacket(msg.getBytes(), msg.getBytes().length, IdToAddr.get(id).getAddr(),
                IdToAddr.get(id).getRecvPort());

        __sendSocket.send(__sendData);
    }

    public void initRecv() {

        // init thread
        __thread = new NetThread();
        __thread.start();
    }

    public CasMsg recvMsg() throws Exception {
        __thread.join();
        return __recvMsg;
    }
}