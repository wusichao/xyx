package com.xyx.ls.controller.siteCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xyx.ls.model.account.Account;
import com.xyx.ls.util.Write2Response;

@Controller
public class SiteCode {
	//到达转化代码生成
	@RequestMapping("productCode")
	public void productCode(Long id,HttpServletRequest request,HttpServletResponse response){
		Account account = (Account) request.getSession().getAttribute("account");
		String code;
		if(!"".equals(id)&&id!=null){
			if(account.getDomain_type()==2){
				code="<img src='//t.xyxtech.com/v?a="+account.getId()+"&v="+id+"' style='display:none;'/>";
			}else{
				code="<img src='//"+account.getDomain()+"/v?a="+account.getId()+"&v="+id+"' style='display:none;'/>";
			}
			
		}else{
			if(account.getDomain_type()==2){
				code="<img src='//t.xyxtech.com/r?a="+account.getId()+"' style='display:none;'/>";
			}else{
				code="<img src='//"+account.getDomain()+"/r?a="+account.getId()+"' style='display:none;'/>";
			}
			
		}
		Write2Response.write2Res(response, code);
	}
	@RequestMapping("InstallationCode")
	public void InstallationCode(String trackingCode,String toweb,HttpServletRequest request,HttpServletResponse response){
		try {
			URL url = new URL(toweb);
			URLConnection conn = url.openConnection();//获得UrlConnection 连接对象
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			conn.setConnectTimeout(10000);
			InputStream is = conn.getInputStream();//获得输入流
			BufferedReader br = new BufferedReader(new InputStreamReader(is));//buffered表示缓冲类
			String str;
			String code = "";
			while(null!=(str = br.readLine())){
				code=code+str;
			}
			if(code.indexOf(trackingCode)!=-1){
				Write2Response.write2Res(response, "true");
			}else{
				Write2Response.write2Res(response, "false");
			}
			br.close();
		} catch (IOException e) {
			Write2Response.write2Res(response, "badweb");
//			e.printStackTrace();
		}
	}
	@RequestMapping("getAccount")
	public void getAccount(HttpServletRequest request,HttpServletResponse response){
		Account account = (Account) request.getSession().getAttribute("account");
		Write2Response.write2Res(response, account.getId().toString());
	}
	@RequestMapping("getDomain")
	public void getDomain(HttpServletRequest request,HttpServletResponse response){
		Account account = (Account) request.getSession().getAttribute("account");
		String domain="t.xyxtech.com";
		if(account.getDomain_type()!=2){
			domain=account.getDomain();
		}
		Write2Response.write2Res(response, domain);
	}
}
