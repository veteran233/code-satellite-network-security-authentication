package com.example;

import java.security.Key;

public abstract class __GLOBAL__ {

    public static Key __ID__KEY__ = new Gen().genIdKey();
    public static Key __MAIN__KEY__=new Gen().genMainKey();

    public static void init() {
        IdToAddr.add("LEO1", "127.0.101.114", 27114, 27115);
        IdToAddr.add("GEO1", "127.0.110.114", 30114, 30115);
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
