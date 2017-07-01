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
					
				case INITIATIVE_EDITED:
					mail = prepareInitiativeEditedEmail(notifications);
					break;
				}
				
				if (mail != null) {
					try {
						request.method = Method.POST;
						request.endpoint = "mail/send";
						request.body = mail.build();
						
						sg.api(request);
						
					} catch (IOException ex) {
						throw ex;
					}
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
		mail.setSubject("Recent activity - new subinitiative created");
	
		for(Notification notification : notifications) {
			if(notification.getSubscriber().getUser().getEmailNotificationsEnabled()) {
				Personalization personalization = basicInitiativePersonalization(notification);
				
				Initiative subInitiative = notification.getActivity().getSubInitiative();
				String transferredAssets = getTransferString(notification.getActivity().getInitiativeTransfers());
				
				String message = "Created the " + getInitiativeAnchor(subInitiative) + " sub-initiative and transferred " + transferredAssets + " to it.";
				personalization.addSubstitution("$MESSAGE$", message);
				
				mail.addPersonalization(personalization);
			}
		}
		
		mail.setTemplateId(env.getProperty("collectiveone.webapp.new-subinitiative-template"));
		
		return mail;

	}
	
	private Mail prepareInitiativeEditedEmail(List<Notification> notifications) {
		Mail mail = new Mail();
		
		Email fromEmail = new Email();
		fromEmail.setName(env.getProperty("collectiveone.webapp.from-mail-name"));
		fromEmail.setEmail(env.getProperty("collectiveone.webapp.from-mail"));
		mail.setFrom(fromEmail);
		mail.setSubject("Recent activity - initiative edited");
	
		for(Notification notification : notifications) {
			if(notification.getSubscriber().getUser().getEmailNotificationsEnabled()) {
				Personalization personalization = basicInitiativePersonalization(notification);
				
				Initiative initiative = notification.getActivity().getInitiative();
				String oldName = notification.getActivity().getOldName();
				String oldDriver = notification.getActivity().getOldDriver();
				
				String message = "";
				boolean changedName = false;
				boolean changedDriver = false;
				
				if(!initiative.getName().equals(oldName)) {
					changedName = true;
				}
				
				if(!initiative.getDriver().equals(oldDriver)) {
					changedDriver = true;
				}
				
				if(changedName && changedDriver) {
					message = "Changed the initiative name from " + oldName + " to " + initiative.getName() + 
							", and the driver from <br /><br /><i>" + oldDriver + "</i><br /><br /> to <br /><br />" + initiative.getDriver();
				} else if (changedName) {
					message = "Changed the initiative name from " + oldName + " to " + initiative.getName();
				} else if (changedDriver) {
					message = "Changed the initiative driver from <br /><br /><i>" + oldDriver + "</i><br /><br /> to <br /><br /><i>" + initiative.getDriver() + "</i>";
				} else {
					message = "";
				}
				
				personalization.addSubstitution("$MESSAGE$", message);
				
				mail.addPersonalization(personalization);
			}
		}
		
		mail.setTemplateId(env.getProperty("collectiveone.webapp.new-subinitiative-template"));
		
		return mail;

	}
	
	private Personalization basicInitiativePersonalization(Notification notification) {
		String toEmailString = notification.getSubscriber().getUser().getEmail();
		String triggeredByUsername = notification.getActivity().getTriggerUser().getNickname();
		String triggerUserPictureUrl = notification.getActivity().getTriggerUser().getPictureUrl();
		Initiative initiative = notification.getActivity().getInitiative();
		
		Personalization personalization = new Personalization();
		
		Email toEmail = new Email();
		toEmail.setEmail(toEmailString);
		
		personalization.addTo(toEmail);
		personalization.addSubstitution("$INITIATIVE_NAME$", initiative.getName());
		personalization.addSubstitution("$TRIGGER_USER_NICKNAME$", triggeredByUsername);
		personalization.addSubstitution("$TRIGGER_USER_PICTURE$", triggerUserPictureUrl);
		personalization.addSubstitution("$INITIATIVE_ANCHOR$", getInitiativeAnchor(initiative));
		personalization.addSubstitution("$INITIATIVE_PICTURE$", "http://guillaumeladvie.com/wp-content/uploads/2014/04/ouishare.jpg");
		
		personalization.addSubstitution("$UNSUSCRIBE_FROM_INITIATIVE_HREF$", getUnsuscribeFromInitiativeHref(initiative));
		personalization.addSubstitution("$UNSUSCRIBE_FROM_ALL_HREF$", getUnsuscribeFromAllHref());
		
		return personalization;
	}
	
	private String getInitiativeAnchor(Initiative initiative) {
		return "<a href=" + env.getProperty("collectiveone.webapp.baseurl") +"/#/inits/" + initiative.getId().toString() + "/overview>" + initiative.getName() + "</a>";
	}
	
	private String getTransferString(List<InitiativeTransfer> transfers) {
		return NumberFormat.getNumberInstance(Locale.US).format(transfers.get(0).getValue()) + " " + transfers.get(0).getTokenType().getName();
	}
	
	private String getUnsuscribeFromInitiativeHref(Initiative initiative) {
		return env.getProperty("collectiveone.webapp.baseurl") +"/#/unsubscribeFromInitiative/" + initiative.getId().toString();
	}
	
	private String getUnsuscribeFromAllHref() {
		return env.getProperty("collectiveone.webapp.baseurl") +"/#/unsubscribeFromAll";
	}


}
