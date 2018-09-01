package cn.merchain.cc.main.security;

import javax.crypto.Cipher;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

/**
 * @version V1.0
 * @Package: cn.merchain.cc.main.security
 * @ClassName: RSAUtils
 * @Description:
 * @author: taoyouxian
 * @date: Create in 2018-08-25 22:23
 **/
public class RSAUtils {
    private static String PUBLIC_KEY_PATH = "H:\\4b095ba0c928f4fc9046db579950ffa7210b94cf93c9e04cb76a69e849c77c19-pub";
    private static String PRIVATE_KEY_PATH = "H:\\4b095ba0c928f4fc9046db579950ffa7210b94cf93c9e04cb76a69e849c77c19-priv";

    private static String PUBLIC_KEY = "H:\\-pub";
    private static String PRIVATE_KEY = "H:\\-priv";

    /**
     * 生成私钥  公钥
     */
    public static void geration() {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom secureRandom = new SecureRandom(new Date().toString().getBytes());
            keyPairGenerator.initialize(1024, secureRandom);
            KeyPair keyPair = keyPairGenerator.genKeyPair();
            byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
            System.out.println("publicKeyBytes: " + keyPair.getPublic().toString());
            FileOutputStream fos = new FileOutputStream(PUBLIC_KEY);
            fos.write(publicKeyBytes);
            fos.close();
            byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
            System.out.println("privateKeyBytes: " + keyPair.getPrivate().toString());
            fos = new FileOutputStream(PRIVATE_KEY);
            fos.write(privateKeyBytes);
            fos.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取公钥
     *
     * @param filename
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String filename) throws Exception {
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    /**
     * 获取私钥
     *
     * @param filename
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String filename) throws Exception {
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public static void main(String[] args) {
        geration();

        String input = "!!!hello world!!!";
        RSAPublicKey pubKey;
        RSAPrivateKey privKey;
        byte[] cipherText;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("RSA");
            pubKey = (RSAPublicKey) getPublicKey(PUBLIC_KEY);
            privKey = (RSAPrivateKey) getPrivateKey(PRIVATE_KEY);

            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            cipherText = cipher.doFinal(input.getBytes());
            //加密后的东西
            System.out.println("cipher: " + new String(cipherText));
            //开始解密
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            byte[] plainText = cipher.doFinal(cipherText);
            System.out.println("publickey: " + Base64.getEncoder().encode(cipherText));
            System.out.println("plain : " + new String(plainText));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
