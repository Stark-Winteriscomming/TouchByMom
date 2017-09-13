package com.capston.server;




import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.capston.server.service.SampleService;

@Controller
public class AppIconReceiverController {
    @Resource(name="sampleService")
    private SampleService sampleService;
	
//	@RequestMapping(value="/capstonserver/sendbitmap")
//	public @ResponseBody void receive(HttpServletRequest request,@RequestBody String requestbodyString){
//		System.out.println("receive img called");
//		Logger log = LoggerFactory.getLogger(AppIconReceiverController.class);
//		//
//		System.out.println("request body string "+requestbodyString);
//		//
//		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
//	    Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
//	    MultipartFile multipartFile = null;
//	    while(iterator.hasNext()){
//	        multipartFile = multipartHttpServletRequest.getFile(iterator.next());
//	        if(multipartFile.isEmpty() == false){
//	            System.out.println("------------- file start -------------");
//	            System.out.println("name : "+multipartFile.getName());
//	            System.out.println("filename : "+multipartFile.getOriginalFilename());
//	            System.out.println("size : "+multipartFile.getSize());
//	            System.out.println("-------------- file end --------------\n");
//	        }
//	    }
//	}
	
	@RequestMapping(value = "/capstonserver/sendbitmap", method = RequestMethod.POST)
	public void insert(HttpServletRequest request2,MultipartHttpServletRequest request, ModelMap model)
			throws IllegalStateException, IOException {
		sampleService.insertImgFile(request, model);
	}
}
