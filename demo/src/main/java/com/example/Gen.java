package com.example;

import java.security.Key;

public class Gen extends Encrypt {

    public byte[] genEncryTempId(Key key, byte[] msg) throws Exception {

        return super.encryptBlockCipher("SM4", "ECB", "PKCS5Padding", key, msg);
    }

    public byte[] genDecryTempId(Key key, byte[] msg) throws Exception {

        return super.decryptBlockCipher("SM4", "ECB", "PKCS5Padding", key, msg);
    }

    public Key genIdKey() {

        try {
            return super.genKey("SM4");
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        return null;
    }

    public Key genMainKey() {

        try {
            return super.genKey("SM4");
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        return null;
    }

    public Key genAuthKey(Key key, String timeStamp) {

        return key;
    }

    public Key genTimestampKey(Key key, String rand) {

        return key;
    }

    public byte[] genMAC(Key key, byte[] msg) throws Exception {

        msg = super.sm3HashMAC(msg);
        byte[] retMsg = new byte[8];
        System.arraycopy(msg, 0, retMsg, 0, 8);
        return retMsg;
    }

    public Key genCommunicationKey(Key key, byte[] rand) {

        return key;
    }

    public String genRes(Key ck, byte[] rand) {

        return "res__res";
    }
}