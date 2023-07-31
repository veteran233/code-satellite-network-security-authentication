package com.example;

import java.net.InetAddress;
import java.security.Key;

public class GEO {

    private final Key __ID_KEY;
    private final Key __MAIN_KEY;

    private final String __GEO_ID;

    private Key __authKey;
    private Key __timeKey;
    private Key __commKey;

    private String __timeToken;
    private String __timeAuthKey;
    private String __xres;

    private InetAddress __destAddr;
    private int __destPort;

    private CasMsg __token;

    private final Gen __GEN = new Gen();

    private final Net __NET = new Net();

    public void response() throws Exception {

        /* wait for require from LEO */
        __NET.initRecv();
        CasMsg recvFromLEOMsg = __NET.recvMsg();

        /* decrypt the TID and get RID of the LEO */
        String leoId = new String(new CasMsg(__GEN.genDecryTempId(__ID_KEY, recvFromLEOMsg.get("TID"))).get("RID"));

        /* set IP Address/Port of the LEO */
        __destAddr = __NET.getAddr();
        __destPort = __NET.getPort();
        System.out.println("GEO (" + this.hashCode() + ") Message : receive Require from " + leoId + "@"
                + __destAddr.getHostAddress() + ":" + __destPort);

        /* generate AuthKey */
        __timeAuthKey = GetTimestamp.get();
        __authKey = __GEN.genAuthKey(__MAIN_KEY, __timeAuthKey);

        /* generate TK */
        String rand = GetRand.get();
        __timeKey = __GEN.genTimestampKey(__authKey, rand);

        /* generate MAC */
        __timeToken = GetTimestamp.get();
        byte[] mac = __GEN.genMAC(__authKey,
                new CasMsg(rand.getBytes(), __timeToken.getBytes(), __GEO_ID.getBytes()).getBytes());

        /* generate Token */
        __token = new CasMsg(rand.getBytes(), __timeToken.getBytes(), __GEO_ID.getBytes(), mac);

        /* generate CK and XRES */
        __commKey = __GEN.genCommunicationKey(__authKey, rand.getBytes());
        __xres = __GEN.genRes(__commKey, rand.getBytes());

        /* send token to LEO */
        __NET.initRecv();
        __NET.sendMsg(__destAddr, __destPort, __token);

        /* receive RES from LEO */
        recvFromLEOMsg = __NET.recvMsg();
        System.out.println("GEO (" + this.hashCode() + ") Message : receive Result from " + leoId + "@"
                + __destAddr.getHostAddress() + ":" + __destPort);
    }

    public GEO(String geoId, Key idKey, Key mainKey, String addr, int port) throws Exception {

        __GEO_ID = geoId;
        __ID_KEY = idKey;
        __MAIN_KEY = mainKey;

        InetAddress tempAddr = InetAddress.getByName(addr);

        __NET.sendMsg(tempAddr, port, new CasMsg(new byte[] { TCC.TCC_ADD }, geoId.getBytes()));
    }
}