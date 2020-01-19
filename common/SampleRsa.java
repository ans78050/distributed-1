package common;

import sun.security.rsa.RSAPublicKeyImpl;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import javax.crypto.Cipher;

public class SampleRsa {
    public static void main(String[] args) throws Exception {
        //System.out.println(new String("this is test".getBytes()));
        // generate public and private keys
        KeyPair keyPair = buildKeyPair();
        PublicKey pubKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        for(byte bb: privateKey.getEncoded()){
            System.out.print(bb + " ");
        }
        System.out.println();
        PublicKey pubKey2 = new RSAPublicKeyImpl(pubKey.getEncoded());
        for(byte bb: pubKey2.getEncoded()){
            System.out.print(bb + " ");
        }
        System.out.println();

//        String plaintext = "This is a secret message";
//        System.out.println("plaintext: " + plaintext);
//        // sign the message
//        byte[] signed = encrypt(privateKey, plaintext);
//        System.out.println("signed: " + new String(signed));  // <<signed message>>
//        // verify the message
//        byte[] verified = decrypt(pubKey2, signed);
//        System.out.println("decode 1: " + new String(verified));     // This is a secret message



        String plaintext = "This is a secret message";
        System.out.println("plaintext: " + plaintext);
        // sign the message
        byte[] signed = encrypt2(pubKey2, plaintext);
        System.out.println("signed: " + new String(signed));  // <<signed message>>
        // verify the message
        byte[] verified = decrypt2(privateKey, signed);
        System.out.println("decode 1: " + new String(verified));     // This is a secret message

//        String s = concat(signed, ",");
//        System.out.println(s);
    }

    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 2048;
        KeyPairGenerator keyPairGenerator =
                KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
    }

    public static byte[] encrypt(PrivateKey privateKey, String message)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(message.getBytes());
    }


    public static byte[] decrypt(PublicKey publicKey, byte[] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(encrypted);
    }

    public static byte[] decrypt2(PrivateKey privateKey, byte[] encrypted)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encrypted);
    }


    public static byte[] encrypt2(PublicKey publicKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(message.getBytes());
    }

    private static String concat(final byte[] bytes, final String delimiter) {
        final StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(b);
            sb.append(delimiter);
        }
        return sb.toString();
    }
}

