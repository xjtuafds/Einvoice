package com.park.einvoice.common;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.park.einvoice.domain.vo.EmailInvoice;

public class MailTools {
	private static final String SERVER_NAME = "einvoice@51park.com.cn";// 邮箱账号名
	private static final String SERVER_PWD = "wytcEinvoice51=";// 邮箱密码
	private static final String SMTP = "smtp.exmail.qq.com"; // 设置发送邮件所用到的smtp
	private static final String title = "您收到一张新的电子发票";// 所发送邮件的标题
//	private static final String MAIL_CONTENT = "<font size='5'>请在附件中下载您的电子发票</font>";
	
	@SuppressWarnings("static-access")
	public static void sendMail(String email, String downloadUrl, EmailInvoice emailInvoice) {
		try {
			Properties props = new Properties();

			javax.mail.Session mailSession = null; // 邮件会话对象
			javax.mail.internet.MimeMessage mimeMsg = null; // MIME 邮件对象

			props = java.lang.System.getProperties(); // 获得系统属性对象
			props.put("mail.smtp.host", SMTP); // 设置SMTP主机
			props.put("mail.smtp.auth", "true"); // 是否到服务器用户名和密码验证
			// 到服务器验证发送的用户名和密码是否正确
			SmtpAuthenticator myEmailAuther = new SmtpAuthenticator(SERVER_NAME, SERVER_PWD);
			// 设置邮件会话 注意这里将认证信息放进了Session的创建参数里
			mailSession = javax.mail.Session.getInstance(props, (Authenticator) myEmailAuther);
			// 设置传输协议
			javax.mail.Transport transport = mailSession.getTransport("smtp");
			// 设置from、to等信息
			mimeMsg = new javax.mail.internet.MimeMessage(mailSession);
			if (null != SERVER_NAME && !"".equals(SERVER_NAME)) {
				InternetAddress sentFrom = new InternetAddress(SERVER_NAME);
				mimeMsg.setFrom(sentFrom); // 设置发送人地址
			}

			InternetAddress sendTo = new InternetAddress(email);

			mimeMsg.setRecipient(javax.mail.internet.MimeMessage.RecipientType.TO, sendTo);
			mimeMsg.setSubject(title + "[发票号码：" + emailInvoice.getInvoiceNo() + "]", "gb2312");

			MimeBodyPart messageBodyPart = new MimeBodyPart();
			// messageBodyPart.setText(UnicodeToChinese(text));
			messageBodyPart.setContent(
					"<table style='box-sizing:border-box;border-collapse:separate!important;mso-table-lspace:0;mso-table-rspace:0;width:100%;background-color:#f6f6f6' width='100%' bgcolor='#f6f6f6'>                                                                                      "
					+"<tbody>                                                                                                                                                                                                                                                                "
					+"<tr>                                                                                                                                                                                                                                                                   "
					+"	<td style='box-sizing:border-box;font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-size:14px;vertical-align:top;display:block;Margin:0 auto!important;max-width:580px;padding:10px;width:580px' width='580' valign='top'>            "
					+"		<div style='box-sizing:border-box;display:block;margin:0 auto;max-width:580px;padding:10px'>                                                                                                                                                                     "
					+"			<table style='box-sizing:border-box;border-collapse:separate!important;mso-table-lspace:0;mso-table-rspace:0;width:100%;background:#fff;border:1px solid #e9e9e9;border-radius:3px' width='100%'>                                                            "
					+"			<tbody>                                                                                                                                                                                                                                                      "
					+"			<tr>                                                                                                                                                                                                                                                         "
					+"				<td style='box-sizing:border-box;font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-size:14px;vertical-align:top;padding:30px' valign='top'>                                                                              "
					+"					<table style='box-sizing:border-box;border-collapse:separate!important;mso-table-lspace:0;mso-table-rspace:0;width:100%' width='100%'>                                                                                                               "
					+"					<tbody>                                                                                                                                                                                                                                              "
					+"					<tr>                                                                                                                                                                                                                                                 "
					+"						<td style='box-sizing:border-box;font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-size:14px;vertical-align:top' valign='top'>                                                                                   "
					+"							<h1 style='color:#111!important;font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-weight:300;line-height:1.4em;margin:0;margin-bottom:30px;font-size:38px;text-transform:capitalize;text-align:center'>电子发票信息</h1>"
					+"							<p style='font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-size:14px;font-weight:400;margin:0;margin-bottom:15px'>                                                                                          "
					+"								尊敬的车主：                                                                                                                                                                                                                             "
					+"							</p>                                                                                                                                                                                                                                         "
					+"							<p style='font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-size:14px;font-weight:400;margin:0;margin-bottom:15px'>                                                                                          "
					+"								您好，感谢光临无忧停车！                                                                                                                                                                                                                 "
					+"							</p>                                                                                                                                                                                                                                         "
					+"							<p style='font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-size:14px;font-weight:400;margin:0;margin-bottom:15px'>                                                                                          "
					+"								无忧停车为您送达已经成功开具的发票，请查收附件。                                                                                                                                                                                         "
					+"							</p>                                                                                                                                                                                                                                         "
					+"							<h2 style='color:#111!important;font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-weight:400;line-height:1.4em;margin:0;margin-bottom:18px;font-size:24px'>发票代码</h2>                                     "
					+"							<p style='font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-size:14px;font-weight:400;margin:0;margin-bottom:15px'>                                                                                          "
					+"								代码编号：<span style='box-sizing:border-box;color:#348eda'>" + emailInvoice.getInvoiceCode() + "</span>                                                                                                                                                      "
					+"							</p>                                                                                                                                                                                                                                         "
					+"							<h2 style='color:#111!important;font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-weight:400;line-height:1.4em;margin:0;margin-bottom:18px;font-size:24px'>发票号码</h2>                                     "
					+"							<p style='font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-size:14px;font-weight:400;margin:0;margin-bottom:15px'>                                                                                          "
					+"								号码编号：<span style='box-sizing:border-box;color:#348eda'>" + emailInvoice.getInvoiceNo() + "</span>                                                                                                                                                        "
					+"							</p>                                                                                                                                                                                                                                         "
					+"							<h2 style='color:#111!important;font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-weight:400;line-height:1.4em;margin:0;margin-bottom:18px;font-size:24px'>开票日期</h2>                                     "
					+"							<p style='font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-size:14px;font-weight:400;margin:0;margin-bottom:15px'>                                                                                          "
					+"								" + DateTools.getFormat(DateTools.DF_DTM, emailInvoice.getInvoiceDate()) + "                                                                                                                                                                                                                     "
					+"							</p>                                                                                                                                                                                                                                         "
					+"							<h2 style='color:#111!important;font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-weight:400;line-height:1.4em;margin:0;margin-bottom:18px;font-size:24px'>发票抬头</h2>                                     "
					+"							<p style='font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-size:14px;font-weight:400;margin:0;margin-bottom:15px'>                                                                                          "
					+"								" + emailInvoice.getBuyerName() + "                                                                                                                                                                                                "
					+"							</p>                                                                                                                                                                                                                                         "
					+"							<h2 style='color:#111!important;font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-weight:400;line-height:1.4em;margin:0;margin-bottom:18px;font-size:24px'>开票金额</h2>                                     "
					+"							<p style='font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-size:14px;font-weight:400;margin:0;margin-bottom:15px'>                                                                                          "
					+"								金额：<span style='box-sizing:border-box;color:#ff0000; font-size:20px;'>￥" + emailInvoice.getInvoiceAmount().toString() + "</span>                                                                                                                                                     "
					+"							</p>                                                                                                                                                                                                                                         "
					+"							<table style='box-sizing:border-box;border-collapse:separate!important;mso-table-lspace:0;mso-table-rspace:0;width:100%' width='100%'>                                                                                                       "
					+"							<tbody>                                                                                                                                                                                                                                      "
					+"							<tr>                                                                                                                                                                                                                                         "
					+"								<td style='box-sizing:border-box;font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-size:14px;vertical-align:top;padding:20px 0' valign='top'>                                                            "
					+"									<table cellpadding='0' cellspacing='0' style='box-sizing:border-box;border-collapse:separate!important;mso-table-lspace:0;mso-table-rspace:0;width:100%' width='100%'>                                                               "
					+"									<tbody>                                                                                                                                                                                                                              "
					+"									<tr>                                                                                                                                                                                                                                 "
					+"										<td style='box-sizing:border-box;font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;vertical-align:top;font-size:0;border-top:1px solid #ccc;line-height:0;height:1px;margin:0;padding:0' valign='top'>"
					+"										</td>                                                                                                                                                                                                                            "
					+"									</tr>                                                                                                                                                                                                                                "
					+"									</tbody>                                                                                                                                                                                                                             "
					+"									</table>                                                                                                                                                                                                                             "
					+"								</td>                                                                                                                                                                                                                                    "
					+"							</tr>                                                                                                                                                                                                                                        "
					+"							</tbody>                                                                                                                                                                                                                                     "
					+"							</table>                                                                                                                                                                                                                                     "
					+"							<h2 style='color:#111!important;font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-weight:400;line-height:1.4em;margin:0;margin-bottom:30px;font-size:24px'>温馨提示</h2>                                     "
					+"							<p style='font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-size:14px;font-weight:400;margin:0;margin-bottom:10px'>                                                                                          "
					+"							</p>                                                                                                                                                                                                                                         "
					+"							<ul style='font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-size:14px;font-weight:400;margin:0;margin-bottom:15px'>                                                                                         "
					+"								<li style='list-style-position:inside;margin-left:0px; padding-left:0px'>如有需要，可以打印本电子发票。</li>                                                                                                                             "
					+"							</ul>                                                                                                                                                                                                                                        "
					+"							<p>                                                                                                                                                                                                                                          "
					+"							</p>                                                                                                                                                                                                                                         "
					+"							<table style='box-sizing:border-box;border-collapse:separate!important;mso-table-lspace:0;mso-table-rspace:0;width:100%' width='100%'>                                                                                                       "
					+"							<tbody>                                                                                                                                                                                                                                      "
					+"							<tr>                                                                                                                                                                                                                                         "
					+"								<td style='box-sizing:border-box;font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-size:14px;vertical-align:top;padding:20px 0' valign='top'>                                                            "
					+"									<table cellpadding='0' cellspacing='0' style='box-sizing:border-box;border-collapse:separate!important;mso-table-lspace:0;mso-table-rspace:0;width:100%' width='100%'>                                                               "
					+"									<tbody>                                                                                                                                                                                                                              "
					+"									<tr>                                                                                                                                                                                                                                 "
					+"										<td style='box-sizing:border-box;font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;vertical-align:top;font-size:0;border-top:1px solid #ccc;line-height:0;height:1px;margin:0;padding:0' valign='top'>"
					+"										</td>                                                                                                                                                                                                                            "
					+"									</tr>                                                                                                                                                                                                                                "
					+"									</tbody>                                                                                                                                                                                                                             "
					+"									</table>                                                                                                                                                                                                                             "
					+"								</td>                                                                                                                                                                                                                                    "
					+"							</tr>                                                                                                                                                                                                                                        "
					+"							</tbody>                                                                                                                                                                                                                                     "
					+"							</table>                                                                                                                                                                                                                                     "
					+"							<p style='font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-size:14px;font-weight:400;margin:0;margin-bottom:15px'>                                                                                          "
					+"								关注无忧停车微博：<a href='http://weibo.com/wuyoutingche' style='box-sizing:border-box;color:#348eda;text-decoration:underline' target='_blank'>无忧停车</a>                                                                             "
					+"							</p>                                                                                                                                                                                                                                         "
					+"							<p style='font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-size:14px;font-weight:400;margin:0;margin-bottom:15px'>                                                                                          "
					+"								关注无忧停车微信号：<span style='box-sizing:border-box;color:#348eda'>P51park</span>                                                                                                                                                     "
					+"							</p>                                                                                                                                                                                                                                         "
					+"							<p style='font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-size:14px;font-weight:400;margin:0;margin-bottom:15px'>                                                                                          "
					+"								邮箱: <a href='mailto:einvoice@51park.com.cn' style='box-sizing:border-box;color:#348eda;text-decoration:underline' target='_blank'>einvoice@51park.com.cn</a>                                                                           "
					+"							</p>                                                                                                                                                                                                                                         "
					+"							<p style='font-family:\"Microsoft YaHei\",Helvetica,Arial,\"Lucida Grande\",sans-serif;font-size:14px;font-weight:400;margin:0;margin-bottom:10px'>                                                                                          "
					+"								北京百会易泊科技有限公司                                                                                                                                                                                                                 "
					+"							</p>                                                                                                                                                                                                                                         "
					+"						</td>                                                                                                                                                                                                                                            "
					+"					</tr>                                                                                                                                                                                                                                                "
					+"					</tbody>                                                                                                                                                                                                                                             "
					+"					</table>                                                                                                                                                                                                                                             "
					+"				</td>                                                                                                                                                                                                                                                    "
					+"			</tr>                                                                                                                                                                                                                                                        "
					+"			</tbody>                                                                                                                                                                                                                                                     "
					+"			</table>                                                                                                                                                                                                                                                     "
					+"		</div>                                                                                                                                                                                                                                                           "
					+"	</td>                                                                                                                                                                                                                                                                "
					+"</tr>                                                                                                                                                                                                                                                                  "
					+"</tbody>                                                                                                                                                                                                                                                               "
					+"</table>                                                                                                                                                                                                                                                               "
					/*"[本邮件为系统自动发送，请勿直接回复]"
					+"		尊敬的车主,您好!                                  "
					+"		感谢光临无忧停车！无忧停车为您送达已经成功开具的发票，请查收附件。    "
					+"		发票代码：" + emailInvoice.getInvoiceCode()
					+"		发票号码：" + emailInvoice.getInvoiceNo()
					+"		开票日期：" + DateTools.getFormat(DateTools.DF_DTM, emailInvoice.getInvoiceDate())
					+"		发票抬头：" + emailInvoice.getBuyerName()
					+"		开票金额：￥" + emailInvoice.getInvoiceAmount().toString()
					+"		如有需要，可以打印本电子发票。"*/
					, "text/html;charset=gb2312"
			);
			Multipart multipart = new MimeMultipart(); 
			// 附件传输格式 
			MimeBodyPart messageBodyPartMulti = new MimeBodyPart(); 
			String displayname = "电子发票" + emailInvoice.getInvoiceNo() + ".pdf";
			// 得到数据源 
			FileDataSource fds = new FileDataSource(downloadUrl); 
			// BodyPart添加附件本身 
			messageBodyPartMulti.setDataHandler(new DataHandler(fds)); 
			// BodyPart添加附件文件名 
//			messageBodyPartMulti.setFileName(new String(displayname.getBytes(), "gb2312"));
			messageBodyPartMulti.setFileName(MimeUtility.encodeText(displayname));
			multipart.addBodyPart(messageBodyPartMulti);
			multipart.addBodyPart(messageBodyPart);
			
			mimeMsg.setContent(multipart);
			// 设置信件头的发送日期
			mimeMsg.setSentDate(new Date());
			mimeMsg.saveChanges();
			// 发送邮件
			transport.send(mimeMsg);
			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

class SmtpAuthenticator extends Authenticator {
	String username = null;
	String password = null;

	public SmtpAuthenticator(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.username, this.password);
	}
}