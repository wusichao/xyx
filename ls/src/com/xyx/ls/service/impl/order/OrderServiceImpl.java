package com.xyx.ls.service.impl.order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xyx.ls.bean.GridBean;
import com.xyx.ls.dao.order.OrderMapper;
import com.xyx.ls.model.account.Account;
import com.xyx.ls.model.order.Functions;
import com.xyx.ls.model.order.Order;
import com.xyx.ls.service.order.OrderService;
import com.xyx.ls.util.SendMailUtil;
@Service
@Transactional
@Scope("singleton")
public class OrderServiceImpl implements OrderService{
	
	@Resource
	private OrderMapper orderMapper;
	@Override
	public void add(Order order,String functions) {
		SimpleDateFormat times = new SimpleDateFormat("yyyyMMddHHmmss");
		Random rand = new Random();
		String nowTime=times.format(new Date());
		int orderRand=rand.nextInt(10);
		order.setCreation(new Date());
		order.setLastModified(new Date());
		order.setOrderNumber(nowTime+orderRand);
		Long accountId=orderMapper.findAccountByCompanyName(order.getCompanyName());
		order.setAccountId(accountId);
		orderMapper.add(order);
		String[] strs=functions.split(",");
		for(String str: strs ){
			orderMapper.addOrdersFunctions(order.getId(),Long.valueOf(str));
		}
	}

	@Override
	public Order findByIds(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GridBean<Order> findAll(String name,String status,int page, int rows, String sidx, String sord) {
		String state;
		if (status.equals("未开始")) {
			state = "DATEDIFF(begin_date,now())>0";
		} else if (status.equals("进行中")) {
			state = "DATEDIFF(begin_date,now())<=0 and DATEDIFF(end_date,now())>=0";
		} else if (status.equals("已结束")) {
			state = "DATEDIFF(end_date,now())<0";
		} else {
			state = "1=1";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		HashMap<String, String> map = new HashMap<String, String>();
		HashMap<String, String> mapre = new HashMap<String, String>();
		mapre.put("name", name);
		mapre.put("state", state);
		int start = (page - 1) * rows;
		int rowpage = rows * page;
		GridBean<Order> gridBean = new GridBean<Order>();
		map.put("state", state);
		map.put("sidx", sidx);
		map.put("sord", sord);
		map.put("name", name);
		map.put("start", start + "");
		map.put("rowpage", rowpage + "");
		int records = orderMapper.getRecords(mapre);
		List<Order> orders=orderMapper.findAll(map);
		for(Order order :orders){
			List<String> functionsList=orderMapper.findFunctionsByOrdersId(order.getId());
			String functions ="";
			for(String str:functionsList){
				functions=functions+str+" ";
			}
			order.setFunctionsName(functions);
			String beginDate = sdf.format(order.getBegin_date());
			String endDate = sdf.format(order.getEnd_date());
			order.setServiceLife(beginDate + "至" + endDate);
		}
		int total;
		if (records % rows == 0) {
			total = records / rows;
		} else {
			total = records / rows + 1;
		}
		gridBean.setPage(page);
		gridBean.setRecords(records);
		gridBean.setTotal(total);
		gridBean.setRows(orders);
		return gridBean;
	}

	@Override
	public Long findAccountByCompanyName(String companyName) {
		return orderMapper.findAccountByCompanyName(companyName);
	}

	@Override
	public Long findProductByname(String productName) {
		return orderMapper.findProductByname(productName);
	}

	@Override
	public List<String> findAccountFunction(Long id) {
		return orderMapper.findAccountFunction(id);
	}

	@Override
	public Order findAccountById(Long id) {
		return orderMapper.findAccountById(id);
	}

	@Override
	public Order findByAccount(Long id) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Order order=orderMapper.findByAccount(id);
		String beginDate = sdf.format(order.getBegin_date());
		String endDate = sdf.format(order.getEnd_date());
		order.setServiceLife(beginDate + "至" + endDate);
		return order;
	}

	@Override
	public List<Functions> findFunctionsList(Long id) {
		return orderMapper.findFunctionsList(id);
	}

	@Override
	public List<Functions> findFunctionsList() {
		return  orderMapper.findFunctionsLists();
	}

	@Override
	public Order findAccountByIdAndDate(Long accountId, Date begin_date) {
		return orderMapper.findAccountByIdAndDate(accountId,begin_date);
	}
	@Scheduled(cron="0 0 15 * * ?")
	public void checkOrder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Order> list =orderMapper.checkOrder();
		for(Order order:list){
			try {
				SendMailUtil.sendMail("wusichao2011@163.com", "wusichao2011", "wsc15171545112", order.getEmail(), "灵石Marketing Plus产品到期提醒", "<h3>尊敬的用户：</h3>您订购的产品将于"+sdf.format(order.getEnd_date())+" 23:59:59到期，请及时联系我们续签，以免影响您的正常使用。<br/><p>感谢您选择灵石Marketing Plus!</p><p>——————</p><p>西游行运营团队</p>");
				
				SendMailUtil.sendMail("wusichao2011@163.com", "wusichao2011", "wsc15171545112", "1033429246@qq.com", "灵石Marketing Plus产品到期提醒", "<h3>尊敬的管理员：</h3>"+order.getCompanyName()+"订购的产品将于"+sdf.format(order.getEnd_date())+" 23:59:59到期。<br/><p>——————</p><p>西游行运营团队</p>");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void addVip(Long id) {
		Order order=new Order();
		order.setAccountId(id);
		SimpleDateFormat times = new SimpleDateFormat("yyyyMMddHHmmss");
		Random rand = new Random();
		String nowTime=times.format(new Date());
		int orderRand=rand.nextInt(10);
		order.setOrderNumber(nowTime+orderRand);
		orderMapper.addVip(order);
		for(int i=1; i<4 ;i++){
			orderMapper.addVipRel(order.getId(),i);
		}
		
	}

}
