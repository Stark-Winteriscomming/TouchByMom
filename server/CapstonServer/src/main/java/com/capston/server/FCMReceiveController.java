package com.capston.server;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.capston.server.model.ClientApp;

@Controller
public class FCMReceiveController {
	public final static String AUTH_KEY_FCM = "AIzaSyD459zFyXaBXWvjiJqiHDd9GsQypff_uGw";
//	public final static String AUTH_KEY_FCM = "AIzaSyA2neX-nhxHXWDkDGbn4VZsoAXb-qjyHlU";
	public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
	private static ArrayList<String> arrayList;
	public static String time;
	@RequestMapping(value = "/capstonserver/fcm/send", method = RequestMethod.POST)
	public void Fcm(HttpServletRequest request){
		System.out.println("fcm called");
		
		//String url_string  = "APA91bHVubXYipYlqGK-OOteHP975W5SGXw4QmVNjKBBBAgDR3aYZT-67EpEdQkpROt72gQlGp783EjU62Qd6KMdGUjbkpZJeXmk-HtxWk1bHbBqmkvj--mVzTu78TkP1dwM_EzUKEtyY73awCypCf0I3fCALILmkA";
//		String url_string  = "dfTicDC8Jhw:APA91bGNtCWpQzTqX6MQlpavYCMPG5Z2_rBeSs0XnsSua08V-A3Z59Z8LuixhhSmZWoiuSgV5AdF_8KG_zdEfhQMPpPIQARffcyvl5Jdu3EK30I3xMWh9dMA0vMisyZBOJZJKNUhfSSL";
		String url_string = "fy_QD10vfOo:APA91bGQY-G8U_-tnUMq3O83OXqduVJd2Oj4HaFoiDTwqieYO62ZJ97e0dXPmYHxSNpcv2AyhjKoXQadttbRLMGXvUVV9z0mxujDPsxrAyc6nSyudEvuSwdulrqtK5lRNo-9L6CwS25q";
		String data = null;
		try {
			System.out.println("syso");
			InputStream is = request.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			byte buf[] = new byte[1024];
			int letti;
			
			while((letti = is.read(buf)) > 0)
				baos.write(buf,0,letti);
			
			data = new String(baos.toByteArray(),"utf-8");
			
			JSONObject obj = new JSONObject(data.toString());
			System.out.println("received data"+data);
			System.out.println("data to string"+data.toString());
			//System.out.println("requestGetcontextPath: "+ request.getContextPath());
			//JSONObject jsonObject = new JSONObject(obj.toJSONString())
			//
			time = (String)obj.get("time");
			//
			JSONArray jsonArray = obj.getJSONArray("targets");
			arrayList = new ArrayList<String>();
			for(int i=0; i<jsonArray.length(); i++){
				System.out.println("jsonarray i: "+jsonArray.getString(i));
				arrayList.add(jsonArray.getString(i));
			}
			// package name 저장
			//List<String> arrayList = new ArrayList<String>();
			
			//
			pushFCMNotification(url_string);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static void pushFCMNotification(String userDeviceIdKey) throws Exception{
		
		System.out.println("push method called");
		String authKey = AUTH_KEY_FCM;   // You FCM AUTH key
		String FMCurl = API_URL_FCM;     

		URL url = new URL(FMCurl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);

		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization","key="+authKey);
		conn.setRequestProperty("Content-Type","application/json");

		JSONObject json = new JSONObject();
		json.put("to",userDeviceIdKey.trim());
		JSONObject info = new JSONObject();
		info.put("title", "Notificatoin Title");   // Notification title
		info.put("body", "Hello Test notification"); // Notification body
		

		json.put("data", info);
		
		////////////////
		 
		JSONArray jsonArray = new JSONArray();
		//jsonArray.add("com.sec.android.app.music");
		//////////////
		for(String app : arrayList){
			jsonArray.put(app);
		}
		
		//////////////
		//jsonArray.put("com.sec.android.app.music");
		//jsonArray.add("com.android.mms");
		//jsonArray.put("com.android.mms");
		info.put("applist",jsonArray);

		info.put("time",time);
		System.out.println("json String:" + json.toString());
		//////////////
			
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(json.toString());
		wr.flush();
		//conn.getInputStream();
		
		
		// adding test code 
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    String inputLine;
	    StringBuffer response = new StringBuffer();

	    while ((inputLine = in.readLine()) != null) {
	        response.append(inputLine);
	    }
	    in.close();

	    // print result
	    System.out.println(response.toString());
	}
}
