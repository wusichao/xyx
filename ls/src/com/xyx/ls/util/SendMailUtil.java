package com.xyx.ls.util;

import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailUtil {
	public static void sendMail(String fromMail, String user, String password,
			String toMail, String mailTitle, String mailContent)
			throws Exception {
		Properties props = new Properties(); // 可以加载一个配置文件
		// 使用smtp：简单邮件传输协议
//		props.put("mail.smtp.host", "smtp.mxhichina.com");// 存储发送邮件服务器的信息
		 props.put("mail.smtp.host", "smtp.163.com");
		props.put("mail.smtp.auth", "true");// 同时通过验证
		props.setProperty("mail.transport.protocol", "smtp");
		Session session = Session.getInstance(props);// 根据属性新建一个邮件会话
		// session.setDebug(true); //有他会打印一些调试信息。

		MimeMessage message = new MimeMessage(session);// 由邮件会话新建一个消息对象
		message.setFrom(new InternetAddress(fromMail));// 设置发件人的地址
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(
				toMail));// 设置收件人,并设置其接收类型为TO
		message.setSubject(mailTitle);// 设置标题
		// 设置信件内容
//		 message.setText(mailContent); //发送 纯文本 邮件 todo
		message.setContent(mailContent, "text/html;charset=utf-8"); // 发送HTML邮件，内容样式比较丰富
		message.setSentDate(new Date());// 设置发信时间
		message.saveChanges();// 存储邮件信息

		// 发送邮件
		// Transport transport = session.getTransport("smtp");
		Transport transport = session.getTransport();
		transport.connect(user, password);
		transport.sendMessage(message, message.getAllRecipients());// 发送邮件,其中第二个参数是所有已设好的收件人地址
		transport.close();
	}
	public static void main(String[] args) throws Exception {
//		sendMail("service@xyxtech.com", "service@xyxtech.com", "Service_xyxtech", "1033429246@qq.com", "Java Mail 测试邮件",
//				"<a href=\"http://localhost:8080/ls/login.html\">前往登录</a>");
//		sendMail("service@xyxtech.com",
//				"service@xyxtech.com",
//				"Service_xyxtech", "service@xyxtech.com", "灵石Marketing Plus产品到期提醒", "<h3>尊敬的用户：</h3>您订购的产品将于 23:59:59到期，请及时联系我们续签，以免影响您的正常使用。<br/><p>感谢您选择灵石Marketing Plus!</p><p>——————</p><p>西游行运营团队</p>");
//		sendMail("wusichao2011@163.com", "wusichao2011", "wsc15171545112", "1033429246@qq.com", "Java Mail 测试邮件",
//				"审批通过<a href=\"http://localhost:8080/ls/login.html\">前往登录</a>");
		try {
			SendMailUtil.sendMail(
					"wusichao2011@163.com", "wusichao2011", "wsc15171545112", "1033429246@qq.com",
					"灵石Marketing Plus产品注册成功提醒",
					"<h3>尊敬的管理员：</h3>"
					+ "<p>有客户选择灵石Marketing Plus，并已成功提交注册信息，请关注!</p><br/>"
					+ "客户名称:xyx<br/>"
					+ "邀请码:ABC<br/>"
					+ "<p>——————</p><p>西游行运营团队</p></div>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
