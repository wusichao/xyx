package com.xyx.ls.util;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import com.xyx.ls.util.Write2Response;
public class FileUploadUtil {
	private static final SimpleDateFormat sf_sss = new SimpleDateFormat("yyMMddHHmmsss");
	public static final int UPLOAD_IMG_FILE_MAXSIZE=5*1024*1024;//最大2Mb
	private static final String UPLOAD_IMG_PATH=File.separator+"file";
	
	//private  CommonsMultipartResolver multipartResolver ;
	/**
	 * 文件上传处理
	 * 
	 * 文件上传的路径为 : /file/scenic_img/
	 * 文件名称的命名规则: yyMMddHHmmsss+4位随机数
	 * 返回结果处理
	 * ret_code: > 0 ok <0 fali
	 * ret_msg: 结果
	 * 1:成功+fileName
	 * -1: 文件过大
	 * -2: 非法文件
	 * -3: 获取文件失败
	 * -4:未知文件
	 * -5:上传失败
	 * @param request
	 * @param response
	 * 
	 */
	public static String uploadImg(HttpServletRequest req,HttpServletResponse response){
		
		MultipartHttpServletRequest request = (MultipartHttpServletRequest)req;
		String info =null ;
		Iterator<String> it = request.getFileNames() ;
		String f = it.next(); // f = "imgfile" 相当于 input type=file 中的name属性的值
		MultipartFile mf = request.getFile(f) ;
		if(!isImage(mf)) {
//			info="{'ret_code':-2,'ret_msg':'非法文件,请使用图片文件上传!'}";
//			Write2Response.write2Res(response, info) ;
			return null;
		}
		
		if(UPLOAD_IMG_FILE_MAXSIZE<=mf.getSize()){
//			info="{'ret_code':-1,'ret_msg':'文件大于2MB!'}";
//			Write2Response.write2Res(response, info) ;
			return null;
		}
		
		String input_fileName = mf.getOriginalFilename(); //上传上来的文件名称 /**/**/.**
		
		if(input_fileName == null) {
//			info="{'ret_code':-3,'ret_msg':'获取文件失败!'}";
//			Write2Response.write2Res(response, info) ;
			return null;
		}
		
		int index = input_fileName.lastIndexOf(".");
		if(index<0){
//			info="{'ret_code':-4,'ret_msg':'未知文件!'}";
//			Write2Response.write2Res(response, info) ;
			return null;
		}
		String cpt = request.getSession().getServletContext().getRealPath("/");
		String save_path = "D:/work/project";//本地
//		String save_path = "/usr/local/wusc/project";//线上
		
		String fileName = sf_sss.format(new java.util.Date())+String.format("%1$04d", new Random().nextInt(1000));
		File path = new File(save_path);
		if(!path.exists()) path.mkdirs();
		String extension = input_fileName.substring(index+1,input_fileName.length());//文件扩展名
		fileName = fileName+"."+extension;
		String path_fileName = save_path+File.separator+fileName;
		
		if(saveFile(mf,path_fileName)){
			info="{'ret_code':1,'ret_msg':'"+fileName+"'}";
			return fileName ;
		}else{
			info="{'ret_code':-5,'ret_msg':'上传失败!'}";
			Write2Response.write2Res(response, info) ;
			return null ;
		}
		
	}
	
	
	/**
	 * 判定是否文件
	 * @param file
	 * @return
	 */
	public static boolean isImage(MultipartFile file) { 
		Image img = null; 
		try {
			img = ImageIO.read(file.getInputStream());
			if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) { 
                return false; 
            } 
			return true;
		} catch (IOException e) {
			return false;
		} 
	} 
	
	/**
	 * 保存文件
	 * @param file
	 * @param filename
	 * @return
	 */
	private static boolean saveFile(MultipartFile file,String filename) {
		  if (file == null || file.isEmpty())
		   return false;
		  // 写入文件
		  File source = new File(filename);
		  if(source.exists()) {
			  System.out.println("出现重复文件,预估并发线程过多!");
			  return false;
		  }else{
			  try {
				  file.transferTo(source);
				  System.out.println(new Date());
				  return true;
			  } catch (Exception e) {
				  e.printStackTrace();
				  return false;
			  }
		  }
	}
	
	
}
