package com.zichan.utils.security;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	private MessageDigest messageDigest;
	private String charsert = "UTF-8";
	
	public static String md5(String input, Charset charset) {
		MessageDigest localMessageDigest = null;
		try {
			localMessageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		localMessageDigest.update(input.getBytes(charset));
		char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
				'b', 'c', 'd', 'e', 'f' };
		byte[] message = localMessageDigest.digest();
		char[] chars = new char[32];
		int i = 0;
		for (int j = 0; j < 16; j++) {
			int k = message[j];
			chars[(i++)] = str[(k >>> 4 & 0xF)];
			chars[(i++)] = str[(k & 0xF)];
		}
		return new String(chars);
	}

	public String getMD5ofStr(String str) {
		try {
			this.messageDigest = MessageDigest.getInstance("MD5");

			this.messageDigest.reset();

			this.messageDigest.update(str.getBytes(this.charsert));

			byte[] byteArray = this.messageDigest.digest();

			StringBuilder md5StrBuff = new StringBuilder();

			for (int i = 0; i < byteArray.length; i++) {
				if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
					md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
				} else {
					md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
				}
			}

			return md5StrBuff.toString().toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getMD5ofFile(File file) {
		try {
			this.messageDigest = MessageDigest.getInstance("MD5");

			this.messageDigest.reset();

			byte[] buffer = new byte[1024];
			FileInputStream in = new FileInputStream(file);
			int len;
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				this.messageDigest.update(buffer, 0, len);
			}
			in.close();

			byte[] byteArray = this.messageDigest.digest();

			StringBuilder md5StrBuff = new StringBuilder();

			for (int i = 0; i < byteArray.length; i++) {
				if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
					md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
				} else {
					md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
				}
			}

			return md5StrBuff.toString().toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getMD5ofByte(byte[] bytes) {
		try {
			this.messageDigest = MessageDigest.getInstance("MD5");

			this.messageDigest.reset();

			this.messageDigest.update(bytes);

			byte[] byteArray = this.messageDigest.digest();

			StringBuilder md5StrBuff = new StringBuilder();

			for (int i = 0; i < byteArray.length; i++) {
				if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
					md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
				} else {
					md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
				}
			}

			return md5StrBuff.toString().toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}