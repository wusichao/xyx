package com.xyx.ls.controller.campain;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.xyx.ls.model.account.Account;
import com.xyx.ls.model.campaign.Channel;
import com.xyx.ls.service.campaign.CampaignService;
import com.xyx.ls.service.campaign.ChannelService;
import com.xyx.ls.util.ParameterCheckUtil;
import com.xyx.ls.util.Write2Response;

@Controller
public class ChannelController {
	@Resource
	private ChannelService channelService;
	@Resource
	private CampaignService campaignService;

	// 添加渠道
	@RequestMapping("addChannel")
	public void addChannel(Channel channel, HttpServletRequest request,
			HttpServletResponse response) {
		if (!"".equals(channel.getName().trim())
				&& channel.getName().trim() != null
				&& ParameterCheckUtil.checkLength(channel.getName(), 40)) {
			Account account = (Account) request.getSession().getAttribute(
					"account");
			if (campaignService.findChannelByName(channel.getName(),
					account.getId()) != null) {
				Write2Response.write2Res(response, "false");
			} else {
				channelService.add(channel, account.getId());
				JSON jsonArray = (JSON) JSON.toJSON(channel);
				Write2Response.write2Res(response, jsonArray.toString());
			}
			// 获取当前用户

		} else {
			Write2Response.write2Res(response, "false");
		}
	}

	// 删除渠道
	@RequestMapping("deleteChannel")
	public void deleteChannel(String ids, HttpServletRequest request,
			HttpServletResponse response) {
		if (!"".equals(ids) && ids != null) {
			if (!channelService.findCampaignByChannel(ids)) {
				Write2Response.write2Res(response, "false");
			} else {
				Account account = (Account) request.getSession().getAttribute(
						"account");
				channelService.delete(ids, account.getId());
				Write2Response.write2Res(response, "true");
			}
			// 获取当前用户

		} else {
			Write2Response.write2Res(response, "false");
		}
	}

	// 更新渠道
	@RequestMapping("updateChannel")
	public void updateChannel(Channel channel, HttpServletRequest request,
			HttpServletResponse response) {
		if (!"".equals(channel.getName().trim())
				&& channel.getName().trim() != null
				&& ParameterCheckUtil.checkLength(channel.getName(), 40)) {
			channelService.update(channel);
			Write2Response.write2Res(response, "true");
		} else {
			Write2Response.write2Res(response, "false");
		}
	}

	// 渠道展示
	@RequestMapping("findAllChannel")
	public void findAllChannel(String name, HttpServletRequest request,
			HttpServletResponse response) {
		// 获取当前用户
		Account account = (Account) request.getSession()
				.getAttribute("account");
		List<Channel> channelList = channelService.findAllByAccount(name,
				account.getId());
		JSON jsonArray = (JSON) JSON.toJSON(channelList);
		Write2Response.write2Res(response, jsonArray.toString());
	}

	// 根据活动查找渠道
	@RequestMapping("findChannelByCampaign")
	public void findChannelByCampaign(Long id, HttpServletRequest request,
			HttpServletResponse response) {
		Long iid = (Long) request.getSession().getAttribute("wscid");
		List<Channel> channelList = channelService.findByCanpaignId(iid);
		JSON jsonArray = (JSON) JSON.toJSON(channelList);
		Write2Response.write2Res(response, jsonArray.toString());

	}
}
