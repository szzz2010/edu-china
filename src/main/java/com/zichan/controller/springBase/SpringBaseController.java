package com.zichan.controller.springBase;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zichan.utils.RequestUtil;

@Controller
@RequestMapping(value = "/commonController")
public class SpringBaseController {

	@RequestMapping(value = "/aaaaa", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView registerSuccess(@RequestBody(required = false) Map<String, Object> params, HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
	RequestUtil.getHeaders(request);
	RequestUtil.getParameters(request);
		return new ModelAndView("ssssss", map);
	}
	
	@RequestMapping(value = "/wwww", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody Map<String,Object> doSomething(@RequestBody(required = false) Map<String, Object> params, HttpServletRequest request) {
		return params;
	}
	
}
