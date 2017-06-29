package org.collectiveone.modules.activity;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.tokens.InitiativeTransfer;
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
	private SendGrid sg;
	
	@Autowired
	protected Environment env;

	List<List<Notification>> segmentedNotifications = new ArrayList<List<Notification>>();
	
	public String sendNotifications(List<Notification> notifications) throws IOException {
		
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

	private Mail prepareSubinitaitiveCreatedEmail(List<Notification> notifications) 
	{
		Mail mail = new Mail();
		
		Email fromEmail = new Email();
		fromEmail.setName(env.getProperty("collectiveone.webapp.from-mail-name"));
		fromEmail.setEmail(env.getProperty("collectiveone.webapp.from-mail"));
		mail.setFrom(fromEmail);
		mail.setSubject("Recent activity in CollectiveOne");
	
		for(Notification notification : notifications) {
			if(notification.getSubscriber().getUser().getEmailNotificationsEnabled()) {
				String toEmailString = notification.getSubscriber().getUser().getEmail();
				String triggeredByUsername = notification.getActivity().getSubInitiative().getCreator().getNickname();
				Initiative initiative = notification.getActivity().getInitiative();
				Initiative subInitiative = notification.getActivity().getSubInitiative();
				String transferredAssets = getTransferString(notification.getActivity().getInitiativeTransfers());
				
				Personalization personalization = new Personalization();
				
				Email toEmail = new Email();
				toEmail.setEmail(toEmailString);
				
				personalization.addTo(toEmail);
				
				personalization.addSubstitution("$INITIATIVE_NAME$", initiative.getName());
				personalization.addSubstitution("$TRIGGER_USER_NICKNAME$", triggeredByUsername);
				personalization.addSubstitution("$INITIATIVE_ANCHOR$", getInitiativeAnchor(initiative));
				personalization.addSubstitution("$SUBINITIATIVE_ANCHOR$", getInitiativeAnchor(subInitiative));
				personalization.addSubstitution("$TRANSFERRED_ASSETS$", transferredAssets);
				
				personalization.addSubstitution("$UNSUSCRIBE_FROM_INITIATIVE_ANCHOR$", transferredAssets);
				personalization.addSubstitution("$UNSUSCRIBE_FROM_COLLECTIVEONE_NOTIFICATIONS$", transferredAssets);
				
				mail.addPersonalization(personalization);
			}
		}
		
		mail.setTemplateId(env.getProperty("collectiveone.webapp.new-subinitiative-template"));
		
		return mail;

	}
	
	private String getInitiativeAnchor(Initiative initiative) {
		return "<a href=" + env.getProperty("collectiveone.webapp.baseurl") +"/#/inits/" + initiative.getId().toString() + "/overview>" + initiative.getName() + "</a>";
	}
	
	private String getTransferString(List<InitiativeTransfer> transfers) {
		return NumberFormat.getNumberInstance(Locale.US).format(transfers.get(0).getValue()) + " " + transfers.get(0).getTokenType().getName();
	}
	
	



}
