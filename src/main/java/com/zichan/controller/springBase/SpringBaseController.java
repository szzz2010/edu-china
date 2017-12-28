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

@Controller
@RequestMapping(value = "/")
public class SpringBaseController {

	@RequestMapping(value = "/login", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView registerSuccess(@RequestBody(required = false) Map<String, Object> params, HttpServletRequest request) {
		return new ModelAndView("login", params);
	}
	
	@RequestMapping(value = "/index", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody Map<String,Object> doSomething(@RequestBody(required = false) Map<String, Object> params, HttpServletRequest request) {
		params = new HashMap<String,Object>();
		params.put("hello", "thank you for playing !");
		return params;
	}
	
}
