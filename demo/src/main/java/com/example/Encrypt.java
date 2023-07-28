package com.example;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public abstract class Encrypt {

    private Key __retKey;
    private KeyGenerator __innerKeyGen;

    private Cipher __crypt;

    private ByteArrayOutputStream __byteOut;
    private CipherOutputStream __cipherOut;

    // the light-weight api of HMAC
    private HMac __innerSM3HMAC = new HMac(new SM3Digest());

    // generate a random key and return it
    protected Key genKey(final String __algorithm) throws Exception {

        Security.addProvider(new BouncyCastleProvider());

        __innerKeyGen = KeyGenerator.getInstance(__algorithm, "BC");
        __innerKeyGen.init(new SecureRandom());

        __retKey = __innerKeyGen.generateKey();

        return __retKey;
    }

    // SM3-HMAC
    protected byte[] sm3HashMAC(final byte[] byteIn) {

        // input data
        __innerSM3HMAC.update(byteIn, 0, byteIn.length);

        // get encryption
        byte[] out = new byte[32];
        __innerSM3HMAC.doFinal(out, 0);

        return out;
    }

    // encrypt the data
    protected byte[] encryptBlockCipher(final String algorithm, final String mode, final String padding, final Key key,
            final byte[] byteIn) throws Exception {

        return __blockCipher(algorithm, mode, padding, key, Cipher.ENCRYPT_MODE, byteIn);
    }

    // decrypt the data
    protected byte[] decryptBlockCipher(final String algorithm, final String mode, final String padding, final Key key,
            final byte[] strIn) throws Exception {

        return __blockCipher(algorithm, mode, padding, key, Cipher.DECRYPT_MODE, strIn);
    }

    private byte[] __blockCipher(final String algorithm, final String mode, final String padding, final Key key,
            final int cryptMode, final byte[] byteIn) throws Exception {

        Security.addProvider(new BouncyCastleProvider());

        // "BC" is the name of the BouncyCastle provider
        __crypt = Cipher.getInstance(algorithm + '/' + mode + '/' + padding, "BC");

        __crypt.init(cryptMode, key);

        __byteOut = new ByteArrayOutputStream();
        __cipherOut = new CipherOutputStream(__byteOut, __crypt);

        __cipherOut.write(byteIn);
        __cipherOut.close();

        return __byteOut.toByteArray();
    }
}