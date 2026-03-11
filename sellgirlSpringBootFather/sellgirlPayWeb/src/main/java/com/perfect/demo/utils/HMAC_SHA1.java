package com.perfect.demo.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * HMAC_SHA1 Sign生成器.
 *
 * 需要apache.commons.codec包
 *
 */
public class HMAC_SHA1 {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String secertKey = "WdfwefDfweffsfdsfsfwefsfsd";

    /**
     * 使用 HMAC-SHA1 签名方法对data进行签名
     *
     * @param data
     *            被签名的字符串
     * @param key
     *            密钥
     * @return
    加密后的字符串
     */
    public static String genHMAC(String data, String key) {
        byte[] result = null;
        try {
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKeySpec signinKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            //用给定密钥初始化 Mac 对象
            mac.init(signinKey);
            //完成 Mac 操作
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = Base64.encodeBase64(rawHmac);

        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        } catch (InvalidKeyException e) {
            System.err.println(e.getMessage());
        }
        if (null != result) {
            return new String(result);
        } else {
            return null;
        }
    }
    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) throws Exception{
        String test ="GET&%2fUnicom%2fSNSAPI%2fUser%2fValidateToken&appid=408d8304d4c36da99a" +
                "5c01486fd92254&appkey=Appdsfewfdllldsfwefedsfsfsfsdfd&nonce=NODNUTXQQC&" +
                "signature_algorithm=HMACSHA1&timestamp=1544023159643";
        String genHMAC = genHMAC(test, secertKey+"&8a965c2156724eacb4a25b420ccc9ca7");
        System.out.println(genHMAC.length());
        System.out.println(genHMAC);
    }
}
