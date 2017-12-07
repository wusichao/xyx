package com.xyx.ls.service.account;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.xyx.ls.bean.GridBean;
import com.xyx.ls.model.account.Account;

public interface AccountService {
	void save(Account Account, HttpServletRequest request,
			HttpServletResponse response);

	Account login(String name, String pw);

	void approval(Long l, String status);

	GridBean<Account> getAccount(Boolean isApproved, int page, int rows,
			String company_name, String sidx, String sord);

	Account findByEmail(String email, Long id);

	Account findByCompanyname(String company_name, Long id);

	Account findByWebsite(String web_site, Long id);

	Account findByEmailIsApproved(String email);

	void resetPassword(String email, String password);

	void update(Account account);

	void updataAttributionCycle(String string, Long attributionCycle);

	List<Account> getApprovedCompanynameList();

	Account findById(Long id);

	void changeStutas(Long id);

	void exportInvitationCode(List<String> invitationCodeList,
			HttpServletResponse response);

	String isHasInvitationCode(String invitationCode);

	void updateCodeStatus(String invitation_code);

	boolean findAccountNoApproved(String email, String md5Hex);

	void deleteAccount(Long id);
}
