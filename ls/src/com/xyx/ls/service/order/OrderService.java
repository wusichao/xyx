package com.xyx.ls.service.order;

import java.util.Date;
import java.util.List;

import com.xyx.ls.bean.GridBean;
import com.xyx.ls.model.account.Account;
import com.xyx.ls.model.order.Functions;
import com.xyx.ls.model.order.Order;

public interface OrderService {
	void add( Order order, String functions);
	Order findByIds(Long id);
	GridBean<Order> findAll(String name,String status, int page, int rows, String sidx, String sord);
	Long findAccountByCompanyName(String companyName);
	Long findProductByname(String productName);
	List<String> findAccountFunction(Long id);
	Order findAccountById(Long id);
	Order findByAccount(Long id);
	List<Functions> findFunctionsList(Long long1);
	List<Functions> findFunctionsList();
	Order findAccountByIdAndDate(Long accountId, Date begin_date);
	void checkOrder();
	void addVip(Long id);
}
