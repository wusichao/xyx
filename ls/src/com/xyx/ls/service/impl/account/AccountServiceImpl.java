package com.xyx.ls.service.impl.account;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xyx.ls.bean.GridBean;
import com.xyx.ls.dao.account.AccountMapper;
import com.xyx.ls.model.account.Account;
import com.xyx.ls.service.account.AccountService;
import com.xyx.ls.util.FileUploadUtil;
import com.xyx.ls.util.SendMailUtil;
import com.xyx.x.utils.ServletUtils;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	@Resource
	private AccountMapper userMapper;

	@Override
	public void save(Account Account, HttpServletRequest request,
			HttpServletResponse response) {
		// 上传营业执照
		Account.setLicense_path("/image/"+FileUploadUtil.uploadImg(request, response));
		Account.setLast_modified(new Date());
		Account.setWeb_site(ServletUtils.encode(Account.getWeb_site()));
		userMapper.saveAccount(Account);
	}

	public AccountMapper getUserMapper() {
		return userMapper;
	}

	public void setUserMapper(AccountMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Override
	public void approval(Long id, String status) {
		try {
			userMapper.approval(id, status);
			String email = userMapper.findEmailById(id);
			if (status.equals("已通过")) {
				userMapper.isApproval(id);
				SendMailUtil
						.sendMail(
								"wusichao2011@163.com", "wusichao2011", "wsc15171545112",
								email,
								"灵石Marketing Plus产品注册成功",
								"<h3>尊敬的用户：</h3><p>感谢您选择灵石Marketing Plus，您的注册信息已通过审核，可以通过以下链接进行登录</p><a href=\"http://xyxtech.com/ls/loginUI.do\">http://xyxtech.com/ls/loginUI.do</a><p>(如果点击链接无反应，请复制链接到浏览器里直接打开)</p><br/><p>——————</p><p>西游行运营团队</p></div>");
			} else {
				userMapper.noApproval(id);
				SendMailUtil
						.sendMail(
								"wusichao2011@163.com", "wusichao2011", "wsc15171545112",
								email,
								"灵石Marketing Plus产品注册失败",
								"<h3>尊敬的用户：</h3><p>感谢您选择灵石Marketing Plus，您的注册信息未通过审核，可以通过以下链接进行修改</p><a href=\"http://xyxtech.com/ls/html/register.html?email="
										+ email
										+ "\">http://xyxtech.com/ls/html/register.html?email="
										+ email
										+ "</a><p>(如果点击链接无反应，请复制链接到浏览器里直接打开)</p><br/><p>——————</p><p>西游行运营团队</p></div>");
			}
		} catch (Exception e) {

		}
	}

	@Override
	public GridBean<Account> getAccount(Boolean isApproved, int page, int rows,
			String company_name, String sidx, String sord) {
		int start = (page - 1) * rows;
		int rowpage = rows * page;
		GridBean<Account> gridBean = new GridBean<Account>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("sidx", sidx);
		map.put("sord", sord);
		map.put("company_name", company_name);
		map.put("start", start + "");
		map.put("rowpage", rowpage + "");
		if (isApproved) {
			int records = userMapper.getRecords(company_name);
			int total;
			if (records % rows == 0) {
				total = records / rows;
			} else {
				total = records / rows + 1;
			}

			List<Account> accountList = userMapper.findAccountApproved(map);
			for (Account account : accountList) {

				String img = "<a class='abc'   style='cursor:pointer ;width:50px; height:50px;z-index:9999'  data-toggle='modal'  data-target='#photo' onmouseover='javascript:creativeShow(this)'><img class='b' style='cursor:pointer ;width:50px; height:50px;' src='"+account.getLicense_path()+"' /></a>";
				account.setLicense_path(img);
				String delete = "<a style='text-decoration:underline' href='javascript:deleteAccount("
						+ account.getId() + ")'>删除</a>";
				account.setDelete(delete);
			}
			gridBean.setPage(page);
			gridBean.setRecords(records);
			gridBean.setTotal(total);
			gridBean.setRows(accountList);
			return gridBean;
		} else {
			int records = userMapper.getIsRecords(company_name);
			int total;
			if (records % rows == 0) {
				total = records / rows;
			} else {
				total = records / rows + 1;
			}
			List<Account> accountList = userMapper.findAccountNoApproved(map);
			for (Account account : accountList) {
				String img = "<a class='abc'  style='cursor:pointer ;width:50px; height:50px;z-index:9999'  data-toggle='modal'  data-target='#photo'  onmouseover='javascript:creativeShow(this)'><img class='b'style='cursor:pointer ;width:50px; height:50px;' src='"
						+ account.getLicense_path()+ "' /></a>";
				account.setLicense_path(img);
				String delete = "<a style='text-decoration:underline' href='javascript:deleteAccount("
						+ account.getId() + ")'>删除</a>";
				account.setDelete(delete);
			}
			gridBean.setPage(page);
			gridBean.setRecords(records);
			gridBean.setTotal(total);
			gridBean.setRows(accountList);
			return gridBean;
		}

	}

	@Override
	public Account login(String name, String pw) {
		return userMapper.login(name, pw);
	}

	@Override
	public Account findByEmail(String email, Long id) {
		return userMapper.findByEmail(email, id);
	}

	@Override
	public Account findByCompanyname(String company_name, Long id) {
		return userMapper.findByCompanyname(company_name, id);
	}

	@Override
	public Account findByWebsite(String web_site, Long id) {
		return userMapper.findByWebsite(web_site, id);
	}

	@Override
	public Account findByEmailIsApproved(String email) {

		return userMapper.findByEmailIsApproved(email);
	}

	@Override
	public void resetPassword(String email, String password) {
		userMapper.resetPassword(email, password);

	}

	@Override
	public void update(Account account) {
		account.setLast_modified(new Date());
		account.setWeb_site(ServletUtils.encode(account.getWeb_site()));
		userMapper.update(account);
	}

	@Override
	public void updataAttributionCycle(String string, Long attributionCycle) {
		userMapper.updataAttributionCycle(string, attributionCycle);
	}

	@Override
	public List<Account> getApprovedCompanynameList() {
		return userMapper.getApprovedCompanynameList();
	}

	@Override
	public Account findById(Long id) {
		return userMapper.findById(id);
	}

	@Override
	public void changeStutas(Long id) {
		userMapper.changeStutas(id);
	}

	@Override
	public void exportInvitationCode(List<String> invitationCodeList,
			HttpServletResponse response) {
		try {
			SimpleDateFormat times = new SimpleDateFormat("yyyyMMdd-HH：mm：ss");
			String time = times.format(new Date());
			String name = time + "邀请码.xls";
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			response.setHeader("Content-Disposition", "attachment; filename="
					+ java.net.URLEncoder.encode(name, "UTF-8"));
			response.setContentType("application/msexcel");// 定义输出类型
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("sheet", 0);
			int i = 0;
			for (String str : invitationCodeList) {
				userMapper.savaInvitationCode(str, new Date());
				sheet.addCell(new Label(0, i, str));
				i++;
			}
			workbook.write();
			workbook.close();
		} catch (Exception e) {
		}

	}

	@Override
	public String isHasInvitationCode(String invitationCode) {
		return userMapper.findInvitationCodeStatus(invitationCode);
	}

	@Override
	public void updateCodeStatus(String invitation_code) {
		userMapper.updateCodeStatus(invitation_code);
	}

	@Override
	public boolean findAccountNoApproved(String email, String md5Hex) {
		if (userMapper.findAccountNoApprove(email, md5Hex) != null) {
			return true;
		}
		return false;
	}

	@Override
	public void deleteAccount(Long id) {
		userMapper.deleteAccount(id);
	}
}
