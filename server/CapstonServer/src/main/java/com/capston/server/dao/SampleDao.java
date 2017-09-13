package com.capston.server.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.capston.server.model.AppImgInfo;
 

 
@Repository("sampleDAO")
public class SampleDao extends AbstractDao{

	 @SuppressWarnings("unchecked")
	 public List<Map<String, Object>> selectBoardList(Map<String, Object> map) throws Exception{
		 return (List<Map<String, Object>>)selectList("sample.selectBoardList", map);
	 }

	public void insertBoard(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
	}
	public void insertFile(Map<String, Object> map) throws Exception{
	    //insert("sample.insertFile", map);
	}
	
	
	public void insertClientPackage(Map<String , Object> packageList){
		insertClientApps("sample.insertClientApps",packageList);
	}
	
	// 앱 아이콘 이미지 정보 삽입 
	public void insertAppIconInfo(AppImgInfo appInfo){
		insertAppIconInfo("sample.insertAppIconInfo",appInfo);
	}
}