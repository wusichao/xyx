package com.xyx.ls.controller.campain;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.xyx.ls.model.account.Account;
import com.xyx.ls.model.campaign.Campaign;
import com.xyx.ls.model.campaign.Inversionpoint;
import com.xyx.ls.service.campaign.InversionpointService;
import com.xyx.ls.util.ParameterCheckUtil;
import com.xyx.ls.util.Write2Response;
@Controller
public class InversionpointController {
	@Resource
	private InversionpointService inversionpointService;
	//添加转化点
	@RequestMapping("addInversionpoint")
	public void addInversionpoint(Inversionpoint inversionpoint,HttpServletRequest request,HttpServletResponse response){
		if(!"".equals(inversionpoint.getName().trim())&&inversionpoint.getName().trim()!=null&& ParameterCheckUtil.checkLength(inversionpoint.getName(), 40)){
			//获取当前用户
			Account account= (Account)request.getSession().getAttribute("account");
			if(inversionpointService.findByName(inversionpoint.getName(),account.getId())==null){
				inversionpointService.add(inversionpoint,account.getId());
				Write2Response.write2Res(response, "true");
			}else{Write2Response.write2Res(response, "false");}
			
		}else{Write2Response.write2Res(response, "false");}
	}
	//删除转化点
	@RequestMapping("deleteInversionpoint")
	public void deleteInversionpoint(String ids ,HttpServletRequest request,HttpServletResponse response){
		if(!"".equals(ids)&&ids!=null){
			if(!inversionpointService.findCampaignByConvertion(ids)){
				Write2Response.write2Res(response, "false");
			}else{Account account= (Account) request.getSession().getAttribute("account");
			inversionpointService.delete(ids,account.getId());
			Write2Response.write2Res(response, "true");}
			
		}else{Write2Response.write2Res(response, "false");}
	}
	//更新转化点
	@RequestMapping("updateInversionpoint")
	public void updateInversionpoint(Inversionpoint inversionpoint,HttpServletRequest request,HttpServletResponse response){
		if(!"".equals(inversionpoint.getName().trim())&&inversionpoint.getName().trim()!=null&& ParameterCheckUtil.checkLength(inversionpoint.getName(), 40)){
			inversionpointService.update(inversionpoint);
			Write2Response.write2Res(response, "true");
		}else{Write2Response.write2Res(response, "false");}
	}
	//转化点列表,搜索
	@RequestMapping("findAllInversionpoint")
	public void findAllInversionpoint(String name,HttpServletRequest request,HttpServletResponse response){
		//获取当前用户
		Account account= (Account)request.getSession().getAttribute("account");
		List<Inversionpoint> channelList = inversionpointService.findAllByAccountId(account.getId(),name);
		JSON jsonArray = (JSON) JSON.toJSON(channelList);
		Write2Response.write2Res(response, jsonArray.toString());
	}
	//验证转化点是否重复
	@RequestMapping("validateConvertion")
	public void validateConvertion(String name ,HttpServletRequest request,HttpServletResponse response){
		//获取当前用户
				Account account= (Account)request.getSession().getAttribute("account");
				if(inversionpointService.findByName(name,account.getId())!=null){
					Write2Response.write2Res(response, "false");
				}else{Write2Response.write2Res(response, "true");}
				
	}
}
