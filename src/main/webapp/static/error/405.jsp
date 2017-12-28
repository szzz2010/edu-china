<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.sanwenqian.util.ServletUtils"%>
<% 
	String basePath = ServletUtils.serverUrl(request);
	//跳转到首页去
	response.sendRedirect(basePath + "admin/home");
%>