package com.zichan.utils.security;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Des {
	public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
		SecureRandom localSecureRandom = new SecureRandom();
		DESKeySpec dESKeySpec = new DESKeySpec(key);
		SecretKeyFactory localObject = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = localObject.generateSecret(dESKeySpec);
		Cipher c = Cipher.getInstance("DES");
		c.init(1, secretKey, localSecureRandom);
		return c.doFinal(src);
	}

	public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
		SecureRandom localSecureRandom = new SecureRandom();
		DESKeySpec dESKeySpec = new DESKeySpec(key);
		SecretKeyFactory localObject = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = localObject.generateSecret(dESKeySpec);
		Cipher c = Cipher.getInstance("DES");

		c.init(2, secretKey, localSecureRandom);
		return c.doFinal(src);
	}
	
	public static byte[] encrypt3Des(byte[] src, byte[] key) throws Exception {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(Base64.decodeBase64(key), "DESede");
			// 加密
			Cipher c1 = Cipher.getInstance("DESede");
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return Base64.encodeBase64(c1.doFinal(src));
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}
	
	public static byte[] decrypt3Des(byte[] src, byte[] key) throws Exception {
		try {
			byte[] base64Str = Base64.decodeBase64(key);
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(base64Str, "DESede");
			// 解密
			Cipher c1 = Cipher.getInstance("DESede");
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(Base64.decodeBase64(src));
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}
}