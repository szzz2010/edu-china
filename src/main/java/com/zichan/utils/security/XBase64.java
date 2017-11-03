package com.zichan.utils.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;

/**
 * @author liyanlong
 * @since  v1.0
 * @date   2016年12月20日上午11:13:20
 * <p>title: XBase64</p>
 * <p>desc:  Base64工具类</p>
 */
public final class XBase64 {
	public static byte[] encodeBase64(byte[] binaryData) {
		return Base64Utils.encode(binaryData);
	}

	public static byte[] decodeBase64(byte[] binaryData) {
		return Base64Utils.decode(binaryData);
	}

	/**
	 * 处理特殊字符：把+换成-， 把/换成_
	 *
	 * @param base64Str
	 * @return
	 */
	public static String enReplace(String base64Str) {
		return StringUtils.isNotBlank(base64Str) ? StringUtils.replace(base64Str, "+", "-").replace("/", "_") : base64Str;
	}


	/**
	 * 还原：把-还成+， 把_换成/
	 *
	 * @param base64Str
	 * @return
	 */
	public static String deReplace(String base64Str) {
		return StringUtils.isNotBlank(base64Str) ? StringUtils.replace(base64Str, "-", "+").replace("_", "/") : base64Str;
	}
}
