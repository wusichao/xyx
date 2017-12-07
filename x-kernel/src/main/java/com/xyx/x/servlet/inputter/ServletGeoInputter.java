package com.xyx.x.servlet.inputter;

import com.xyx.x.model.Geo;
import com.xyx.x.model.kernel.action.Action;
import com.xyx.x.model.servlet.ServletReqResp;
import com.xyx.x.servlet.XServletInputter;
import com.xyx.x.utils.ServletUtils;
import com.xyx.x.utils.ipanalyze.IpZone;
import com.xyx.x.utils.ipanalyze.Ips;

public class ServletGeoInputter implements XServletInputter<Action> {
	
	private Ips ips = new Ips();

	@Override
	public void input(ServletReqResp source, Action action) {

		if (action.getGeo() == null) {
			action.setGeo(new Geo());
		}
		
		String ip = ServletUtils.getRemoteAddr(source.getRequest());
		
		if(ip != null){
			if(ip.indexOf(':') > 0){
				action.getGeo().setIpv6(ip);
			}else{
				action.getGeo().setIp(ip);
			}
			IpZone ipz = ips.citySeek(ip);
			if(ipz != null){
				action.getGeo().setId((long)ipz.getCityId());
			}
		}
	}

}
