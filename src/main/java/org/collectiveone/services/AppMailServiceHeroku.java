package org.collectiveone.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

@Service("mailService")
public class AppMailServiceHeroku extends BaseService {

	public void sendMail(String to, String subject, String body) throws IOException {
		List<String> tos = new ArrayList<String>();
		tos.add(to);
		sendMail(tos, subject, body);
	}
	
	void sendMail(List<String> tos, String subject, String body) throws IOException {
		if(env.getProperty("collectiveone.webapp.send-email-enabled").equalsIgnoreCase("true")) {
			if(tos.size() > 0) {
				String key = System.getenv("SENDGRID_API_KEY");
				SendGrid sg = new SendGrid(key);
				sg.addRequestHeader("X-Mock", "true");
	
				Request request = new Request();
				Mail mail = prepareMail(tos, subject, body);
				
				try {
					request.method = Method.POST;
					request.endpoint = "mail/send";
					request.body = mail.build();
					Response response = sg.api(request);
					System.out.println(response.statusCode);
					System.out.println(response.body);
					System.out.println(response.headers);
				} catch (IOException ex) {
					throw ex;
				}
			}
		}
	}

	private Mail prepareMail(List<String> tos, String subject, String body) 
	{
		Mail mail = new Mail();

		Email fromEmail = new Email();
		fromEmail.setName(env.getProperty("collectiveone.webapp.from-mail-name"));
		fromEmail.setEmail(env.getProperty("collectiveone.webapp.from-mail"));
		mail.setFrom(fromEmail);
		mail.setSubject(subject);

		for(String to : tos) {
			Email toEmail = new Email();
			Personalization personalization = new Personalization();
			toEmail.setEmail(to);
			personalization.addTo(toEmail);
			mail.addPersonalization(personalization);
		}
		
		Content content = new Content();
		content.setType("text/plain");
		content.setValue("some text here");

		mail.addContent(content);
		content.setType("text/html");
		content.setValue("<html><body>"+body+"</body></html>");
		mail.addContent(content);

		return mail;

	}



}
