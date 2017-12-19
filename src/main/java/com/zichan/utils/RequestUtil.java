package com.zichan.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

	public static String getRemoteIPAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	
	
	/**
	 * HttpRequest 解析工具
	 */
	public static Map<String, Object> getHeaders(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Enumeration<?> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getParameters(HttpServletRequest request) {
		 // 参数Map  
	    Map<String, String[]> properties = request.getParameterMap();  
	    // 返回值Map  
	    Map<String, Object> returnMap = new HashMap<String, Object>();  
		Iterator<Entry<String, String[]>>  entries = properties.entrySet().iterator(); 
		Map.Entry<String, String[]> entry;  
	    while (entries.hasNext()) {
	    	String value = "";    
	        entry = entries.next();  
	        String name = (String) entry.getKey();  
	        Object valueObj = entry.getValue();  
	        if(null == valueObj){  
	            value = "";  
	        }else if(valueObj instanceof String[]){  
	            String[] values = (String[])valueObj;
	            for(int i=0;i<values.length;i++){  
	                value += values[i] + ",";  
	            }  
	            value = value.substring(0, value.length()-1);  
	        }else{  
	            value = valueObj.toString();  
	        }  
	        returnMap.put(name, value);  
	    }  
	    return returnMap;  
	}

	
	/*// 从请求中获取int参数
	public static int getInt(HttpServletRequest request, String str, int defaultInt) {
		String tempStr = request.getParameter(str);
		int tempInt = 0;
		if (tempStr == null || "".equals(tempStr)) {
			return defaultInt;
		}
		try {
			tempInt = Integer.parseInt(tempStr);
		} catch (Exception e) {
			tempInt = defaultInt;
		}
		return tempInt;
	}*/
	
	/*protected Map<String, Object> getAttributes(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Enumeration<?> attributeNames = request.getAttributeNames();
		while (attributeNames.hasMoreElements()) {
			String key = (String) attributeNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		return map;
	}*/
	
}