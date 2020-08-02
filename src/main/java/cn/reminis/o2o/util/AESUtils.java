package cn.reminis.o2o.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author sun
 * @date 2020-08-02 17:34
 * @description 对称加密，加密和解密使用的钥匙一致
 */
public class AESUtils {

    private static Logger logger = LoggerFactory.getLogger(AESUtils.class);

    private static final String CIPHER_NAME = "AES/ECB/PKCS5Padding";
    private static String key = "1234567890abcdef";

    //加密
    public static String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            //使用加密模式
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            //通过doFinal()得到加密后的字节数组
            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return new BASE64Encoder().encode(encrypted);
        } catch (Exception e) {
            logger.error("AESUtils encrypt error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //解密
    public static String decrypt(String data) {
        try {
            byte[] decodeBuffer = new BASE64Decoder().decodeBuffer(data);
            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            //使用解密模式
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            //通过doFinal()得到加密后的字节数组
            byte[] decrypted = cipher.doFinal(decodeBuffer);
            return new String(decrypted,StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("AESUtils encrypt error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
