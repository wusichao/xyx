package com.xyx.ls.dao.order;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.xyx.ls.model.account.Account;
import com.xyx.ls.model.order.Functions;
import com.xyx.ls.model.order.Order;

public interface OrderMapper {
	void add( Order order);
	Order findByIds(Long id);
	List<Order> findAll(HashMap<String, String> map);
	int getRecords(HashMap<String, String> mapre);
	Long findAccountByCompanyName(String companyName);
	Long findProductByname(String productName);
	List<String> findAccountFunction(Long id);
	Order findAccountById(Long id);
	void addOrdersFunctions(Long id, Long valueOf);
	List<String> findFunctionsByOrdersId(Long id);
	List<Functions> findFunctionsList(Long id);
	Order findByAccount(Long id);
	List<Functions> findFunctionsLists();
	Order findAccountByIdAndDate(Long accountId, Date begin_date);
	List<Order> checkOrder();
	void addVip(Order order);
	void addVipRel(Long id, int i);
}
