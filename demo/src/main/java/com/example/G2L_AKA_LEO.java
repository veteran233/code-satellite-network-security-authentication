package com.example;

import java.security.Key;

public class G2L_AKA_LEO {

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

    private CasMsg __token;

    private final Gen __GEN;

    private final Net __NET;

    public void firstOperation(String geoId) throws Exception {

        /* get timestamp of TID and generate TID 
         * T_TID||RID --> 6 byte || 4 byte --> 10 byte
        */
        __timeTempId = GetTimestamp.get();
        __tempId = __GEN.genEncryTempId(__ID_KEY, new CasMsg(__timeTempId.getBytes(), __LEO_ID.getBytes()).getBytes());

        /* send REQ||TID to GEO 
         * REQ||TID --> 2 byte || 16 byte --> 18 byte
        */
        __NET.initRecv();
        __NET.sendMsg(geoId, new CasMsg(__REQUIRE.getBytes(), __tempId));

        /* receive token from GEO */
        __token = __NET.recvMsg();

        /* generate AuthKey, check token, generate CK and RES */
        __timeAuthKey = GetTimestamp.get();
        __authKey = __GEN.genAuthKey(__MAIN_KEY, __timeAuthKey);
        __commKey = __GEN.genCommunicationKey(__authKey, __token.get("RAND"));
        __res = __GEN.genRes(__commKey, __token.get("RAND"));

        /* send RES to GEO */
        __NET.sendMsg(geoId, new CasMsg(__res.getBytes()));
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

    public void switchOperation(String geoId) throws Exception {

        /* try to send predict message, use __firstOperation() if it failed */
        try {
            __NET.sendMsg(geoId, new CasMsg(__REQUIRE.getBytes(), __tempId, __res.getBytes()));
        } catch (Exception e) {
            if (e.getMessage() == "Failed") {
                firstOperation(geoId);
                return;
            }
        }

        /* receive token from GEO */
        __token = __NET.recvMsg();

        /* check token */

        /* generate CK */
        __commKey = __GEN.genCommunicationKey(__authKey, __token.get("RAND"));
    }

    public G2L_AKA_LEO(String __realId, Key __idKey, Key __mainKey) throws Exception {
        __LEO_ID = __realId;
        __ID_KEY = __idKey;
        __MAIN_KEY = __mainKey;
        __REQUIRE = "rq";
        __NET = new Net(__LEO_ID);
        __GEN = new Gen();
    }
}