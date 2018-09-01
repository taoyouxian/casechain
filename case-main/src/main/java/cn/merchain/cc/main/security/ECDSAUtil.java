package cn.merchain.cc.main.security;

import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @version V1.0
 * @Package: cn.merchain.cc.main.security
 * @ClassName: ECDSAUtil
 * @Description:
 * @author: taoyouxian
 * @date: Create in 2018-08-30 18:49
 **/
public class ECDSAUtil {
    private static String str = "hello";

    public static void main(String[] args) {
        jdkECDSA();
    }

    public static void jdkECDSA() {

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            keyPairGenerator.initialize(256);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            String pub = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEPYm+sDFjLRj/RnkDYAWWdev7hLnk\n" +
                    "If5QRZcUtLaFvHVN/gdOOmx82Gkyc3F28SoPmEOKl5eFXsgk5yKTwclYhg==";
            String priv = "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQg6wo2FuI74WGlDdl9\n" +
                    "RYhKnk68EtAbSSm28d3ElEDo7jWhRANCAAQ9ib6wMWMtGP9GeQNgBZZ16/uEueQh\n" +
                    "/lBFlxS0toW8dU3+B046bHzYaTJzcXbxKg+YQ4qXl4VeyCTnIpPByViG";

//            KeyPair keyPair = new KeyPair(pub, priv);

            ECPublicKey ecPublicKey = (ECPublicKey) keyPair.getPublic();
            ECPrivateKey ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();

            // 2.执行签名
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(ecPrivateKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("EC");

            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Signature signature = Signature.getInstance("SHA1withECDSA");
            signature.initSign(privateKey);

            signature.update(str.getBytes());
            byte[] sign = signature.sign();

            // 验证签名
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(ecPublicKey.getEncoded());
            keyFactory = KeyFactory.getInstance("EC");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            signature = Signature.getInstance("SHA1withECDSA");
            signature.initVerify(publicKey);
            signature.update(str.getBytes());

            boolean bool = signature.verify(sign);
            System.out.println(bool);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
