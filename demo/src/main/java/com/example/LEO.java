package com.example;

import java.net.InetAddress;
import java.security.Key;

public class LEO {

    private final Key __ID_KEY;
    private final Key __MAIN_KEY;

    private final String __LEO_ID;
    private final String __REQUIRE;

    private Key __authKey;
    private Key __commKey;

    private String __timeTempId;
    private String __timeAuthKey;
    private byte[] __tempId;
    private String __res;

    private InetAddress __destAddr;
    private int __destPort;

    private CasMsg __token;

    private final Gen __GEN = new Gen();

    private final Net __NET = new Net();

    public void firstOperation() throws Exception {

        /*
         * get timestamp of TID and generate TID
         * T_TID||RID --> 6 byte || 4 byte --> 10 byte
         */
        __timeTempId = GetTimestamp.get();
        __tempId = __GEN.genEncryTempId(__ID_KEY, new CasMsg(__timeTempId.getBytes(), __LEO_ID.getBytes()).getBytes());

        /*
         * send REQ||TID to GEO
         * REQ||TID --> 2 byte || 16 byte --> 18 byte
         */
        __NET.initRecv();
        __NET.sendMsg(__destAddr, __destPort, new CasMsg(__REQUIRE.getBytes(), __tempId));

        /* receive token from GEO */
        __token = __NET.recvMsg();
        System.out.println("LEO (" + this.hashCode() + ") Message : receive Token from "
                + new String(__token.get("GID")) + "@" + __destAddr.getHostAddress() + ":" + __destPort);

        /* generate AuthKey, check token, generate CK and RES */
        __timeAuthKey = GetTimestamp.get();
        __authKey = __GEN.genAuthKey(__MAIN_KEY, __timeAuthKey);
        __commKey = __GEN.genCommunicationKey(__authKey, __token.get("RAND"));
        __res = __GEN.genRes(__commKey, __token.get("RAND"));

        /* send RES to GEO */
        __NET.sendMsg(__destAddr, __destPort, new CasMsg(__res.getBytes()));
    }

    public void preCal() throws Exception {

        /* predict timestamp */
        __timeTempId = GetTimestamp.get();
        __timeAuthKey = GetTimestamp.get();

        /* update TID, AuthKey and RES */
        __tempId = __GEN.genEncryTempId(__ID_KEY, new CasMsg(__timeTempId.getBytes(), __LEO_ID.getBytes()).getBytes());
        __authKey = __GEN.genAuthKey(__MAIN_KEY, __timeAuthKey);
        __res = __GEN.genRes(__GEN.genCommunicationKey(__authKey, __token.get("RAND")), __token.get("RAND"));
    }

    public void switchOperation() throws Exception {

        /* try to send predict message, use __firstOperation() if it failed */
        try {
            __NET.sendMsg(__destAddr, __destPort, new CasMsg(__REQUIRE.getBytes(), __tempId, __res.getBytes()));
        } catch (Exception e) {
            if (e.getMessage() == "Failed") {
                firstOperation();
                return;
            }
        }

        /* receive token from GEO */
        __token = __NET.recvMsg();

        /* check token */

        /* generate CK */
        __commKey = __GEN.genCommunicationKey(__authKey, __token.get("RAND"));
    }

    public LEO(String realId, Key idKey, Key mainKey, String addr, int port, String geoId) throws Exception {

        __LEO_ID = realId;
        __ID_KEY = idKey;
        __MAIN_KEY = mainKey;
        __REQUIRE = "rq";

        InetAddress tempAddr = InetAddress.getByName(addr);

        __NET.sendMsg(tempAddr, port, new CasMsg(new byte[] { TCC.TCC_ADD }, realId.getBytes()));

        __NET.initRecv();
        __NET.sendMsg(tempAddr, port, new CasMsg(new byte[] { TCC.TCC_ADDR }, geoId.getBytes()));
        __destAddr = InetAddress.getByAddress(__NET.recvMsg().getBytes());

        __NET.initRecv();
        __NET.sendMsg(tempAddr, port, new CasMsg(new byte[] { TCC.TCC_PORT }, geoId.getBytes()));
        __destPort = ((__NET.recvMsg().getBytes()[0] & 0xff) << 8) + (__NET.recvMsg().getBytes()[1] & 0xff);
    }
}