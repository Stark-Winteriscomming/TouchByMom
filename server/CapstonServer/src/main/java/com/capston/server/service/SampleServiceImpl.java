package com.capston.server.service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.capston.server.common.util.CommonUtils;
import com.capston.server.common.util.FileUtils;
import com.capston.server.dao.SampleDao;
import com.capston.server.model.AppImgInfo;
 
@Service("sampleService")
public class SampleServiceImpl implements SampleService{
    Logger log = Logger.getLogger(this.getClass());
     
    @Resource(name="sampleDAO")
    private SampleDao sampleDao;
     
    @Resource(name="fileUtils")
    private FileUtils fileUtils;
    
    @Override
    public List<Map<String, Object>> selectBoardList(Map<String, Object> map) throws Exception {
        return sampleDao.selectBoardList(map);
    }

	@Override
	public void insertBoard(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
        //sampleDao.insertBoard(map);
        
        List<Map<String,Object>> list = fileUtils.parseInsertFileInfo(request);
        for(int i=0, size=list.size(); i<size; i++){
            sampleDao.insertFile(list.get(i));
        }
	}

	@Override
	public void insertClientPackages(Map<String , Object> packageList) {
		// TODO Auto-generated method stub
		sampleDao.insertClientPackage(packageList);
	}
	@Override
	public void insertImgFile(MultipartHttpServletRequest request, ModelMap model)
			throws IllegalStateException, IOException {
		
		Map<String, MultipartFile> files = request.getFileMap();
		System.out.println(files.size());
		//
		String keyset = files.keySet().toString();
		keyset = keyset.replace("[","");
		keyset = keyset.replace("]","");
		System.out.println("keysetString: "+keyset);
		System.out.println(files.keySet());
		CommonsMultipartFile cmf = (CommonsMultipartFile) files.get(keyset);
		// 경로
		//System.out.println("cmf.getOriginalfilename "+cmf.getOriginalFilename());
		String pdfPath = request.getSession().getServletContext().getRealPath("/upload");
		
		System.out.println("pdf : " + pdfPath);
		
//		String savePath = "C://Users/changho/Desktop/MyAndroidProject/server/CapstonServer/src/main/resources/common/img/"+cmf.getOriginalFilename();
		String savePath = pdfPath+"\\"+cmf.getOriginalFilename();
		System.out.println("저장 경로 : " +savePath);
//		System.out.println("name : " + name);

//		File file = new File(savePath);
		
		File file = new File(savePath);
		// 파일 업로드 처리 완료.
		cmf.transferTo(file);

		try {
			// insert method
			model.addAttribute("result", "업로드 성공");
		} catch (Exception e) {
			model.addAttribute("result", "업로드 실패");
		}
		
		AppImgInfo appImgInfo = new AppImgInfo();
		String stored_name = CommonUtils.getRandomString();
		String packageName = keyset; 
		appImgInfo.setPackageName(packageName);
		appImgInfo.setStored_name(stored_name);
		

		sampleDao.insertAppIconInfo(appImgInfo);
	}
}
