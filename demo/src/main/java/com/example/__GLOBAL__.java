package com.example;

import java.security.Key;
import java.security.Security;

import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public abstract class __GLOBAL__ {

    public static final String __TCC_ADDR__ = "127.11.45.14";
    public static final int __TCC_PORT__ = 19198;

    public static Key __ID_KEY__     ;
    public static Key __MAIN_KEY__   ;

    static {
        Security.addProvider(new BouncyCastleProvider());

        byte[] idKey = new byte[] { -91, 108, -48, 20, -35, 61, -113, -54, -72, -43, 113, -94, 106, -21, -70, 17 };
        byte[] mainKey = new byte[] { -27, 42, 37, -2, 100, -58, 112, 30, 65, 9, 37, -52, -73, -63, 9, 26 };

        __ID_KEY__ = new SecretKeySpec(idKey, "SM4");
        __MAIN_KEY__ = new SecretKeySpec(mainKey, "SM4");
    }
}

/*
 * 身份信息         32/128      bits
 * 群组信息         32          bits
 * 密钥             128         bits
 * 随机数           128         bits
 * 时间戳           48          bits
 * 消息验证码       64          bits
 * 响应值           64          bits
 * 标识符           16          bits
 */


/*
    * T_TID||RID --> 6 byte || 4 byte --> 10 byte
    * REQ||TID --> 2 byte || 16 byte --> 18 byte
    * RAND||TxorTK||GID||MAC --> 16 byte || 6 byte || 4 byte || 8 byte --> 34 byte
*/
