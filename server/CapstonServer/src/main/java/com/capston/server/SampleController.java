package com.capston.server;

import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.capston.server.service.SampleService;
 
@Controller
public class SampleController {
    Logger log = Logger.getLogger(this.getClass());
    
    @Resource(name="sampleService")
    private SampleService sampleService;
     
    @RequestMapping(value="/open", method = RequestMethod.POST)
    public void openSampleBoardList(Map<String,Object> commandMap,HttpServletResponse httpServletResponse) throws Exception{
        ModelAndView mv = new ModelAndView("/BoardList");
         
        List<Map<String,Object>> list = sampleService.selectBoardList(commandMap);
        System.out.println(list.get(0).get("app_name")+" "+list.get(0).get("app_packageName"));
        
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for(Map<String,Object> map : list){
        	String name = (String)map.get("app_name");
        	String pName = (String)map.get("app_packageName");
        	JSONObject app = new JSONObject();
        	app.put("appName",name);
        	app.put("packageName",pName);
        	jsonArray.put(app);     	
        }
        json.put("appList",jsonArray);
        
        System.out.println(json.toString());
        
        
        OutputStreamWriter wr = new OutputStreamWriter(httpServletResponse.getOutputStream());
        wr.write(json.toString());
        wr.flush();
        
        //mv.addObject("list", list);
        //return mv;
    }
    
    @RequestMapping(value="/sample/insertBoard")
    public void insertBoard(HttpServletRequest request) throws Exception{
       System.out.println("sample insertBoard called");  
        sampleService.insertBoard(request);
    }
}
