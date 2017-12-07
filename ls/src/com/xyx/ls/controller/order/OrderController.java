package com.xyx.ls.controller.order;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.xyx.ls.bean.GridBean;
import com.xyx.ls.model.account.Account;
import com.xyx.ls.model.order.Functions;
import com.xyx.ls.model.order.Order;
import com.xyx.ls.service.order.OrderService;
import com.xyx.ls.util.Write2Response;

@Controller
public class OrderController {
	@Resource
	private OrderService orderService;
	//添加订单
	@RequestMapping("addOrder")
	public void addOrder(Order order, String functions,HttpServletRequest request,HttpServletResponse response){
		Long accountId= orderService.findAccountByCompanyName(order.getCompanyName());
		if(orderService.findAccountByIdAndDate(accountId,order.getBegin_date())==null){
			orderService.add(order,functions);
			}else{
				Write2Response.write2Res(response, "false");
			}
		
	}
	//订单展示
	@RequestMapping("findAllOrder")
	public void findAllOrder(String name,String status,int page, int rows,
			String sidx, String sord,HttpServletRequest request,HttpServletResponse response){
		GridBean<Order> gridBean =orderService.findAll(name,status,page,rows,sidx,sord);
		JSON jsonArray = (JSON) JSON.toJSON(gridBean);
		Write2Response.write2Res(response, jsonArray.toString());
	}
	//客户类型
	@RequestMapping("findAccountFunction")
	public void findAccountFunction(Order order,HttpServletRequest request,HttpServletResponse response){
		Account account=(Account) request.getSession().getAttribute("account");
		List<Functions> functionList = orderService.findFunctionsList(account.getId());
		String functions="";
		for(Functions function:functionList){
			functions=functions+function.getName()+" ";
		}
		Write2Response.write2Res(response,functions);
	}
	//获取功能列表
	@RequestMapping("findFunctionsListByAccount")
	public void findFunctionsListByAccount(HttpServletRequest request,HttpServletResponse response){
		Account account=(Account) request.getSession().getAttribute("account");
		List<Functions> functions= orderService.findFunctionsList(account.getId());
		JSON jsonArray = (JSON) JSON.toJSON(functions);
		Write2Response.write2Res(response, jsonArray.toString());
	}
	
	//获取功能列表
		@RequestMapping("findFunctionsList")
		public void findFunctionsList(HttpServletRequest request,HttpServletResponse response){
			List<Functions> functions= orderService.findFunctionsList();
			JSON jsonArray = (JSON) JSON.toJSON(functions);
			Write2Response.write2Res(response, jsonArray.toString());
		}
}
