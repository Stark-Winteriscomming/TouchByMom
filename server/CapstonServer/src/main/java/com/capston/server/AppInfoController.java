package com.capston.server;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.capston.server.model.ClientApp;
import com.capston.server.service.SampleService;

@Controller
public class AppInfoController {
	@Resource(name="sampleService")
	private SampleService sampleService;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    

	/**
	 * Simply selects the home view to render by returning its name.
	 */

	@RequestMapping(value = "/capstonserver/android/sendinstalledapplist", method = RequestMethod.POST)
	public @ResponseBody void home(HttpServletRequest request , Locale locale, Model model) {
		//logger.info("Welcome home2! The client locale is {}.", locale);
		System.out.println("android calling");
		try{
			String data = null;
			
			InputStream is = request.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			byte buf[] = new byte[1024];
			int letti;
			
			while((letti = is.read(buf)) > 0)
				baos.write(buf,0,letti);
			
			data = new String(baos.toByteArray(),"utf-8");
			
			
			//JSONParser parser = new JSONParser();

			//JSONObject obj = (JSONObject)parser.parse(data);
			JSONObject obj = new JSONObject(data.toString());
			System.out.println("received data"+data);
			System.out.println("data to string"+data.toString());
			//System.out.println("requestGetcontextPath: "+ request.getContextPath());
			//JSONObject jsonObject = new JSONObject(obj.toJSONString())
			
			JSONArray jsonArray = obj.getJSONArray("appList");
			// package name 저장
			List<ClientApp> arrayList = new ArrayList<ClientApp>();
			
			for(int i=0; i<jsonArray.length(); i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				System.out.println("appName "+jsonObject.get("appName"));
				System.out.println("packageName "+jsonObject.get("packageName"));
				String appname = (String)jsonObject.get("appName");
				String packagename = (String)jsonObject.get("packageName");
				// ClientApp 
				ClientApp app =  new ClientApp();
				app.setClient_id("changho");
				app.setAppName(appname);
				app.setPackageName(packagename);
				
				arrayList.add(app);
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("list",arrayList);
			sampleService.insertClientPackages(map);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}

	    
		
	}
}
