package org.zeroturnaround.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;

import sun.security.pkcs.PKCS8Key;

public class KeyUtil {
  public static void main(String[] args) throws Exception {
    KeyPair keyPair = null;

    // Test
    keyPair = readPkcs1(new FileInputStream("/home/juri/tmp/private.pem"));
    //		System.out.println(rsaKeyPair.getPrivate());
    //		System.out.println(rsaKeyPair.getPublic());

    Signature signature = Signature.getInstance("SHA1withRSA");
    signature.initSign(keyPair.getPrivate());
    signature.update("foo".getBytes());
    byte[] signatureBytes = signature.sign();

    keyPair = readPkcs8(new FileInputStream("/home/juri/tmp/private.pk8der"));
    //		System.out.println(rsaKeyPair.getPrivate());
    //		System.out.println(rsaKeyPair.getPublic());

    signature.initVerify(keyPair.getPublic());
    signature.update("foo".getBytes());
    System.out.println(signature.verify(signatureBytes));
  }

  public static KeyPair readPkcs1(InputStream in) throws IOException {
    // openssl genrsa -out private.pem 2048

    if (Security.getProvider("BC") == null) {
      Security.addProvider(new BouncyCastleProvider());
    };

    PEMReader pemReader = new PEMReader(new InputStreamReader(in));
    return (KeyPair) pemReader.readObject();
  }

  public static KeyPair readPkcs8(InputStream in) throws Exception  {
    // openssl genrsa -out private.pem 2048
    // openssl pkcs8 -topk8 -in private.pem -out private.pk8der -outform DER -nocrypt

    byte[] keyBytes = readAll(in);

    InputStream byteIn = new ByteArrayInputStream(keyBytes);
    Object obj = new PEMReader(new InputStreamReader(byteIn)).readObject();
    if (obj instanceof RSAPrivateCrtKey) {
      return toKeyPair((RSAPrivateCrtKey) obj);
    }
    // TODO: add DSA (maybe EC) handler

    // DER-encoded file
    PKCS8Key pkcs8 = new PKCS8Key();
    pkcs8.decode(keyBytes);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8.getEncoded());

    String keyAlgorithm = pkcs8.getAlgorithm(); 
    if ("RSA".equals(keyAlgorithm)) {
      return toKeyPair(keySpec);
    } // TODO: add DSA (maybe EC) handler
    else {
      throw new InvalidKeyException("Algorithm not supported: " + keyAlgorithm);
    }
  }



  private static byte[] readAll(InputStream in) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] buffer = new byte[8192];

    int bytesRead = 0;
    while ((bytesRead = in.read(buffer)) > 0) {
      out.write(buffer, 0, bytesRead);
    }

    return out.toByteArray();
  }


  private static KeyPair toKeyPair(RSAPrivateCrtKey privateKey) throws Exception {
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");

    RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(
        privateKey.getModulus(), privateKey.getPublicExponent());
    PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

    return new KeyPair(publicKey, privateKey);
  }

  private static KeyPair toKeyPair(KeySpec keySpec) throws Exception {
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");

    RSAPrivateCrtKey privateKey = (RSAPrivateCrtKey) keyFactory.generatePrivate(keySpec);

    RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(
        privateKey.getModulus(), privateKey.getPublicExponent());
    PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

    return new KeyPair(publicKey, privateKey);
  }
}
