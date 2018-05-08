package com.mgp.common.utils.filter;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;  
import org.bouncycastle.util.encoders.Base64;  
  
public class AESUtil {  
    /** 
     * 密钥算法 java6支持56位密钥，bouncycastle支持64位 
     * */  
	
	private static final String IV_STRING = "yishengxiao_both";
	
	public static String encryptAES(String content, String key)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, UnsupportedEncodingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
			byte[] byteContent = content.getBytes("UTF-8");
			// 注意，为了能与 iOS 统一
			// 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
			byte[] enCodeFormat = key.getBytes();
			SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
			byte[] initParam = IV_STRING.getBytes();
			IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
			// 指定加密的算法、工作模式和填充方式
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
			byte[] encryptedBytes = cipher.doFinal(byteContent);
			// 同样对加密后数据进行 base64 编码
			/*Encoder encoder = Base64.getEncoder();
			return encoder.encodeToString(encryptedBytes);*/
			
			byte[] data = Base64.encode(encryptedBytes);  
	        return new String(data,"UTF-8");  
	}
	public static String decryptAES(String content, String key)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
			// base64 解码
			/*Decoder decoder = Base64.getDecoder();
			byte[] encryptedBytes = decoder.decode(content);*/
			
			
			 byte[] encryptedBytes = Base64.decode(content); 
			
			byte[] enCodeFormat = key.getBytes();
			SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, "AES");
			byte[] initParam = IV_STRING.getBytes();
			IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
			byte[] result = cipher.doFinal(encryptedBytes);
			return new String(result, "UTF-8");
	}
	
   /* public static final String KEY_ALGORITHM = "AES";  
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";  
    //key长度为[128bit(16byte),192bit(24byte)，256bit(32byte)]中的一个  
    public static final String KEY = "yishengxiao_both";  
    public static final SecretKey secretKey  = new SecretKeySpec(KEY.getBytes(), KEY_ALGORITHM);  
    static {  
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());  
    }  
  
    *//** 
     * 加密数据 
     * @param str 
     * @return String 加密后的数据 
     * *//*  
    public static String encrypt(String str) throws Exception {  
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);  
        // 初始化，设置为加密模式  
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);  
        // 执行操作  
        byte[] data = cipher.doFinal(str.getBytes());  
        data = Base64.encode(data);  
        return new String(data,"UTF-8");  
    }  
  
    *//** 
     * 解密数据 
     *  
     * @param str 
     * @return String 解密后的数据 
     * *//*  
    public static String decrypt(String str) throws Exception {  
        byte[] data = Base64.decode(str);  
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);  
        // 初始化，设置为解密模式  
        cipher.init(Cipher.DECRYPT_MODE, secretKey);  
        // 执行操作  
        return new String(cipher.doFinal(data),"UTF-8");  
    }   */   
}
