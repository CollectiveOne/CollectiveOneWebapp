package org.collectiveone.modules.activity;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.collectiveone.modules.activity.dto.NotificationDto;
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

	public void sendNotificationsGrouped(List<Notification> orderedNotifications, String title) throws IOException {
		
		
		/* orderedNotifications are already grouped by user */
		Iterator<Notification> iterator = orderedNotifications.iterator();
		List<Notification> thisUserNotifications = new ArrayList<Notification>();
		
		while (iterator.hasNext()) {
			
			Boolean thisUserHasMore = false;
			Integer n = 0;
			
			Notification currentNotif = iterator.next();
			thisUserNotifications.add(currentNotif);
			n++;
			AppUser currentUser = currentNotif.getSubscriber().getUser();
			Notification nextNotif = null;
			
			if (iterator.hasNext()) {
				nextNotif = iterator.next();
				AppUser nextUser = nextNotif.getSubscriber().getUser();
				
				while (iterator.hasNext() && (nextUser.getC1Id().equals(currentUser.getC1Id()))) {
					
					if (n < 20) {
						thisUserNotifications.add(nextNotif);
					}
				
					nextNotif = iterator.next();
					n++;
					
					nextUser = nextNotif.getSubscriber().getUser();
				}
			}
			
			/* at this point the notifications of this user are in the list */
			try {
				sendSegmentedPerUserNotifications(
						thisUserNotifications, 
						currentUser,
						thisUserHasMore,
						title);
				
			} catch (Exception ex) {
				for (Notification notification : thisUserNotifications) {
					/* protection to prevent spamming */
					notification.setEmailNowState(NotificationState.DELIVERED);
					notification.setEmailSummaryState(NotificationState.DELIVERED);
					notificationRepository.save(notification);
				}
			}
			
			thisUserNotifications.clear();
			thisUserHasMore = false;
			
			/* add the just read notification as the first element of the list */
			thisUserNotifications.add(nextNotif);
		}
	}
	
	private String sendSegmentedPerUserNotifications(
			List<Notification> notifications, 
			AppUser receiver,
			Boolean thisUserHasMore,
			String title) throws IOException {
		
		if(env.getProperty("collectiveone.webapp.send-email-enabled").equalsIgnoreCase("true")) {
			if(notifications.size() > 0 && receiver.getEmailNotificationsEnabled()) {
				Request request = new Request();
				Mail mail = new Mail();
				
				Email fromEmail = new Email();
				fromEmail.setName(env.getProperty("collectiveone.webapp.from-mail-name"));
				fromEmail.setEmail(env.getProperty("collectiveone.webapp.from-mail"));
				mail.setFrom(fromEmail);
				mail.setSubject(title);
				
				Personalization personalization = new Personalization();
				
				Email toEmail = new Email();
				toEmail.setEmail(receiver.getEmail());
				
				personalization.addTo(toEmail);
				personalization.addSubstitution("$TITLE$", title);
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
					+			"<b>" + notificationDto.getTitle() + "</b><br>" 
					+			notificationDto.getMessage()
					+		"</td>"
					+	"</tr>";
				}
				
				personalization.addSubstitution("$ROWS$", rows);
				
				mail.setTemplateId(env.getProperty("collectiveone.webapp.user-activity-template"));
								
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
						
						for (Notification notification : notifications) {
							notification.setEmailNowState(NotificationState.DELIVERED);
							notification.setEmailSummaryState(NotificationState.DELIVERED);
							notificationRepository.save(notification);
						}
						
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
	
	private String getInitiativeAnchor(Initiative initiative) {
		return "<a href=" + env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
				initiative.getId().toString() + "/overview>" + initiative.getMeta().getName() + "</a>";
	}
	
	private String getUnsuscribeFromAllHref() {
		return env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/unsubscribe?fromAll=true";
	}
	
}
