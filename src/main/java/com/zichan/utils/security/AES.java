package com.zichan.utils.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.util.Base64Utils;

public class AES {
	/**
	 * 分组模式：
	 * 	1.EBC模式：简单；有利于并行计算；误差不会被传送；不能隐藏明文；可能对明文进行主动攻击
	 *  2.CBC模式：不容易主动攻击,安全性好于ECB,适合传输长度长的报文,是SSL、IPSec的标准。不利于并行计算；误差传递；需要初始化向量IV
	 *  3.CFB模式：隐藏了明文；分组密码转化为流模式；可以及时加密传送小于分组的数据；不利于并行计算；误差传送：一个明文单元损坏影响多个单元；唯一的IV
	 *  4.OFB模式，OFB模式又称输出反馈模式：隐藏了明文；分组密码转化为流模式；可以及时加密传送小于分组的数据；不利于并行计算；对明文的主动攻击是可能的；误差传送：一个明文单元损坏影响多个单元。
	 *  5.CTR模式：CTR 模式被广泛用于 ATM 网络安全和 IPSec应用中，能有效利于硬件性能，简单，不要求实现解密，无填充，可以高效地作为流式加密使用
	 * 填充模式：NoPadding（不填充），ZerosPadding（零填充）， PKCS5Padding（总字节数填充）
	 * 初始化向量：
	 */
	private static final String algorithm = "AES";
    private static final String transform = "AES/CBC/PKCS5Padding";
    private static final byte[] iv = "68656C6C6F646169".getBytes();
    private static final String passKey = "130269F7B8B0C093B5FD3C50D7F4C384";

    public static byte[] encrypt(byte[] input, String key) throws Exception{
    	key = key == null ? passKey : key;
        // 判断Key是否为16位  
        if (key.length() % 16 != 0) {  
        	throw new RuntimeException("加密密钥必须为16的倍数！"); 
        }  
    	Cipher cipher = Cipher.getInstance(transform);
    	SecretKey  secretKey = new SecretKeySpec(key.getBytes("UTF-8"), algorithm);
    	cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
    	return Base64Utils.encode(cipher.doFinal(input));
    }
    
    public static byte[] decrypt(byte[] src, String key) throws Exception{
    	key = key == null ? passKey : key;
    	byte[] input = Base64Utils.decode(src);
    	SecretKey securekey = new SecretKeySpec(key.getBytes("UTF-8"), algorithm);
    	Cipher cipher = Cipher.getInstance(transform);
    	cipher.init(Cipher.DECRYPT_MODE, securekey, new IvParameterSpec(iv));   
        return cipher.doFinal(input);
    }
    public static void main(String[] args) {  
        getKey();  
        getKeyByPass();  
    }  
      
    /** 
     * 随机生成秘钥 
     */  
    public static void getKey(){    
        try {    
            KeyGenerator kg = KeyGenerator.getInstance("AES");    
            kg.init(128);//要生成多少位，只需要修改这里即可128, 192或256    
            SecretKey sk = kg.generateKey();    
            byte[] b = sk.getEncoded();    
            String s = byteToHexString(b);    
            System.out.println(s);    
            System.out.println("十六进制密钥长度为"+s.length());    
            System.out.println("二进制密钥的长度为"+s.length()*4);    
        } catch (NoSuchAlgorithmException e) {    
            e.printStackTrace();    
            System.out.println("没有此算法。");    
        }    
    }    
      
    /** 
     * 使用指定的字符串生成秘钥 
     */  
    public static void getKeyByPass(){  
        //生成秘钥  
//        String password="testkey";  
        try {    
            KeyGenerator kg = KeyGenerator.getInstance("AES");    
           // kg.init(128);//要生成多少位，只需要修改这里即可128, 192或256    
            //SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以生成的秘钥就一样。  
            kg.init(128, new SecureRandom(iv));  
            SecretKey sk = kg.generateKey();    
            byte[] b = sk.getEncoded();    
            String s = byteToHexString(b);    
            System.out.println(s);    
            System.out.println("十六进制密钥长度为"+s.length());    
            System.out.println("二进制密钥的长度为"+s.length()*4);    
        } catch (NoSuchAlgorithmException e) {    
            e.printStackTrace();    
            System.out.println("没有此算法。");    
        }    
    }  
    /** 
     * byte数组转化为16进制字符串 
     * @param bytes 
     * @return 
     */  
    public static String byteToHexString(byte[] bytes){       
        StringBuffer sb = new StringBuffer();       
        for (int i = 0; i < bytes.length; i++) {       
             String strHex=Integer.toHexString(bytes[i]);   
             if(strHex.length() > 3){       
                    sb.append(strHex.substring(6));       
             } else {    
                  if(strHex.length() < 2){    
                     sb.append("0" + strHex);    
                  } else {    
                     sb.append(strHex);       
                  }       
             }    
        }    
       return  sb.toString();       
    }    
}
