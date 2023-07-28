package com.example;

import java.security.Key;

public class G2L_AKA_GEO {

    private final Key __ID_KEY;
    private final Key __MAIN_KEY;

    private final String __GEO_ID;

    private Key __authKey;
    private Key __timeKey;
    private Key __commKey;

    private String __timeToken;
    private String __timeAuthKey;
    private String __xres;

    private CasMsg __token;

    private final Gen __GEN;

    private final Net __NET;

    public void response() throws Exception {

        /* wait for require from LEO */
        __NET.initRecv();
        CasMsg recvFromLEOMsg = __NET.recvMsg();

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
        String leoId = new String(new CasMsg(__GEN.genDecryTempId(__ID_KEY, recvFromLEOMsg.get("TID"))).get("RID"));
        __NET.initRecv();
        __NET.sendMsg(leoId, __token);

        /* receive RES from LEO */
        recvFromLEOMsg = __NET.recvMsg();
    }

    public G2L_AKA_GEO(String geoId, Key idKey, Key mainKey) throws Exception {
        __GEO_ID = geoId;
        __ID_KEY = idKey;
        __MAIN_KEY = mainKey;
        __NET = new Net(__GEO_ID);
        __GEN = new Gen();
    }
}