package com.capston.server;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
public class CalculateController {
	private static final Logger logger = LoggerFactory.getLogger(CalculateController.class);
	
	@RequestMapping(value = "/calculate", method = RequestMethod.POST,produces = "text/plain")
	@ResponseBody
	public String print(@RequestBody String data,HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
	    response.setCharacterEncoding("UTF-8");
		String url = data.toString();
		try {
			String d_url = URLDecoder.decode(url,"UTF-8");
			System.out.println(d_url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = "ERROR";
		return result;
		
	}
}
