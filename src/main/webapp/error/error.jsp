<%@page contentType="text/html; charset=utf-8"%>
<%@page language="java" import="com.sanwenqian.enumeration.Code"%>
<%
	out.write("{\"message\":{\"code\":\"" + Code.request_failure.getCode() + "\",\"message\":\""
			+ Code.request_failure.getMsg() + "\"}}");
%>