package org.collectiveone.modules.activity;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.collectiveone.modules.activity.dto.NotificationDto;
import org.collectiveone.modules.activity.enums.ActivityType;
import org.collectiveone.modules.activity.enums.NotificationState;
import org.collectiveone.modules.activity.repositories.NotificationRepositoryIf;
import org.collectiveone.modules.activity.repositories.WantToContributeRepositoryIf;
import org.collectiveone.modules.initiatives.Initiative;
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
	private NotificationRepositoryIf notificationRepository;
	
	@Autowired
	private NotificationDtoBuilder notificationDtoBuilder;
	
	@Autowired
	private WantToContributeRepositoryIf wantToContributeRepository;
	
	@Autowired
	private SendGrid sg;
	
	@Autowired
	protected Environment env;

	List<List<Notification>> segmentedPerActivityNotifications = new ArrayList<List<Notification>>();
	List<List<Notification>> segmentedPerUserNotifications = new ArrayList<List<Notification>>();
	List<List<Notification>> segmentedPerUserAndInitiativeNotifications = new ArrayList<List<Notification>>();
	
	public String sendNotificationsGrouped(List<Notification> notifications) throws IOException {
		/* group notifications by subscriber and send one email to each */
		segmentedPerUserNotifications.clear();
		for (Notification notification : notifications) {
			int ix = indexOfUser(notification.getSubscriber().getUser().getC1Id());
			if (ix == -1) {
				List<Notification> newArray = new ArrayList<Notification>();
				newArray.add(notification);
				segmentedPerUserNotifications.add(newArray);
			} else {
				segmentedPerUserNotifications.get(ix).add(notification);
			}
		}
		
		String result = "success";
		
		for (List<Notification> theseNotifications : segmentedPerUserNotifications) {
			
			segmentedPerUserAndInitiativeNotifications.clear();
			
			for (Notification notification : theseNotifications) {
				// TODO: notifications on sections and cards are not associated to one initiative
				if (notification.getActivity().getInitiative() != null) {
					int ix = indexOfInitiative(notification.getActivity().getInitiative().getId());
					if (ix == -1) {
						List<Notification> newArray = new ArrayList<Notification>();
						newArray.add(notification);
						segmentedPerUserAndInitiativeNotifications.add(newArray);
					} else {
						segmentedPerUserAndInitiativeNotifications.get(ix).add(notification);
					}	
				}
			}
			
			String subresult = "";
			try {
				subresult = sendSegmentedPerUserAndInitiativeNotifications(
						theseNotifications,
						theseNotifications.get(0).getSubscriber().getUser(),
						theseNotifications.get(0).getActivity().getInitiative());
				
			} catch (Exception ex) {
				subresult = "error sending emails";
				
				for (Notification notification : theseNotifications) {
					/* protection to prevent spamming */
					notification.setEmailNowState(NotificationState.DELIVERED);
					notification.setEmailSummaryState(NotificationState.DELIVERED);
					notificationRepository.save(notification);
				}
			}
			
			
			if (!subresult.equals("success")) {
				result = "error sending notifications";
			}
		}
		
		return result;
	}
	
	public String sendNotificationsSendNow(List<Notification> notifications) throws IOException {
		
		if (notifications.size() > 0) {
			/* being global, it is kept in memory as attribute of this component */
			segmentedPerActivityNotifications.clear();
			
			/* segment all notifications into subarrays of those of the same 
			 * activity type, one email with multiple personalizations per activity type */
			for (Notification notification : notifications) {
				int ix = indexOfType(notification.getActivity().getType());
				if (ix == -1) {
					List<Notification> newArray = new ArrayList<Notification>();
					newArray.add(notification);
					segmentedPerActivityNotifications.add(newArray);
				} else {
					segmentedPerActivityNotifications.get(ix).add(notification);
				}
			}
			
			String result = "success";
			
			for (List<Notification> theseNotifications : segmentedPerActivityNotifications) {
				
				String subresult = ""; 
				try {
					subresult = sendSegmentedPerActivityNotifications(theseNotifications);
				} catch (Exception ex) {
					subresult = "error sending emails";
					
					for (Notification notification : theseNotifications) {
						/* protection to prevent spamming */
						notification.setEmailNowState(NotificationState.DELIVERED);
						notification.setEmailSummaryState(NotificationState.DELIVERED);
						notificationRepository.save(notification);
					}
				}
				
				if (!subresult.equals("success")) {
					result = "error sending notifications";
				}
			}
			
			String subresult = "";
			
			if (!subresult.equals("success")) {
				result = "error sending notifications";
			}
			
			return result;
		}
		
		return "empty";
	}
	
	private int indexOfType(ActivityType type) {
		for (int ix = 0; ix < segmentedPerActivityNotifications.size(); ix++) {
			if (segmentedPerActivityNotifications.get(ix).get(0).getActivity().getType().equals(type)) {
				return ix; 
			}
		}
		return -1;
	}
	
	private int indexOfUser(UUID userId) {
		for (int ix = 0; ix < segmentedPerUserNotifications.size(); ix++) {
			if (segmentedPerUserNotifications.get(ix).get(0).getSubscriber().getUser().getC1Id().equals(userId)) {
				return ix; 
			}
		}
		return -1;
	}
	
	private int indexOfInitiative(UUID initiativeId) {
		for (int ix = 0; ix < segmentedPerUserAndInitiativeNotifications.size(); ix++) {
			if (segmentedPerUserAndInitiativeNotifications.get(ix).get(0).getActivity().getInitiative().getId().equals(initiativeId)) {
				return ix; 
			}
		}
		return -1;
	}
	
	private String sendSegmentedPerUserAndInitiativeNotifications(List<Notification> notifications, AppUser receiver, Initiative initiative) throws IOException {
		if(env.getProperty("collectiveone.webapp.send-email-enabled").equalsIgnoreCase("true")) {
			if(notifications.size() > 0 && receiver.getEmailNotificationsEnabled()) {
				Request request = new Request();
				Mail mail = new Mail();
				
				Email fromEmail = new Email();
				fromEmail.setName(env.getProperty("collectiveone.webapp.from-mail-name"));
				fromEmail.setEmail(env.getProperty("collectiveone.webapp.from-mail"));
				mail.setFrom(fromEmail);
				mail.setSubject("Recent activity in your initiatives");
				
				Personalization personalization = new Personalization();
				
				Email toEmail = new Email();
				toEmail.setEmail(receiver.getEmail());
				
				personalization.addTo(toEmail);
				personalization.addSubstitution("$INITIATIVE_NAME$", initiative.getMeta().getName());
				personalization.addSubstitution("$INITIATIVE_ANCHOR$", getInitiativeAnchor(initiative));
				
				personalization.addSubstitution("$UNSUSCRIBE_FROM_ALL_HREF$", getUnsuscribeFromAllHref());
				
				mail.addPersonalization(personalization);
				
				String rows = "";
				for (Notification notification : notifications) {
					NotificationDto notificationDto = notificationDtoBuilder.getNotificationDto(notification, true);
					rows += 
						"<tr>"
					+		"<td class=\"avatar-box\">"
					+			"<img class=\"avatar-img\" src=\"" + notification.getActivity().getTriggerUser().getProfile().getPictureUrl() + "\"></img>"
					+		"</td>"
					+		"<td class=\"time-box\">"
					+			getTimeSinceStr(notification.getActivity().getTimestamp())
					+		"</td>"
					+		"<td class=\"activity-text\">"
					+			notificationDto.getMessage()
					+		"</td>"
					+	"</tr>";
				}
				
				personalization.addSubstitution("$ROWS$", rows);
				
				mail.setTemplateId(env.getProperty("collectiveone.webapp.initiative-and-user-activity-template"));
								
				try {
					request.method = Method.POST;
					request.endpoint = "mail/send";
					request.body = mail.build();
					
					Response response = sg.api(request);
					
					if(response.statusCode == 202) {
						System.out.println("emails sent!");
						
						for (Notification notification : notifications) {
							notification.setEmailNowState(NotificationState.DELIVERED);
							notification.setEmailSummaryState(NotificationState.DELIVERED);
							notificationRepository.save(notification);
						}
						
						return "success";
					} else {
						return response.body;
					}
					
				} catch (IOException ex) {
					throw ex;
				}
			
			}
		}
		return "success";
	}
	
	private String getTimeSinceStr(Timestamp since) {
		Map<TimeUnit,Long> diff = computeDiff(new Date(since.getTime()) , new Date());
		
		if (diff.get(TimeUnit.DAYS) > 0) {
			return diff.get(TimeUnit.DAYS) + " days ago";
		} else {
			if (diff.get(TimeUnit.HOURS) > 0) {
				return diff.get(TimeUnit.HOURS) + " hours ago";
			} else {
				return diff.get(TimeUnit.MINUTES) + " minutes ago";
			}
		}
	}
	
	private static Map<TimeUnit,Long> computeDiff(Date date1, Date date2) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
	    Collections.reverse(units);
	    Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
	    long milliesRest = diffInMillies;
	    for ( TimeUnit unit : units ) {
	        long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
	        long diffInMilliesForUnit = unit.toMillis(diff);
	        milliesRest = milliesRest - diffInMilliesForUnit;
	        result.put(unit,diff);
	    }
	    return result;
	}
	
	public String sendWantToContributeNotifications(List<WantToContributeNotification> notifications) throws IOException {
		if(env.getProperty("collectiveone.webapp.send-email-enabled").equalsIgnoreCase("true")) {
			if(notifications.size() > 0) {
				Request request = new Request();
				Mail mail = prepareWantToContributeEmail(notifications);
				
				if (mail != null) {
					try {
						request.method = Method.POST;
						request.endpoint = "mail/send";
						request.body = mail.build();
						
						Response response = sg.api(request);
						
						if(response.statusCode == 202) {
							System.out.println("emails sent!");
							return "success";
						} else {
							return response.body;
						}
						
					} catch (IOException ex) {
						throw ex;
					}
				} else {
					return "error bulding email";
				}
			}
		}
		
		/* if email is disabled */
		return "success";
	}
	
	private String sendSegmentedPerActivityNotifications(List<Notification> notifications) throws IOException {
		if(env.getProperty("collectiveone.webapp.send-email-enabled").equalsIgnoreCase("true")) {
			if(notifications.size() > 0) {
				
				Request request = new Request();
				Mail mail = prepareActivitySendNowEmail(notifications);
				
				if (mail != null) {
					try {
						request.method = Method.POST;
						request.endpoint = "mail/send";
						request.body = mail.build();
						
						Response response = sg.api(request);
						
						if(response.statusCode == 202) {
							System.out.println("emails sent!");
							return "success";
						} else {
							return response.body;
						}
						
					} catch (IOException ex) {
						throw ex;
					}
				} else {
					return "error bulding email";
				}
			}
		}
		
		/* if email is disabled */
		return "success";
	}
	
	private Mail prepareWantToContributeEmail(List<WantToContributeNotification> notifications) {
		Mail mail = new Mail();
		
		Email fromEmail = new Email();
		fromEmail.setName(env.getProperty("collectiveone.webapp.from-mail-name"));
		fromEmail.setEmail(env.getProperty("collectiveone.webapp.from-mail"));
		mail.setFrom(fromEmail);
		mail.setSubject("Request to contribute");
	
		for(WantToContributeNotification notification : notifications) {
			
			if (notification.getAdmin().getEmailNotificationsEnabled()) {
				String toEmailString = notification.getAdmin().getEmail();
				Initiative initiative = notification.getInitiative();
				String acceptRequestUrl = env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
						initiative.getId().toString() + "/people/addMember/" + notification.getUser().getC1Id().toString();
				
				
				Personalization personalization = new Personalization();
				
				Email toEmail = new Email();
				toEmail.setEmail(toEmailString);
				
				personalization.addTo(toEmail);
				personalization.addSubstitution("$INITIATIVE_ANCHOR$", getInitiativeAnchor(initiative));
				personalization.addSubstitution("$USER_NICKNAME$", notification.getUser().getProfile().getNickname());
				personalization.addSubstitution("$USER_PICTURE$", notification.getUser().getProfile().getPictureUrl());
				personalization.addSubstitution("$USER_EMAIL$", notification.getUser().getEmail());
				personalization.addSubstitution("$ACCEPT_AS_MEMBER_URL$", acceptRequestUrl);
							
				mail.addPersonalization(personalization);
			
				notification.setEmailState(NotificationState.DELIVERED);
				
				wantToContributeRepository.save(notification);
			}
			
		}
		
		mail.setTemplateId(env.getProperty("collectiveone.webapp.want-to-contribute-template"));
		
		return mail;
	}
	
	private Mail prepareActivitySendNowEmail(List<Notification> notifications) {
		Mail mail = new Mail();
		
		Email fromEmail = new Email();
		fromEmail.setName(env.getProperty("collectiveone.webapp.from-mail-name"));
		fromEmail.setEmail(env.getProperty("collectiveone.webapp.from-mail"));
		mail.setFrom(fromEmail);
		mail.setSubject("Activity in CollectiveOne");
	
		for(Notification notification : notifications) {
			if(notification.getSubscriber().getUser().getEmailNotificationsEnabled()) {
				Personalization personalization = basicInitiativePersonalization(notification);
				
				NotificationDto notificationDto = notificationDtoBuilder.getNotificationDto(notification, true);
				personalization.addSubstitution("$MESSAGE$", notificationDto.getMessage());
				
				mail.addPersonalization(personalization);
			} 
			
			notification.setEmailNowState(NotificationState.DELIVERED);
			notification.setEmailSummaryState(NotificationState.DELIVERED);
			
			notificationRepository.save(notification);
		}
		
		mail.setTemplateId(env.getProperty("collectiveone.webapp.initiative-activity-template"));
		
		return mail;
	}
	
	private Personalization basicInitiativePersonalization(Notification notification) {
		String toEmailString = notification.getSubscriber().getUser().getEmail();
		String triggeredByUsername = notification.getActivity().getTriggerUser().getProfile().getNickname();
		String triggerUserPictureUrl = notification.getActivity().getTriggerUser().getProfile().getPictureUrl();
		Initiative initiative = notification.getActivity().getInitiative();
		
		Personalization personalization = new Personalization();
		
		Email toEmail = new Email();
		toEmail.setEmail(toEmailString);
		
		personalization.addTo(toEmail);
		personalization.addSubstitution("$INITIATIVE_NAME$", initiative.getMeta().getName());
		personalization.addSubstitution("$TRIGGER_USER_NICKNAME$", triggeredByUsername);
		personalization.addSubstitution("$TRIGGER_USER_PICTURE$", triggerUserPictureUrl);
		personalization.addSubstitution("$INITIATIVE_ANCHOR$", getInitiativeAnchor(initiative));
		personalization.addSubstitution("$INITIATIVE_PICTURE$", "http://guillaumeladvie.com/wp-content/uploads/2014/04/ouishare.jpg");
		
		personalization.addSubstitution("$UNSUSCRIBE_FROM_INITIATIVE_HREF$", getUnsuscribeFromInitiativeHref(initiative));
		personalization.addSubstitution("$UNSUSCRIBE_FROM_ALL_HREF$", getUnsuscribeFromAllHref());
		
		return personalization;
	}
	
	private String getInitiativeAnchor(Initiative initiative) {
		return "<a href=" + env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
				initiative.getId().toString() + "/overview>" + initiative.getMeta().getName() + "</a>";
	}
	
	private String getUnsuscribeFromInitiativeHref(Initiative initiative) {
		return env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/unsubscribe?fromInitiativeId=" + 
				initiative.getId().toString() + "&fromInitiativeName=" + initiative.getMeta().getName();
	}
	
	private String getUnsuscribeFromAllHref() {
		return env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/unsubscribe?fromAll=true";
	}
	
}
