package com.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Net {

    private class NetThread extends Thread {

        public void threadRecvMsg() throws Exception {

            // init receive data
            final byte[] recvBuf = new byte[256];

            __recvData = new DatagramPacket(recvBuf, recvBuf.length);
            __socket.receive(__recvData);

            final byte[] retBytes = new byte[__recvData.getLength()];
            System.arraycopy(recvBuf, 0, retBytes, 0, __recvData.getLength());

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

    private DatagramSocket __socket;
    private DatagramPacket __sendData;
    private DatagramPacket __recvData;

    private NetThread __thread;

    private CasMsg __recvMsg;

    public Net() throws Exception {

        // init socket
        __socket = new DatagramSocket();
    }

    public void sendMsg(final InetAddress destAddr, final int destPort, final CasMsg msg) throws Exception {

        // init send data
        __sendData = new DatagramPacket(msg.getBytes(), msg.getBytes().length, destAddr, destPort);
        __socket.send(__sendData);
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

    public InetAddress getAddr(){

        return __recvData.getAddress();
    }

    public int getPort(){

        return __recvData.getPort();
    }
}