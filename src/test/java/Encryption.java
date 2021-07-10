import javax.crypto.Cipher;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class Encryption
{
    private static String privk = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAObtrf0IuWbcEpW/LsTqnMXQlp5MUuO0VT" +
            "jCwCedFL3q+HYH8wMnXPaaKb4sECi2G1WKfEr4+eulUG0GhUVZtrN96fIdWBIc1XYnDdLwYg24pOFQWhBx56vzASHe7XWQBOVitdONoc" +
            "ex5jJmqggcr0LUk9bmAAtaLpzu1gkGD+wDAgMBAAECgYEA08KGdu9GHIWYYUtElvxRMLCbPaPdqsLWF0u3K1xHBfTvTETz2iOBgv1RY/" +
            "tCAiMkI6fvXEy92RHqE2AUW8jt2CJKPS4aEkteMmx/gQKs5vTdImCMIRJaTnouTVvngdbjwxf8jEbLrVqL0VIqQyJZgFYtvyiEcX3mayh" +
            "Fx5dJWVkCQQD9Bt+fEMd9V4szcSfYXdmp5Gz33tZUigeBfrStrArHSqt/2WPhvKoz0ydFwoiqMHSUoShXmNrWK4S/oclLXT5lAkEA6aRV" +
            "CtiMXdi4WvEYUoQvjUELHh2vEbWcntAsehktE0Ic+MvUmeomThU9+SzTEYGGVVK9DAI3NNGjX7+ym1RGRwJAOPN2G9hr/mNY+pAqlsGw/" +
            "1Nhz3zBoy+aNuRtCHYjyu2col8s4x9S8+0/9qytlBjp9JY9fVHzV6dd7sAcjbEEHQJAAX181V7gNlEFi/7xqsUREJOToJOL5E8GOrUVM4" +
            "opkW/gg225Y2ns6J6WEJrKldf/pVerwQHanEmiAyBT4U2+TQJBAIBpS3Ty3IsVO59ZIgBHnRL16H0aR0ovWjhZFkUupEUGLXtWKzmPKDE6" +
            "SQmVhrFarPaaXlVw/C5EN0A64HyFbUI=";

    private static String pubk = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDm7a39CLlm3BKVvy7E6pzF0JaeTFLjtFU4wsAnnRS96vh2B/" +
            "MDJ1z2mim+LBAothtVinxK+PnrpVBtBoVFWbazfenyHVgSHNV2Jw3S8GINuKThUFoQceer8wEh3u11kATlYrXTjaHHseYyZqoIHK9C1JPW5" +
            "gALWi6c7tYJBg/sAwIDAQAB";

    //private static PrivateKey privateKey;
    //private static PublicKey publicKey;

    public static void mainw(String[] args) throws Exception
    {
        /*
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);

        KeyPair pair = keyGen.generateKeyPair();
        publicKey = pair.getPublic();
        privateKey = pair.getPrivate();

        String pub = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String priv = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        write("pubKey.txt", pub);
        write("privKey.txt", priv);
         */
    }

    private static void write(String path, String str) throws Exception
    {
        File file = new File(path);
        FileWriter writer = new FileWriter(file);
        writer.write(str);
        writer.close();
    }

    public static void main(String[] args) throws Exception
    {
        String message = "a";

        Cipher encr = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encr.init(Cipher.ENCRYPT_MODE, getPublicKey(pubk));
        byte[] data = encr.doFinal(message.getBytes());

        System.out.println(Arrays.toString(data));

        Cipher decr = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        decr.init(Cipher.DECRYPT_MODE, getPrivateKey(privk));
        String destr = new String(encr.doFinal(data));

        System.out.println(destr);
    }

    public static PrivateKey getPrivateKey(String key)
    {
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key.getBytes()));
        KeyFactory keyFactory = null;
        try
        {
            keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(keySpec);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return privateKey;
    }

    public static PublicKey getPublicKey(String base64PublicKey)
    {
        PublicKey publicKey = null;
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
        try
        {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return publicKey;
    }
}
