package org.collectiveone.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
public class AppMailServiceHeroku {

	@Autowired
	private Environment env;

	public void sendMail(String to, String subject, String body) throws IOException {
		SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
		sg.addRequestHeader("X-Mock", "true");

		Request request = new Request();
		Mail mail = prepareMail(to, subject, body);
		
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

	public Mail prepareMail(String to, String subject, String body) 
	{
		Mail mail = new Mail();

		Email fromEmail = new Email();
		fromEmail.setName(env.getProperty("collectiveone.webapp.from-mail-name"));
		fromEmail.setEmail(env.getProperty("collectiveone.webapp.from-mail"));
		mail.setFrom(fromEmail);
		mail.setSubject(subject);

		Personalization personalization = new Personalization();
		Email toEmail = new Email();
		toEmail.setEmail(to);
		personalization.addTo(toEmail);
		mail.addPersonalization(personalization);

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
