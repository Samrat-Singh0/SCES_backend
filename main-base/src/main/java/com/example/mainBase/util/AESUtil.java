package com.example.mainBase.util;

import com.example.mainBase.dto.ReportRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
  private static final String KEY_ALGORITHM = "AES";
  private static final ObjectMapper objectMapper = new ObjectMapper();


  public static String encrypt(String plainText, String secretKey)
      throws Exception {
    Cipher cipher = Cipher.getInstance(KEY_ALGORITHM + "/GCM/NoPadding");     //Crete Cipher object for AES in GCM Mode
    GCMParameterSpec iv = generateIv();
    cipher.init(Cipher.ENCRYPT_MODE, generateKey(secretKey), iv);         //Initialize the cipher for encrypt mode. Takes secret key and iv.
    byte[] encrypted = cipher.doFinal(plainText.getBytes());                        //Encrypt the plain text by converting it into byte[] using the created cipher object.
    ByteBuffer byteBuffer = ByteBuffer.allocate(iv.getIV().length + encrypted.length);      //GCM mode ma transmit garda, iv pani pathaune
    byteBuffer.put(iv.getIV());
    byteBuffer.put(encrypted);
    return Base64.getEncoder().encodeToString(byteBuffer.array());                 //kunai kunai encrypted texts haru print hudaina, so Base64 is used to encode it for safe storage or transmission.
  }

  public static String encryptObject(ReportRequestDto requestDto, String secretKey)
      throws Exception {
    String json = objectMapper.writeValueAsString(requestDto);          //Convert Json Object to string
    return encrypt(json, secretKey);
  }

  private static SecretKeySpec generateKey(String secretKey){
    return new SecretKeySpec(secretKey.getBytes(), KEY_ALGORITHM);
  }

  public static GCMParameterSpec generateIv() {
    byte[] iv = new byte[12];
    new SecureRandom().nextBytes(iv);
    return new GCMParameterSpec(128, iv);
  }

}
