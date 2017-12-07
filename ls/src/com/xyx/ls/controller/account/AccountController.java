package com.xyx.ls.controller.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.xyx.ls.bean.GridBean;
import com.xyx.ls.model.account.Account;
import com.xyx.ls.model.order.Order;
import com.xyx.ls.service.account.AccountService;
import com.xyx.ls.service.order.OrderService;
import com.xyx.ls.util.FileUploadUtil;
import com.xyx.ls.util.ParameterCheckUtil;
import com.xyx.ls.util.SendMailUtil;
import com.xyx.ls.util.ValidataCodeUtil;
import com.xyx.ls.util.Write2Response;
import com.xyx.x.utils.ServletUtils;

@Controller
public class AccountController {
	@Resource
	private AccountService userService;
	@Resource
	private OrderService orderService;

	// 注册页面
	@RequestMapping("registerUI")
	public String registerUI(String email, HttpServletRequest request,
			HttpServletResponse response) {
		return "register";
	}

	// 审批
	@RequestMapping("approvalUI")
	public String approvalUI(HttpServletRequest request,
			HttpServletResponse response) {
		return "approval";
	}

	@RequestMapping("registerData")
	public void registerData(String email, HttpServletRequest request,
			HttpServletResponse response) {
		if (email != null) {
			Account account = userService.findByEmail(email, -1l);
			if (account != null) {
				account.setPassWord("");
				account.setLicense_path(account.getLicense_path());
				
				JSON jsonArray = (JSON) JSON.toJSON(account);
				Write2Response.write2Res(response, jsonArray.toString());
				account.setWeb_site(ServletUtils.decode(account.getWeb_site()));
				request.getSession().setAttribute("accountId", account.getId());
			}

		} else {
			Write2Response.write2Res(response, "false");
		}
	}

