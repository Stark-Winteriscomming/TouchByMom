package com.capston.server.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface SampleService {

	List<Map<String, Object>> selectBoardList(Map<String, Object> commandMap) throws Exception;
	
	void insertBoard(HttpServletRequest request) throws Exception;
	
	void insertClientPackages(Map<String , Object> packageList);
	
	
	void insertImgFile(MultipartHttpServletRequest request, ModelMap model)throws IllegalStateException, IOException;
			
}
