package org.collectiveone.modules.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.collectiveone.modules.users.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

@Service
public class EmailService {
	
	@Autowired
	protected Environment env;

	List<List<Notification>> segmentedNotifications = new ArrayList<List<Notification>>();
	
	public String sendNotifications(List<Notification> notifications) {
		
		/* being global, it is kept in memory as attribute of this component */
		segmentedNotifications.clear();
		
		/* segment all notifications into subarrays of those of the same 
		 * activity type */
		for (Notification notification : notifications) {
			int ix = indexOfType(notification.getActivity().getType());
			if (ix == -1) {
				List<Notification> newArray = new ArrayList<Notification>();
				newArray.add(notification);
				segmentedNotifications.add(newArray);
			} else {
				segmentedNotifications.get(ix).add(notification);
			}
		}
		
		for (List<Notification> theseNotifications : segmentedNotifications) {
			sendSegmentedNotifications(theseNotifications);
		}
		
		return "success";
	}
	
	private int indexOfType(ActivityType type) {
		for (int ix = 0; ix < segmentedNotifications.size(); ix++) {
			if (segmentedNotifications.get(ix).get(0).getActivity().getType().equals(type)) {
				return ix; 
			}
		}
		return -1;
	}
	
	private void sendSegmentedNotifications(List<Notification> notifications) throws IOException {
		if(env.getProperty("collectiveone.webapp.send-email-enabled").equalsIgnoreCase("true")) {
			if(notifications.size() > 0) {
				
				String key = System.getenv("SENDGRID_API_KEY");
				SendGrid sg = new SendGrid(key);
				sg.addRequestHeader("X-Mock", "true");
	
				Request request = new Request();
				Mail mail = null;
						
				ActivityType type = notifications.get(0).getActivity().getType();
				
				switch (type) {
				case SUBINITIATIVE_CREATED:
					mail = prepareSubinitaitiveCreatedEmail(notifications);
					break;
				}
				
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

	private Mail prepareSubinitaitiveCreatedEmail(List<Notification> notifications, String subject, String body) 
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
			personalization.addSubstitution(env.getProperty("collectiveone.webapp.email-template.username_holder"), );
			mail.addPersonalization(personalization);
		}
		
		mail.setTemplateId("aaa5565e-d53e-42e8-a11c-0e6ffb6757ee");
				
		return mail;

	}



}
