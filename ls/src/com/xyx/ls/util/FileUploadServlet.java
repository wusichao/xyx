package com.xyx.ls.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.xyx.ls.model.campaign.Creative;
import com.xyx.ls.service.campaign.CampaignService;

@Controller
public class FileUploadServlet{
	@Resource
	private CampaignService campaignService;
	private static final int UPLOAD_SUCCSSS=0;    // "上传文件成功！", 
	private static final int UPLOAD_FAILURE=1;    // "上传文件失败！"), 
	private static final int UPLOAD_TYPE_ERROR=2; // "上传文件类型错误！"), 
	private static final int UPLOAD_OVERSIZE=3;   // "上传文件过大！"),
	private static final int UPLOAD_ZEROSIZE=4;   // "上传文件为空！"),
	private static final int UPLOAD_NOTFOUND=5;   // "上传文件路径错误！")
	private static final int UPLOAD_FIELNAMEOVERSIZE=6;   //文件名过长
	private static final int UPLOAD_FIELNAMESAME=7;	//创意名重复
	
	@RequestMapping("creativeDate")
	public void creativeDate(HttpServletRequest request,HttpServletResponse response){
		List<Creative> creativeList=null;
		try {
			creativeList=(List<Creative>) request.getSession().getAttribute("uploadCreative");
			for(Creative creative :creativeList){
				creative.setCreation(new Date());
				creative.setLast_modified(new Date());
				campaignService.addCreative(creative);
				creative.setId(creative.getId());
			}
			JSON jsonArray = (JSON) JSON.toJSON(creativeList);
			Write2Response.write2Res(response, jsonArray.toString());
		} catch (Exception e) {
		} finally{
			request.getSession().removeAttribute("uploadCreative");
		}
	}
	
	@RequestMapping("addCreativeNoPath")
	public void addCreativeNoPath(String name,HttpServletRequest request,HttpServletResponse response){
		Creative creative = new Creative();
		creative.setName(name);
		creative.setCreation(new Date());
		creative.setLast_modified(new Date());
		if(!isSameNane(name,request)){
			campaignService.addCreative(creative);
			creative.setId(creative.getId());
			JSON jsonArray = (JSON) JSON.toJSON(creative);
			Write2Response.write2Res(response, jsonArray.toString());
			}else{
				Write2Response.write2Res(response, "false");
			}
		
	}
	@RequestMapping("uploadCreative")
	public void uploadCreative(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		PrintWriter out=response.getWriter();
		 //上传操作  
		  FileItemFactory factory = new DiskFileItemFactory();  
		  ServletFileUpload upload = new ServletFileUpload(factory);  
		  upload.setHeaderEncoding("UTF-8");  
		  try{  
		      List<MultipartFile> items = ((MultipartRequest) request).getFiles("filedata");
			for (MultipartFile item : items) {
				String path = "D:/work/project";//本地
//				String path = "/usr/local/wusc/project";//线上
				File creativepath = new File(path);
				if (!creativepath.exists())
					creativepath.mkdirs();
				String filerealName = getFileNameNoEx(item
						.getOriginalFilename());
				if (filerealName.length() > 30) {
					out.print("{status:" + this.UPLOAD_FIELNAMEOVERSIZE
							+ ",message:''}");
				} else{
				if(isSameNane(filerealName,request)){
					out.print("{status:" + this.UPLOAD_FIELNAMESAME
							+ ",message:''}");
				}
				else {
					String fileName = new SimpleDateFormat("yyMMddHHmmsss")
							.format(new java.util.Date())
							+ String.format("%1$04d",
									new Random().nextInt(1000));
					int index = item.getOriginalFilename().lastIndexOf(".");
					String extension = item.getOriginalFilename().substring(
							index + 1, item.getOriginalFilename().length());// 文件扩展名
					fileName = fileName + "." + extension;
					File savedFile = new File(creativepath, fileName);
					item.transferTo(savedFile);
					Creative creative = new Creative();
					creative.setPath("/image/" + fileName);
					creative.setName(filerealName);
					List<Creative> creativeList = (List<Creative>) request.getSession().getAttribute("uploadCreative");
					if(creativeList==null){
						 creativeList = new ArrayList<Creative>();
					}
					creativeList.add(creative);
					request.getSession().setAttribute("uploadCreative", creativeList);
					out.print("{status:" + this.UPLOAD_SUCCSSS
							+ ",message:'/ls" + path + "/" + fileName + "'}");
				}
			}
				}
//			request.getSession().setAttribute("loadCreative", creativeList);
//		              }  
//		          }  
//		      }  
		  }catch(Exception e){  
		      e.printStackTrace();  
		  }
	}

	private boolean isSameNane(String filerealName,HttpServletRequest request) {
		Map<String, String> map=(Map<String, String>) request.getSession().getAttribute("creativeNamesss");
		if(map==null){
			map = new HashMap<String, String>();
			List<Creative> oldCreativeList = (List<Creative>) request.getSession().getAttribute("creativeList");
			if(oldCreativeList!=null&&oldCreativeList.size()>0){
				for(Creative creative:oldCreativeList){
					map.put(creative.getName(), creative.getName());
				}
			}
		}
		if (map.get(filerealName) == null) {
			map.put(filerealName, filerealName);
			request.getSession().setAttribute("creativeNamesss",map);
			return false;
		} else {
			return true;
		}
	}
	/** 
     * new文件名= 时间 + 全球唯一编号 
     * @param fileName old文件名 
     * @return new文件名 
     */  
    private String generateFileName(String fileName) {  
        String uuid=UUID.randomUUID().toString();  
        int position = fileName.lastIndexOf(".");     
        String extension = fileName.substring(position);     
        return uuid + extension;     
    }  
    public String getFileNameNoEx(String filename) { 
        if ((filename != null) && (filename.length() > 0)) { 
            int dot = filename.lastIndexOf('.'); 
            if ((dot >-1) && (dot < (filename.length()))) { 
                return filename.substring(0, dot); 
            } 
        } 
        return filename; 
    }
}
