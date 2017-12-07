package com.xyx.ls.dao.account;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xyx.ls.model.account.Account;

public interface AccountMapper {

	void saveAccount(Account account);

	Account login(String email, String pw);

	void approval(Long id, String status);

	Account findByEmail(@Param("email")String email, @Param("id")Long id);

	Account findByCompanyname(@Param("company_name")String company_name,@Param("id")Long id);

	Account findByWebsite(@Param("web_site")String web_site, @Param("id")Long id);

	List<Account> findAccountApproved(HashMap<String, String> map);

	List<Account> findAccountNoApproved(HashMap<String, String> map);

	int getRecords(String company_name);

	int getIsRecords(String company_name);

	void isApproval(Long id);

	void noApproval(Long id);

	String findEmailById(Long id);

	Account findByEmailIsApproved(String email);

	void resetPassword(String email, String password);

	void update(Account account);

	void updataAttributionCycle(String string, Long attributionCycle);

	List<Account> getApprovedCompanynameList();

	Account findById(Long id);

	void changeStutas(Long id);

	void savaInvitationCode(String str, Date date);

	String findInvitationCodeStatus(String invitationCode);

	void updateCodeStatus(String invitation_code);

	Account findAccountNoApprove(String email, String md5Hex);

	void deleteAccount(Long id);
}
