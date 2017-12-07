package com.xyx.ls.model.account;

import java.io.Serializable;
import java.util.Date;

import com.xyx.x.utils.ServletUtils;


/**
 * 账户信息
 * 
 * @author Administrator
 * 
 */
public class Account implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;// 账户id
	private String email; // 注册邮箱
	private String passWord;// 登录密码
	private String contact;// 联系人
	private String cellphone; // 联系电话
	private String invitation_code;// 邀请码
	private String status;// 审核状态 NOT_CHECKED APPROVED DISAPPROVED
	private boolean approved;// bit(1) DEFAULT b'0', -- 是否审核通过
	private String company_name;// 公司名称
	private String vertical; // 行业
	private String web_site;// 公司网址
	private String license_path; // 营业执照,文件存储在服务器，此处是文件路径
	private boolean active; // bit(1) DEFAULT b'0', -- 是否激活
	private Long attributionCycle=1L;// 回溯周期
	private Date last_modified;
	private String delete;
	private String domain;
	private Integer domain_type;
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			{return false;}else{
				other.attributionCycle++;}
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		
		return true;
	}

	public Integer getDomain_type() {
		return domain_type;
	}

	public void setDomain_type(Integer domain_type) {
		this.domain_type = domain_type;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDelete() {
		return delete;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}

	public Date getLast_modified() {
		return last_modified;
	}

	public void setLast_modified(Date last_modified) {
		this.last_modified = last_modified;
	}

	public Long getAttributionCycle() {
		return attributionCycle;
	}

	public void setAttributionCycle(Long attributionCycle) {
		this.attributionCycle = attributionCycle;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getInvitation_code() {
		return invitation_code;
	}

	public void setInvitation_code(String invitation_code) {
		this.invitation_code = invitation_code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getVertical() {
		return vertical;
	}

	public void setVertical(String vertical) {
		this.vertical = vertical;
	}

	public String getWeb_site() {
		return web_site;
	}

	public void setWeb_site(String web_site) {
		this.web_site = web_site;
	}

	public String getLicense_path() {
		return license_path;
	}

	public void setLicense_path(String license_path) {
		this.license_path = license_path;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
