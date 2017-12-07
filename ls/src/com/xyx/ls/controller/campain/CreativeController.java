package com.xyx.ls.controller.campain;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.xyx.ls.model.campaign.Creative;
import com.xyx.ls.service.campaign.CreativeService;
import com.xyx.ls.util.ParameterCheckUtil;
import com.xyx.ls.util.Write2Response;

public class CreativeController {
	@Resource
	private CreativeService creativeService;
	//添加创意
	@RequestMapping("addCreative")
	public void addCreative(Creative creative,Long accountId,HttpServletRequest request,HttpServletResponse response){
		if(!"".equals(creative.getName().trim())&&creative.getName().trim()!=null&& ParameterCheckUtil.checkLength(creative.getName(), 40)){
			creativeService.add(creative,accountId);
			Write2Response.write2Res(response, "true");
		}else{Write2Response.write2Res(response, "false");}
	}
	//删除创意
	@RequestMapping("deleteCreative")
	public void deleteCreative(String ids,Long accountId,HttpServletRequest request,HttpServletResponse response){
		if(!"".equals(ids)&&ids!=null){
			creativeService.delete(ids,accountId);
			Write2Response.write2Res(response, "true");
		}else{Write2Response.write2Res(response, "false");}
	}
	//更新创意
	@RequestMapping("updateCreative")
	public void updateCreative(Creative creative,HttpServletRequest request,HttpServletResponse response){
		if(!"".equals(creative.getName().trim())&&creative.getName().trim()!=null&& ParameterCheckUtil.checkLength(creative.getName(), 40)){
			creativeService.update(creative);
			Write2Response.write2Res(response, "true");
		}else{Write2Response.write2Res(response, "false");}
	}
	//创意列表,搜索
	@RequestMapping("findAllCreative")
	public void findAllCreative(String name,HttpServletRequest request,HttpServletResponse response){
		List<Creative> channelList = creativeService.findAll(name);
		JSON jsonArray = (JSON) JSON.toJSON(channelList);
		Write2Response.write2Res(response, jsonArray.toString());
	}
}
