package com.yishenxiao.commons.utils;

import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.util.MailSSLSocketFactory;
import com.yishenxiao.commons.beans.EmailBean;

public class ExmailUtils {
	
	  private static Logger logger = LoggerFactory.getLogger(ExmailUtils.class);
	  
	  public static void sendMail(HttpServletRequest req, EmailBean emailBean){
		   String path = req.getSession().getServletContext().getRealPath("/")+"WEB-INF/classes/email.properties";
	       final Properties properties = PropertiesUtils.getProperties(path);      
	       //协议
	       properties.put("mail.transport.protocol", properties.get("mail.protocol"));
	       // 表示SMTP发送邮件，必须进行身份验证
	       properties.put("mail.smtp.auth", "true");
	       //此处填写SMTP服务器
	       properties.put("mail.smtp.host", properties.get("mail.smtp.host"));
	       //端口号，QQ邮箱给出了两个端口，但是另一个我一直使用不了，所以就给出这一个587
	       properties.put("mail.smtp.port", properties.get("mail.smtp.port"));
	       // 此处填写你的账号
	       properties.put("mail.user", properties.get("mail.user"));
	       // 此处的密码就是前面说的16位STMP口令
	       properties.put("mail.password", properties.get("mail.password"));
	       
	       //使用SSL，企业邮箱必需！
	       //开启安全协议
	       MailSSLSocketFactory sf = null;
	       try {
	           sf = new MailSSLSocketFactory();
	           sf.setTrustAllHosts(true);
	       } catch (GeneralSecurityException e1) {
	           e1.printStackTrace();
	       }
	       properties.put("mail.smtp.ssl.enable", "true");
	       properties.put("mail.smtp.ssl.socketFactory", sf);
	       // 构建授权信息，用于进行SMTP进行身份验证
	       Authenticator authenticator = new Authenticator() {
	           protected PasswordAuthentication getPasswordAuthentication() {
	               // 用户名、密码
	               String userName = properties.getProperty("mail.user");
	               String password = properties.getProperty("mail.password");
	               return new PasswordAuthentication(userName, password);
	           }
	       };
	       // 使用环境属性和授权信息，创建邮件会话
	       Session mailSession = Session.getInstance(properties, authenticator);
	       // 创建邮件消息
	       MimeMessage mailMessage = new MimeMessage(mailSession);
	       try {
	    	     // 设置发件人
				 mailMessage.setFrom(new InternetAddress(properties.get("mail.user").toString()));
				 // Message.RecipientType.TO属性表示接收者的类型为TO
		         mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(emailBean.getRecipient()));
		         // 设置邮件标题
		         mailMessage.setSubject(emailBean.getSubject(), "UTF-8");
		         // 设置发送时间
		         mailMessage.setSentDate(emailBean.getSendTime());
		         // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
		         Multipart mainPart = new MimeMultipart();
		         // 创建一个包含HTML内容的MimeBodyPart
		         BodyPart html = new MimeBodyPart();
		         html.setContent(emailBean.getContent().trim(), "text/html; charset=utf-8");
		         mainPart.addBodyPart(html);
		         // 设置邮件的内容体
		         mailMessage.setContent(mainPart);
		         //发送邮件
		         Transport.send(mailMessage);
			} catch (Exception e) {
				logger.error("发送邮件失败！");
				e.printStackTrace();
			}
	       
	   }
}