	@RequestMapping("addAccount")
	public String addAccount(Account account, String token,HttpServletRequest request,
			HttpServletResponse response) {
		String uuid= java.util.UUID.randomUUID().toString();
		Long id = (Long) request.getSession().getAttribute("accountId");
		Account accountss = userService.findById(id);
		//验证重复提交
		String sessionToken = (String) request.getSession().getAttribute("token");
		if(!sessionToken.equals(token)){
			if (!"".equals(account.getInvitation_code())) {
				return "after-free";
			}else{
				return "after-re";
			}
			
		}
		if(sessionToken.equals(token)){
		request.getSession().setAttribute("token",uuid);
		if (id != null && checkData(account)) {
			account.setId(id);
			account.setPassWord(DigestUtils.md5Hex(account.getPassWord()));
			account.setLicense_path("/image/"+FileUploadUtil.uploadImg(request, response));
			if (account.getLicense_path() == null) {
				account.setLicense_path(accountss.getLicense_path());
			}
			account.setLast_modified(new Date());
			userService.update(account);
			userService.changeStutas(id);
			request.getSession().removeAttribute("accountId");
			// 有邀请码的用户
			if (!"".equals(account.getInvitation_code())) {
				userService.approval(account.getId(), "已通过");
				orderService.addVip(account.getId());
				userService.updateCodeStatus(account.getInvitation_code());
				try {
					SendMailUtil.sendMail(
							"wusichao2011@163.com", "wusichao2011", "wsc15171545112", "1033429246@qq.com",
							"灵石Marketing Plus产品注册成功提醒",
							"<h3>尊敬的管理员：</h3>"
							+ "<p>有客户选择灵石Marketing Plus，并已成功提交注册信息，请关注!</p><br/>"
							+ "客户名称:"+account.getCompany_name()+"<br/>"
							+ "邀请码:"+account.getInvitation_code()+"<br/>"
							+ "<p>——————</p><p>西游行运营团队</p></div>");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "after-free";
			} else {
				try {
					SendMailUtil.sendMail(
							"wusichao2011@163.com", "wusichao2011", "wsc15171545112", "1033429246@qq.com",
							"灵石Marketing Plus产品注册成功提醒",
							"<h3>尊敬的管理员：</h3>"
							+ "<p>有客户选择灵石Marketing Plus，并已成功提交注册信息，请关注!</p><br/>"
							+ "客户名称:"+account.getCompany_name()+"<br/>"
							+ "<p>——————</p><p>西游行运营团队</p></div>");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "after-re";
			}

		} else {
			if (checkData(account)) {
				// md5加密密码解密
				account.setPassWord(DigestUtils.md5Hex(account.getPassWord()));
				userService.save(account, request, response);
				// 有邀请码的用户
				if (!"".equals(account.getInvitation_code())) {
					userService.approval(account.getId(), "已通过");
					orderService.addVip(account.getId());
					userService.updateCodeStatus(account.getInvitation_code());
					try {
						SendMailUtil.sendMail(
								"wusichao2011@163.com", "wusichao2011", "wsc15171545112", "1033429246@qq.com",
								"灵石Marketing Plus产品注册成功提醒",
								"<h3>尊敬的管理员：</h3>"
								+ "<p>有客户选择灵石Marketing Plus，并已成功提交注册信息，请关注!</p><br/>"
								+ "客户名称:"+account.getCompany_name()+"<br/>"
								+ "邀请码:"+account.getInvitation_code()+"<br/>"
								+ "<p>——————</p><p>西游行运营团队</p></div>");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return "after-free";
				} else {
					try {
						SendMailUtil.sendMail(
								"wusichao2011@163.com", "wusichao2011", "wsc15171545112", "1033429246@qq.com",
								"灵石Marketing Plus产品注册成功提醒",
								"<h3>尊敬的管理员：</h3>"
								+ "<p>有客户选择灵石Marketing Plus，并已成功提交注册信息，请关注!</p><br/>"
								+ "客户名称:"+account.getCompany_name()+"<br/>"
								+ "<p>——————</p><p>西游行运营团队</p></div>");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return "after-re";
				}

			}
		}
		}
		return "";

	}

	/**
	 * 验证注册信息
	 * 
	 * @param account
	 * @return
	 */
	private boolean checkData(Account account) {
		if (!ParameterCheckUtil.checkMailFormat(account.getEmail())) {
			return false;
		}
		if (!ParameterCheckUtil.checkLength(account.getEmail(), 40)) {
			return false;
		}
		if (!ParameterCheckUtil.IsPasswLength(account.getPassWord())) {
			return false;
		}
		if (!ParameterCheckUtil.checkLength(account.getCompany_name(), 60)) {
			return false;
		}
		if (!ParameterCheckUtil.checkLength(account.getWeb_site(), 60)) {
			return false;
		}
		if (!ParameterCheckUtil.checkLength(account.getContact(), 40)) {
			return false;
		}
		if (!ParameterCheckUtil.IsTelephone(account.getCellphone())) {
			return false;
		}
		if (!ParameterCheckUtil.checkLength(account.getInvitation_code(), 40)) {
			return false;
		}
		return true;
	}

	// 获取审批列表
	@RequestMapping("approval")
	public void findAccount(Boolean isApproved, int page, int rows,
			String sidx, String sord, String company_name,
			HttpServletRequest request, HttpServletResponse response) {
		GridBean<Account> gridBean = userService.getAccount(isApproved, page,
				rows, company_name, sidx, sord);
		JSON jsonArray = (JSON) JSON.toJSON(gridBean);
		Write2Response.write2Res(response, jsonArray.toString());
	}

	// 审批
	@RequestMapping("approvalOP")
	public void approval(String id, String status, HttpServletRequest request,
			HttpServletResponse response) {
		String[] id_Array = id.split(",");
		for (String check_id : id_Array) {
			long l = Long.parseLong(check_id);
			userService.approval(l, status);
		}
		Write2Response.write2Res(response, "");
	}

	// 登录页面
	@RequestMapping("loginUI")
	public String loginUI(HttpServletRequest request,HttpServletResponse response) {
//		String xid = ServletUtils.getCookie("XID",request);
//		if(xid == null){
//			String xid = XidUtils.generateUserId();
//			ServletUtils.setCookie("XID", xid,response);
//		}
		return "login";
	}

	// 登录
	@RequestMapping("login")
	public void login(String email, String passWord, String code,
			HttpServletRequest request, HttpServletResponse response) {
		String code1 = null;
		Account account = null;
		String iscode=(String) request.getSession().getAttribute("iscode");
		if("true".equals(iscode)&&"".equals(code)){
			Write2Response.write2Res(response, "iscode");
		}
		//需要验证码
		if("true".equals(iscode)&&code != null && !"".equals(code)){
			code1 = request.getSession().getAttribute("code").toString();
			if (code1.equalsIgnoreCase(code)) {
				// md5加密密码解密
				account = userService
						.login(email, DigestUtils.md5Hex(passWord));
				if (account != null && email.equals("admin")) {
					account.setWeb_site(ServletUtils.decode(account.getWeb_site()));
					request.getSession().setAttribute("account", account);
					request.getSession().removeAttribute("iscode");
					Write2Response.write2Res(response, "admin");
				}
				if (userService.login(email, DigestUtils.md5Hex(passWord)) != null) {
					if(isOrder(account.getId())){
						account.setWeb_site(ServletUtils.decode(account.getWeb_site()));
						request.getSession().setAttribute("account", account);
						request.getSession().removeAttribute("iscode");
						Write2Response.write2Res(response, "true");
					}else{
						Write2Response.write2Res(response, "NoOrder");
					}
					

				} else {
					if (userService.findAccountNoApproved(email,
							DigestUtils.md5Hex(passWord))) {
						request.getSession().removeAttribute("iscode");
						Write2Response.write2Res(response, "accountNoApproved");
					} else {
						Write2Response.write2Res(response, "codefalse");
					}
				}

			} else {
				Write2Response.write2Res(response, "codeerror");
			}
		}
		else {
			if(iscode==null){
			account = userService.login(email, DigestUtils.md5Hex(passWord));
			if (account != null && email.equals("admin")) {
				account.setWeb_site(ServletUtils.decode(account.getWeb_site()));
				request.getSession().setAttribute("account", account);
				Write2Response.write2Res(response, "admin");
			}
			if (userService.login(email, DigestUtils.md5Hex(passWord)) != null) {
				if(isOrder(account.getId())){
					account.setWeb_site(ServletUtils.decode(account.getWeb_site()));
					request.getSession().setAttribute("account", account);
					Write2Response.write2Res(response, "true");
				}else{
					Write2Response.write2Res(response, "NoOrder");
				}

			} else {
				if (userService.findAccountNoApproved(email,
						DigestUtils.md5Hex(passWord))) {
					Write2Response.write2Res(response, "accountNoApproved");
				} else {
					if (request.getSession().getAttribute("num") == null) {
						request.getSession().setAttribute("num", 1);
						Write2Response.write2Res(response, "false");
					} else if ((Integer) request.getSession().getAttribute(
							"num") == 1) {
						request.getSession().setAttribute("num", 2);
						Write2Response.write2Res(response, "false");
					} else if ((Integer) request.getSession().getAttribute(
							"num") == 2) {
						request.getSession().setAttribute("num", 3);
						Write2Response.write2Res(response, "false");
					} else {
						request.getSession().setAttribute("iscode", "true");
						Write2Response.write2Res(response, "code");
					}
				}

			}
		}}
	}

	private boolean isOrder(Long id) {
		Order order=orderService.findAccountById(id);
		if(order==null){
			return false;
		}
		return true;
	}

	/**
	 * 验证邮箱是否重复
	 * 
	 * @param email
	 * @param response
	 * @return
	 */
	@RequestMapping("validataEmail")
	public void validataEmail(String email, HttpServletRequest request,
			HttpServletResponse response) {
		Long id = (Long) request.getSession().getAttribute("accountId");
		if (id == null) {
			id = -1l;
		}
		;
		if (userService.findByEmail(email, id) != null) {
			Write2Response.write2Res(response, "false");
		}
		Write2Response.write2Res(response, "true");
	}

	/**
	 * 验证公司名称是否重复
	 * 
	 * @param company_name
	 * @param response
	 * @return
	 */
	@RequestMapping("validataCompanyname")
	public void validataCompanyname(String company_name,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = (Long) request.getSession().getAttribute("accountId");
		if (id == null) {
			id = -1l;
		}
		;
		if (userService.findByCompanyname(company_name, id) != null) {
			Write2Response.write2Res(response, "false");
		}
		Write2Response.write2Res(response, "true");
	}

	/**
	 * 验证网站是否重复
	 * 
	 * @param web_site
	 * @param response
	 * @return
	 */
	@RequestMapping("validataWebsite")
	public void validataWebsite(String web_site, HttpServletRequest request,
			HttpServletResponse response) {
		Long id = (Long) request.getSession().getAttribute("accountId");
		if (id == null) {
			id = -1l;
		}
		;
		if (userService.findByWebsite(web_site, id) != null) {
			Write2Response.write2Res(response, "false");
		}
		Write2Response.write2Res(response, "true");
	}

	// 找回密码验证登录名
	@RequestMapping("validataEmailIsApproved")
	public void validataEmailIsApproved(String email,
			HttpServletResponse response) {
		if (userService.findByEmailIsApproved(email) == null) {
			Write2Response.write2Res(response, "false");
		}
		Write2Response.write2Res(response, "true");
	}

	// 重置密码发送邮件
	@RequestMapping("sendCode")
	public void sendCode(String email,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Random rand = new Random();
		Integer randNum = rand.nextInt(899999) + 100000;
		String sendCode = randNum.toString();
		request.getSession().setAttribute("sendCode",sendCode);
		SendMailUtil.sendMail("wusichao2011@163.com", "wusichao2011", "wsc15171545112", email, "灵石Marketing Plus产品密码重置",
				"<h3>尊敬的用户：</h3>您正在重置登陆密码，验证码为：" + sendCode
						+ "<br/><p>——————</p><p>西游行运营团队</p>");
		Write2Response.write2Res(response, "true");
	}

	// 验证验证码
	@RequestMapping("validataCode")
	public void validataCode(String code, HttpServletRequest request,HttpServletResponse response){
		if (code != null && !"".equals(code)) {
			String sendCode=(String) request.getSession().getAttribute("sendCode");
			Write2Response.write2Res(response, code.equals(sendCode) ? "true"
					: "false");
		} else {
			Write2Response.write2Res(response, "null");
		}

	}

	// 重置密码
	@RequestMapping("resetPassword")
	public void resetPassword(String email, String password,
			HttpServletResponse response) {
		userService.resetPassword(email, DigestUtils.md5Hex(password));
		Write2Response.write2Res(response, "true");
	}

	// 修改页面准备数据
	@RequestMapping("updateAccountUI")
	public void updateAccountUI(HttpServletRequest request,
			HttpServletResponse response) {
		Account account = (Account) request.getSession()
				.getAttribute("account");
		JSON jsonArray = (JSON) JSON.toJSON(account);
		Write2Response.write2Res(response, jsonArray.toString());
	}

	// 修改账户
	@RequestMapping("updateAccount")
	public void updateAccount(Account account, HttpServletRequest request,
			HttpServletResponse response) {
		if(ParameterCheckUtil.checkLength(account.getContact(), 40)&&ParameterCheckUtil.IsTelephone(account.getCellphone())){
		Account oldAccount = (Account) request.getSession().getAttribute(
				"account");
		if (!account.getContact().equals(oldAccount.getContact())) {
			oldAccount.setContact(account.getContact());
		}
		if (!account.getCellphone().equals(oldAccount.getCellphone())) {
			oldAccount.setCellphone(account.getCellphone());
		}
//		if (!account.getCompany_name().equals(oldAccount.getCompany_name())) {
//			oldAccount.setCompany_name(account.getCompany_name());
//		}
		if (!account.getVertical().equals(oldAccount.getVertical())) {
			oldAccount.setVertical(account.getVertical());
		}
		userService.update(oldAccount);
		Write2Response.write2Res(response, "true");}
	}

	// 修改密码
	@RequestMapping("updatePasswordValidada")
	public void updatePasswordValidada(String oldPassword, String password,
			HttpServletRequest request, HttpServletResponse response) {
		if(ParameterCheckUtil.IsPasswLength(password)){
		Account account = (Account) request.getSession()
				.getAttribute("account");
		// md5加密密码解密
		oldPassword = DigestUtils.md5Hex(oldPassword);
		if (account.getPassWord().equals(oldPassword)) {
			userService.resetPassword(account.getEmail(),
					DigestUtils.md5Hex(password));
			Write2Response.write2Res(response, "true");
		} else {
			Write2Response.write2Res(response, "false");
		}}
	}

	// // 修改密码
	// @RequestMapping("uodatePassword")
	// public void uodatePassword(String password, HttpServletRequest request) {
	// Account account = (Account) request.getSession()
	// .getAttribute("account");
	// userService.resetPassword(account.getEmail(), password);
	// }

	// 回溯周期数据显示
	@RequestMapping("attributionCycleData")
	public void attributionCycleData(HttpServletRequest request,
			HttpServletResponse response) {
		Account account = (Account) request.getSession()
				.getAttribute("account");
		Write2Response.write2Res(response, account.getAttributionCycle()
				.toString());
	}

	// 修改回溯周期
	@RequestMapping("updataAttributionCycle")
	public void updataAttributionCycle(Long attributionCycle,
			HttpServletRequest request, HttpServletResponse response) {
		if (attributionCycle < 0 || attributionCycle > 15) {
			Write2Response.write2Res(response, "false");
		} else {
			Account account = (Account) request.getSession().getAttribute(
					"account");
			userService.updataAttributionCycle(account.getEmail(),
					attributionCycle);
			account.setAttributionCycle(attributionCycle);
			Write2Response.write2Res(response, "true");
		}
	}

	// 发送图片验证码
	@RequestMapping("sendValidataCode")
	public void sendValidataCode(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ValidataCodeUtil.getCode(request, response);
	}

	// 验证图片验证码
	@RequestMapping("loginValidataCode")
	public void loginValidataCode(String code, HttpServletRequest request,
			HttpServletResponse response) {
		String code1 = request.getSession().getAttribute(code).toString();
		Write2Response.write2Res(response,
				code.equalsIgnoreCase(code1) ? "true" : "false");
	}

	// 已审批通过用户列表
	@RequestMapping("approvedCompanynameList")
	public void approvedCompanynameList(HttpServletRequest request,
			HttpServletResponse response) {
		Account account = (Account) request.getSession()
				.getAttribute("account");
		if ("admin".equals(account.getEmail())) {
			List<Account> approvedCompanynameList = userService
					.getApprovedCompanynameList();
			JSON jsonArray = (JSON) JSON.toJSON(approvedCompanynameList);
			Write2Response.write2Res(response, jsonArray.toString());
		} else {
			Write2Response.write2Res(response, "权限不足");
		}
	}

	// 管理人员切换用户
	@RequestMapping("cutAccount")
	public void cutAccount(String company_name, HttpServletRequest request,
			HttpServletResponse response) {
		Account account = userService.findByCompanyname(company_name, -1l);
		account.setWeb_site(ServletUtils.decode(account.getWeb_site()));
		request.getSession().setAttribute("account", account);
		Write2Response.write2Res(response, "true");
	}

	// 判断是否是管理员
	@RequestMapping("isAdmin")
	public void isAdmin(HttpServletRequest request, HttpServletResponse response) {
		Account account = (Account) request.getSession()
				.getAttribute("account");
		Write2Response.write2Res(response,
				"admin".equals(account.getEmail()) ? "true" : "false");
	}

	// 注销
	@RequestMapping("logout")
	public String logout(HttpServletRequest request) {
		request.getSession().removeAttribute("account");
		return "login";
	}

	@RequestMapping("getAccountName")
	public void getAccountName(HttpServletRequest request,
			HttpServletResponse response) {
		Account account = (Account) request.getSession()
				.getAttribute("account");
		Write2Response.write2Res(response, account.getEmail());
	}

	// 审批未通过修改注册信息
	@RequestMapping("registerUpdate")
	public void registerUpdate(Account account, HttpServletRequest request,
			HttpServletResponse response) {
		userService.update(account);
	}

	// 生成邀请码
	@RequestMapping("productInvitationCode")
	public void productInvitationCode(HttpServletRequest request,
			HttpServletResponse response) {
		List<String> invitationCodeList = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			String invitationCode = UUID.randomUUID().toString().substring(24);
			invitationCodeList.add(invitationCode);
		}
		userService.exportInvitationCode(invitationCodeList, response);
	}

	// //判断邀请码是否存在
	// @RequestMapping("isHasInvitationCode")
	// public void isHasInvitationCode(String invitationCode,HttpServletRequest
	// request,HttpServletResponse response){
	// String status =userService.isHasInvitationCode(invitationCode);
	// if("".equals(status)||status==null){
	// Write2Response.write2Res(response,"false");
	// }else{
	// Write2Response.write2Res(response,"true");
	// }
	// }
	// 判断邀请码是否有效
	@RequestMapping("isInvitationCode")
	public void isInvitationCode(String invitation_code,
			HttpServletRequest request, HttpServletResponse response) {
		String status = userService.isHasInvitationCode(invitation_code);
		if ("未使用".equals(status)) {
			Write2Response.write2Res(response, "true");
		} else {
			Write2Response.write2Res(response, "false");
		}
	}
	//验证session
	@RequestMapping("validateSession")
	public void validateSession(HttpServletRequest request,HttpServletResponse response){
		Account account =(Account) request.getSession().getAttribute("account");
		if (account!=null) {
			Write2Response.write2Res(response, "true");
		} else {
			Write2Response.write2Res(response, "false");
		}
	}
	//删除用户
	@RequestMapping("deleteAccount")
	public void deleteAccount(Long id,HttpServletResponse response){
		userService.deleteAccount(id);
		Write2Response.write2Res(response, "true");
	}
	//获取用户产品有效期
	@RequestMapping("findServiceLife")
	public void findServiceLife(HttpServletRequest request,HttpServletResponse response){
		Account account =(Account) request.getSession().getAttribute("account");
		String serviceLife=orderService.findByAccount(account.getId()).getServiceLife();
		Write2Response.write2Res(response, serviceLife);
	}
	//重复提交验证码
	@RequestMapping("registerMakeToken")
	public void registerMakeToken(HttpServletRequest request,HttpServletResponse response){
		String token = java.util.UUID.randomUUID().toString();
		request.getSession().setAttribute("token",token);
		System.out.println(token);
		Write2Response.write2Res(response, token);
	}
}
