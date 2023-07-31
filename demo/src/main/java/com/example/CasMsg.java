package com.example;

public class CasMsg {

    private byte[] __innerByteArr;

    public CasMsg() {
    }

    public CasMsg(byte[] bytes) {

        __innerByteArr = bytes;
    }

    public CasMsg(byte[] bytes1, byte[] bytes2) {

        __innerByteArr = new byte[bytes1.length + bytes2.length];

        int totalLength = 0;

        System.arraycopy(bytes1, 0, __innerByteArr, totalLength, bytes1.length);
        totalLength += bytes1.length;

        System.arraycopy(bytes2, 0, __innerByteArr, totalLength, bytes2.length);
        totalLength += bytes2.length;
    }

    public CasMsg(byte[] bytes1, byte[] bytes2, byte[] bytes3) {

        __innerByteArr = new byte[bytes1.length + bytes2.length + bytes3.length];

        int totalLength = 0;

        System.arraycopy(bytes1, 0, __innerByteArr, totalLength, bytes1.length);
        totalLength += bytes1.length;

        System.arraycopy(bytes2, 0, __innerByteArr, totalLength, bytes2.length);
        totalLength += bytes2.length;

        System.arraycopy(bytes3, 0, __innerByteArr, totalLength, bytes3.length);
        totalLength += bytes3.length;
    }

    public CasMsg(byte[] bytes1, byte[] bytes2, byte[] bytes3, byte[] bytes4) {

        __innerByteArr = new byte[bytes1.length + bytes2.length + bytes3.length + bytes4.length];

        int totalLength = 0;

        System.arraycopy(bytes1, 0, __innerByteArr, totalLength, bytes1.length);
        totalLength += bytes1.length;

        System.arraycopy(bytes2, 0, __innerByteArr, totalLength, bytes2.length);
        totalLength += bytes2.length;

        System.arraycopy(bytes3, 0, __innerByteArr, totalLength, bytes3.length);
        totalLength += bytes3.length;

        System.arraycopy(bytes4, 0, __innerByteArr, totalLength, bytes4.length);
        totalLength += bytes4.length;
    }

    public byte[] getBytes() {

        return __innerByteArr;
    }

    public byte[] get(int startPos, int endPos) {

        byte[] ret = new byte[endPos - startPos + 1];
        System.arraycopy(__innerByteArr, startPos, ret, 0, endPos - startPos + 1);
        return ret;
    }

    public byte[] get(String req) {

        byte[] ret = null;
        if (req == "T_TID") {
            ret = new byte[6];
            System.arraycopy(__innerByteArr, 0, ret, 0, 6);
        } else if (req == "RID") {
            ret = new byte[4];
            System.arraycopy(__innerByteArr, 6, ret, 0, 4);
        } else if (req == "REQ") {
            ret = new byte[2];
            System.arraycopy(__innerByteArr, 0, ret, 0, 2);
        } else if (req == "TID") {
            ret = new byte[16];
            System.arraycopy(__innerByteArr, 2, ret, 0, 16);
        } else if (req == "RAND") {
            ret = new byte[16];
            System.arraycopy(__innerByteArr, 0, ret, 0, 16);
        } else if (req == "TxorTK") {
            ret = new byte[6];
            System.arraycopy(__innerByteArr, 16, ret, 0, 6);
        } else if (req == "GID") {
            ret = new byte[4];
            System.arraycopy(__innerByteArr, 22, ret, 0, 4);
        } else if (req == "MAC") {
            ret = new byte[8];
            System.arraycopy(__innerByteArr, 26, ret, 0, 8);
        } else if (req == "RES") {
            return __innerByteArr;
        } else {
        }
        return ret;
    }
}