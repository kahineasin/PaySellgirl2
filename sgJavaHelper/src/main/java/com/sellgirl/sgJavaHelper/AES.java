package com.sellgirl.sgJavaHelper;

 
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
 
/**
 * Java使用AES加密算法进行加密解密
 * https://blog.csdn.net/m0_58111460/article/details/131146689
 * */
public class AES {
    /**
     * AES算法加密
     * @Param:text原文
     * @Param:key密钥
     * */
    public static String AESEncryptDemo(String text,String key) throws Exception {
        // 创建AES加密算法实例(根据传入指定的秘钥进行加密)
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
 
        // 初始化为加密模式，并将密钥注入到算法中
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
 
        // 将传入的文本加密
        byte[] encrypted = cipher.doFinal(text.getBytes());
 
        //生成密文
        // 将密文进行Base64编码，方便传输
        return Base64.getEncoder().encodeToString(encrypted);
    }
 
    /**
     * AES算法解密
     * @Param:base64Encrypted密文
     * @Param:key密钥
     * */
    public  static String AESDecryptDemo(String base64Encrypted,String key)throws Exception{
        // 创建AES解密算法实例
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
 
        // 初始化为解密模式，并将密钥注入到算法中
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
 
        // 将Base64编码的密文解码
        byte[] encrypted = Base64.getDecoder().decode(base64Encrypted);
 
        // 解密
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted);
    }
    //@Test
    public void AESTest() throws Exception {
        //明文
        String text="从大漠孤烟塞北，到杏花春雨江南，从山水田园牧歌，到金戈铁马阳关，我们在吟诵着千古名句，我们也在体味着人间百态";
        //秘钥(需要使用长度为16、24或32的字节数组作为AES算法的密钥，否则就会遇到java.security.InvalidKeyException异常)
        String key="20230610HelloDog";
        //加密，生成密文
        String base64Encrypted = AESEncryptDemo(text,key);
 
        System.out.println(base64Encrypted);
        //解密，获取明文
        String text2 = AESDecryptDemo(base64Encrypted,key);
 
        System.out.println(text2);
    }
}