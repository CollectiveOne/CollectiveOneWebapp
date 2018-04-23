package org.collectiveone.modules.activity;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.collectiveone.modules.activity.dto.NotificationDto;
import org.collectiveone.modules.activity.dto.PushInfoDto;
import org.collectiveone.modules.activity.dto.SubscriberDto;
import org.collectiveone.modules.activity.enums.ActivityType;
import org.collectiveone.modules.activity.enums.NotificationEmailState;
import org.collectiveone.modules.activity.enums.NotificationPushState;
import org.collectiveone.modules.activity.repositories.NotificationRepositoryIf;
import org.collectiveone.modules.activity.repositories.WantToContributeRepositoryIf;
import org.collectiveone.modules.assignations.Assignation;
import org.collectiveone.modules.assignations.Evaluator;
import org.collectiveone.modules.assignations.Receiver;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.model.ModelCardWrapper;
import org.collectiveone.modules.model.ModelSection;
import org.collectiveone.modules.model.ModelView;
import org.collectiveone.modules.tokens.InitiativeTransfer;
import org.collectiveone.modules.tokens.TokenMint;
import org.collectiveone.modules.tokens.TokenType;
import org.collectiveone.modules.users.AppUser;
import org.json.JSONObject;
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
public class PushMessageBuilder {
	
	@Autowired
	private NotificationRepositoryIf notificationRepository;
	
	@Autowired
	private WantToContributeRepositoryIf wantToContributeRepository;
	
	@Autowired
	private SendGrid sg;
	
	@Autowired
	protected Environment env;

	List<List<Notification>> segmentedPerActivityNotifications = new ArrayList<List<Notification>>();
	List<List<Notification>> segmentedPerUserNotifications = new ArrayList<List<Notification>>();
	List<List<Notification>> segmentedPerUserAndInitiativeNotifications = new ArrayList<List<Notification>>();

	private String triggeredUsername;
	private String triggeredUserPicture;
	private NotificationPushState notificationPushState;
	private Boolean isHtml;


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
				int ix = indexOfInitiative(notification.getActivity().getInitiative().getId());
				if (ix == -1) {
					List<Notification> newArray = new ArrayList<Notification>();
					newArray.add(notification);
					segmentedPerUserAndInitiativeNotifications.add(newArray);
				} else {
					segmentedPerUserAndInitiativeNotifications.get(ix).add(notification);
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
					notification.setEmailState(NotificationEmailState.DELIVERED);
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
					notification.setEmailState(NotificationEmailState.DELIVERED);
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

	public NotificationDto getPushMessage(Notification notification, Boolean isHtml) {
		triggeredUsername = notification.getActivity().getTriggerUser().getProfile().getNickname();
		triggeredUserPicture = notification.getActivity().getTriggerUser().getProfile().getPictureUrl();

		if(notification.getPushState() != null)
			notificationPushState = notification.getPushState();
		
		return getNotificationDto(notification, isHtml);
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
			if(notifications.size() > 0) {
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
					rows += 
						"<tr>"
					+		"<td class=\"avatar-box\">"
					+			"<img class=\"avatar-img\" src=\"" + notification.getActivity().getTriggerUser().getProfile().getPictureUrl() + "\"></img>"
					+		"</td>"
					+		"<td class=\"time-box\">"
					+			getTimeSinceStr(notification.getActivity().getTimestamp())
					+		"</td>"
					+		"<td class=\"activity-text\">"
					+			getNotificationDto(notification, isHtml).getMessage()
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
							notification.setEmailState(NotificationEmailState.DELIVERED);
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
		
			notification.setEmailState(NotificationEmailState.DELIVERED);
			wantToContributeRepository.save(notification);
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
				
				String message = getNotificationDto(notification, isHtml).getMessage();
				personalization.addSubstitution("$MESSAGE$", message);
				
				mail.addPersonalization(personalization);
			} 
			
			notification.setEmailState(NotificationEmailState.DELIVERED);
			notificationRepository.save(notification);
		}
		
		mail.setTemplateId(env.getProperty("collectiveone.webapp.initiative-activity-template"));
		
		return mail;
	}
	
	private String checkHtml(String str){
		return isHtml ? str : "";
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
	
	public NotificationDto getNotificationDto(Notification notification, Boolean isHtml) {
		
		this.isHtml = isHtml;
		
		Activity act = notification.getActivity();
		
		Initiative initiative = act.getInitiative();
		Initiative subInitiative = act.getSubInitiative();
		String transferredAssets = (act.getInitiativeTransfers() != null) ? getTransferString(act.getInitiativeTransfers()) : "";
		TokenType tokenType = notification.getActivity().getTokenType();
		TokenMint mint = notification.getActivity().getMint();
		Assignation assignation = notification.getActivity().getAssignation();
		InitiativeTransfer transfer = notification.getActivity().getInitiativeTransfer();
		
		ModelView modelView = notification.getActivity().getModelView();
		ModelSection modelSection = notification.getActivity().getModelSection();
		ModelCardWrapper modelCardWrapper = notification.getActivity().getModelCardWrapper();
		
		ModelSection onSection = notification.getActivity().getOnSection();
		ModelView onView = notification.getActivity().getOnView();
		
		ModelSection fromSection = notification.getActivity().getFromSection();
		ModelView fromView = notification.getActivity().getFromView();

		
		String message = "";
		String url = "";
		
		switch (notification.getActivity().getType()) {
			
		case INITIATIVE_CREATED:
			message = checkHtml("<p>") + "created the " + getInitiativeAnchor(initiative) + checkHtml(" initiative and added you as a member.</p>");
			url = (env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
					initiative.getId().toString() + "/overview");
			break;

		case INITIATIVE_EDITED:
			message = checkHtml("<p>") + "edited the " + checkHtml("name or purpose of the ") + getInitiativeAnchor(initiative) + " initiative"  + checkHtml(".</p>");
			url = (env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
					initiative.getId().toString() + "/overview");
			break;
		
		case INITIATIVE_DELETED:
			message =  checkHtml("<p>") + "deleted the initiative " + getInitiativeAnchor(initiative) + checkHtml(
					". All its assets, if any, have been transferred to its parent initiative, if the parent exist.</p>");
			url = (env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
					initiative.getId().toString() + "/overview");
			break;
				
		case SUBINITIATIVE_CREATED:
			message = checkHtml("<p>") + "created " + checkHtml("the ") + getInitiativeAnchor(subInitiative) + " sub-initiative " + checkHtml(" sub-initiative and transferred " + transferredAssets + " to it.</p>");
			url = (env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
					subInitiative.getId().toString() + "/overview") ;
			break;

		case TOKENS_MINTED:
			message = checkHtml("<p>") + "minted " + mint.getValue() + " " + mint.getToken().getName() + checkHtml(" with motive: " + mint.getMotive() + ".</p>");
			url = (env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
					initiative.getId().toString() + "/overview") ;
			break;
			
		case TOKEN_CREATED:
			message = checkHtml("<p>") + "created a new token type called " + tokenType.getName() + " in " + getInitiativeAnchor(initiative) + checkHtml(", and minted " + mint.getValue() + " units.</p>");
			url = (env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
					initiative.getId().toString() + "/overview") ;
			break;
			
		case PR_ASSIGNATION_CREATED:
			Evaluator evaluator = null;
			
			/* check if this member is an evaluator */
			for (Evaluator thisEvaluator : assignation.getEvaluators()) {
				if (thisEvaluator.getUser().getC1Id() == notification.getSubscriber().getUser().getC1Id()) {
					evaluator = thisEvaluator;
				}
			}
						String receiversList = "";
			for (int ix = 0; ix < assignation.getReceivers().size(); ix++) {
				
				/* first element starts the string */
				if (ix == 0) {
					receiversList += "";	
				}
			
				/* next elements add a comma or 'and' and a space */
				if (ix > 0) {
					if  (ix == assignation.getReceivers().size() - 1) {
						receiversList += " and ";
					} else {
						receiversList += ", ";
					}
				}
				if (assignation.getReceivers().get(ix).getUser().getC1Id() == notification.getSubscriber().getUser().getC1Id()) {
					receiversList += "you";
				} else {
					receiversList += assignation.getReceivers().get(ix).getUser().getProfile().getNickname();
				}
			}
			
			message = checkHtml("<p>") + "created a new peer-reviewed " + getAssignationAnchor(assignation) + checkHtml(" of " + 
					assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() +
					" to be distributed among " + receiversList + ", with motive: </p><p>" + assignation.getMotive() + ".</p>"); 
			
			if (evaluator != null) {
				Date closeDate = assignation.getConfig().getMaxClosureDate();
				SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
				
				message += checkHtml("You are one of the evaluators of this transfer! Please visit the " + 
						getAssignationAnchor(assignation) + " page to make your evaluation." + 
						"You have until " + dateFormat.format(closeDate) + " at this time of the day to do it.");
			}
			url = (env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
					assignation.getInitiative().getId().toString() + "/assignations/" + assignation.getId().toString());
			break;
		
		case PR_ASSIGNATION_DONE: 
			
			message = checkHtml("<p>") + "Peer-reviewed " + getAssignationAnchor(assignation) + checkHtml(" with motive: </p>"
					+ "<p>" + assignation.getMotive() + "</p>"
					+ "<p>has been closed.</p>"
					+ "<p>" + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() +
					" have been transferred to its receivers.</p>");
			
			url = (env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
					assignation.getInitiative().getId().toString() + "/assignations/" + assignation.getId().toString());
			break;
			
		case D_ASSIGNATION_CREATED:
			message = checkHtml("<p>") + "made a direct " + getAssignationAnchor(assignation) + " of " + 
				assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() +
				checkHtml(" to " + assignation.getReceivers().get(0).getUser().getProfile().getNickname() + ", with motive: </p><p>" + assignation.getMotive() + ".</p>");
			url = (env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
					assignation.getInitiative().getId().toString() + "/assignations/" + assignation.getId().toString());
			break;
			
		case INITIATIVE_TRANSFER:			
			message = checkHtml("<p>") + "made a transfer of " + 
					transfer.getValue() + " " + transfer.getTokenType().getName() +
					" to " + getInitiativeAnchor(transfer.getTo()) + checkHtml(", with motive: </p><p>" + transfer.getMotive() + ".</p>");
			url = (env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
					initiative.getId().toString() + "/overview");
			break;

		case ASSIGNATION_REVERT_ORDERED: 	
			message = checkHtml("<p>") + "wants to revert the " + getAssignationAnchor(assignation) + 
				" of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() + checkHtml(" with motive: " + assignation.getMotive() + ".</p> ");
			for (Receiver receiver : assignation.getReceivers()) {
				if (receiver.getUser().getC1Id().equals(notification.getSubscriber().getUser().getC1Id()) && isHtml) {
					message += "<p>You were one of the transfer receivers, so you will have to approve this revert by "
							+ "visiting the " + getAssignationAnchor(assignation) + " page.</p>";
				}
			}
			url = (env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
					assignation.getInitiative().getId().toString() + "/assignations/" + assignation.getId().toString());
			break;

			
		case ASSIGNATION_REVERT_CANCELLED: 
			message = checkHtml("<p>") + "ordered a revert of the " + getAssignationAnchor(assignation) + 
				" of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() +  checkHtml(", but the revert has been cancelled.</p> ");
			url = (env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
					assignation.getInitiative().getId().toString() + "/assignations/" + assignation.getId().toString());
			break;
		
		case ASSIGNATION_REVERTED: 
			message = checkHtml("<p>") + "ordered a revert of the " + getAssignationAnchor(assignation) + 
				" of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() + checkHtml(" with motive: " + assignation.getMotive() + ", and the revert has been accepted.</p> ");
			url = (env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
					assignation.getInitiative().getId().toString() + "/assignations/" + assignation.getId().toString());
			break;
		
		case ASSIGNATION_DELETED: 
			message = checkHtml("<p>") + "deleted the ongoing " + getAssignationAnchor(assignation) + 
				" of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() + 
				checkHtml(" with motive: " + assignation.getMotive() + ". No tokens have or will be transferred.</p> ");
			url = env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
					assignation.getInitiative().getId().toString() + "/assignations/" + assignation.getId().toString();
			break;
			
		case MODEL_VIEW_CREATED:
			message = checkHtml("<p>") + "created" + checkHtml("a new model ")  + getModelViewAnchor(modelView) +" view " + checkHtml("</p>");
			url = (env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
					modelView.getInitiative().getId().toString() + "/model/view/" + 
					modelView.getId().toString());
			break;
			
		case MODEL_VIEW_EDITED:
			message = checkHtml("<p>") + "edited the model " + getModelViewAnchor(modelView) + " view" + checkHtml("</p>");
			url = (env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
					modelView.getInitiative().getId().toString() + "/model/view/" + 
					modelView.getId().toString());
			break;
			
		case MODEL_VIEW_DELETED:
			message = checkHtml("<p>") + "deleted the model " + getModelViewAnchor(modelView)+ " view" + checkHtml("</p>");
			url = (env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
					modelView.getInitiative().getId().toString() + "/model/view/" + 
					modelView.getId().toString());
			break;
			
		case MODEL_SECTION_CREATED:
			if (onSection != null) {
				message = checkHtml("<p>") + "created the subsection " + getModelSectionAnchor(modelSection) + 
						checkHtml(" under section " + getModelSectionAnchor(onSection) + "</p>");
			} else {
				message = checkHtml("<p>") + "created the section " + getModelSectionAnchor(modelSection) + 
						checkHtml(" under the " + getModelViewAnchor(onView) + " view</p>");
			}
			url = (env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
					modelSection.getInitiative().getId().toString() + "/model/section/" + 
					modelSection.getId().toString());				
			break;
			
		case MODEL_SECTION_EDITED:
			message = checkHtml("<p>") + "edited the model section " + getModelSectionAnchor(modelSection) + checkHtml("</p>");
			url = (env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
					modelSection.getInitiative().getId().toString() + "/model/section/" + 
					modelSection.getId().toString());
			break;
			
		case MODEL_SECTION_DELETED:
			message = checkHtml("<p>") + "deleted the model section " + getModelSectionAnchor(modelSection) + checkHtml("</p>");
			url = (env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
					modelSection.getInitiative().getId().toString() + "/model/section/" + 
					modelSection.getId().toString());
			break;
			
		case MODEL_CARDWRAPPER_CREATED:
			message = checkHtml("<p>") + "created the card " + getModelCardWrapperAnchor(modelCardWrapper, onSection) + 
					checkHtml(" on section " + getModelSectionAnchor(onSection) + "</p>");
			url = (env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
					modelCardWrapper.getInitiative().getId().toString() + "/model/section/" + 
					onSection.getId().toString() + "/card/" + modelCardWrapper.getCard().getId().toString());
			break;
			
		case MODEL_CARDWRAPPER_EDITED:
			message = checkHtml("<p>") + "edited the card " + getModelCardWrapperAnchor(modelCardWrapper) + checkHtml("</p>");
			url = (env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
					modelCardWrapper.getInitiative().getId().toString() + "/model/card/" + 
					modelCardWrapper.getId().toString());
			break;
			
		case MODEL_CARDWRAPPER_DELETED:
			message = checkHtml("<p>") + "deleted the card " + getModelCardWrapperAnchor(modelCardWrapper) + checkHtml("</p>");
			url = (env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
					modelCardWrapper.getInitiative().getId().toString() + "/model/card/" + 
					modelCardWrapper.getId().toString());
			break;
			
		case MODEL_SECTION_ADDED: 
			if (onSection != null) {
				message = checkHtml("<p>") + "added the section " + getModelSectionAnchor(modelSection) + 
						checkHtml(" as sub-section of " + getModelSectionAnchor(onSection) + "</p>");
			} else {
				message = checkHtml("<p>") + "added the section " + getModelSectionAnchor(modelSection) + 
						checkHtml(" under the " + getModelViewAnchor(onView) + " view </p>");
			}
			url = (env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
					modelSection.getInitiative().getId().toString() + "/model/section/" + 
					modelSection.getId().toString());					
			break;
			
		case MODEL_SECTION_MOVED:
			if (onSection != null) {
				if (fromSection != null) {
					message = checkHtml("<p>") + "moved the section " + getModelSectionAnchor(modelSection) + 
							checkHtml(" from " + getModelSectionAnchor(fromSection) + 
							" to " + getModelSectionAnchor(onSection) + "</p>");
				} else {
					message = checkHtml("<p>") + "moved the section " + getModelSectionAnchor(modelSection) + 
							checkHtml(" from " + getModelViewAnchor(fromView) + 
							" to " + getModelSectionAnchor(onSection) + "</p>");
				}
				
			} else {
				if (fromSection != null) {
					message = checkHtml("<p>") + "moved the section " + getModelSectionAnchor(modelSection) + 
							checkHtml(" from " + getModelSectionAnchor(fromSection) + 
							" to " + getModelViewAnchor(onView)+ "</p> ");
				} else {
					message = checkHtml("<p>") + "moved the section " + getModelSectionAnchor(modelSection) + 
							checkHtml(" from " + getModelViewAnchor(fromView) + 
							" to " + getModelViewAnchor(onView)+ "</p> ");
				}
			}
			url = (env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
					modelSection.getInitiative().getId().toString() + "/model/section/" + 
					modelSection.getId().toString());		
			break;
			
		case MODEL_SECTION_REMOVED:
			if (fromSection != null) {
				message = checkHtml("<p>") + "removed the section " + getModelSectionAnchor(modelSection) + 
						checkHtml(" from " + getModelSectionAnchor(fromSection) + "</p> ");
			} else {
				message = checkHtml("<p>") + "removed the section " + getModelSectionAnchor(modelSection) + 
						checkHtml(" from the " + getModelViewAnchor(fromView) + " view</p> ");
			}
			url = (env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
					modelSection.getInitiative().getId().toString() + "/model/section/" + 
					modelSection.getId().toString());
			break;
			
		case MODEL_CARDWRAPPER_ADDED:
			message = checkHtml("<p>") + "added the card " + getModelCardWrapperAnchor(modelCardWrapper, onSection) + 
					checkHtml(" under section " + getModelSectionAnchor(onSection) +  "</p> ");
			url = (env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
					modelCardWrapper.getInitiative().getId().toString() + "/model/section/" + 
					onSection.getId().toString() + "/card/" + modelCardWrapper.getCard().getId().toString());
			break;
			
		case MODEL_CARDWRAPPER_MOVED:
			message = checkHtml("<p>") + "moved the card " + getModelCardWrapperAnchor(modelCardWrapper, onSection) + 
					checkHtml(" from " + getModelSectionAnchor(fromSection) + 
					" to " + getModelSectionAnchor(onSection) + "</p> ");
			url = (env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
					modelCardWrapper.getInitiative().getId().toString() + "/model/section/" + 
					onSection.getId().toString() + "/card/" + modelCardWrapper.getCard().getId().toString());
			break;
			
		case MODEL_CARDWRAPPER_REMOVED:
			message = checkHtml("<p>") + "removed the card " + getModelCardWrapperAnchor(modelCardWrapper, fromSection) + 
					checkHtml(" from section " + getModelSectionAnchor(fromSection) + "</p> ");
			url = (env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
					modelCardWrapper.getInitiative().getId().toString() + "/model/section/" + 
					onSection.getId().toString() + "/card/" + modelCardWrapper.getCard().getId().toString());
			break;
		case MESSAGE_POSTED:
			String from = "CollectiveOne";
			if(notification.getActivity().getModelCardWrapper() != null) {
				from = getModelCardWrapperAnchor(modelCardWrapper) + " card";
				url = (env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
						modelCardWrapper.getInitiative().getId().toString() + "/model/card/" + 
						modelCardWrapper.getId().toString());
			}else if(notification.getActivity().getModelSection() != null) {
				from = getModelSectionAnchor(modelSection) + " section";
				url = (env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
						modelSection.getInitiative().getId().toString() + "/model/section/" + 
						modelSection.getId().toString());
			}else if(notification.getActivity().getModelView() != null) {
				from = getModelViewAnchor(modelView) + " view";
				url = (env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
						modelView.getInitiative().getId().toString() + "/model/view/" + 
						modelView.getId().toString());
			}else if(notification.getActivity().getInitiative() != null) {
				from = getInitiativeAnchor(initiative) + " initiative";
				url = (env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
						initiative.getId().toString() + "/overview");
			}
			message = "commented in " + from;
			break;
		case MODEL_VIEW_MOVED:
			message = checkHtml("<p>") + "moved the view " + getModelViewAnchor(modelView) + " view" + checkHtml("</p>");
			url = (env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
					modelView.getInitiative().getId().toString() + "/model/view/" + 
					modelView.getId().toString());
			break;
		default:
			message = "You have received a notification";
			url = "http://www.collectiveone.org";
			break;
		
		}

		NotificationDto dto = new NotificationDto();
		
		dto.setId(notification.getId().toString());
		dto.setTriggerUser(notification.getActivity().getTriggerUser().toDtoLight());
		dto.setActivity(notification.getActivity().toDto());
		dto.setSubscriber(notification.getSubscriber().getUser().toDtoLight());
		dto.setSubscriberState(notification.getSubscriber().getState().toString());
		dto.setEmailState(notification.getEmailState().toString());
		if(notification.getPushState() != null)
			dto.setPushState(notification.getPushState().toString());
		dto.setMessage((isHtml ? "" : dto.getTriggerUser().getNickname() + " ") + message); //Add trigger username for pushNotifications
		dto.setUrl(url);
		dto.setIsHtml(isHtml);
		
		return dto;
	}
	
	private String getInitiativeAnchor(Initiative initiative) {
		
		return checkHtml("<a href=" + env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
				initiative.getId().toString() + "/overview>") +  initiative.getMeta().getName() + checkHtml("</a>");
	}
	
	private String getAssignationAnchor(Assignation assignation) {
		
		return checkHtml("<a href=" + env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
				assignation.getInitiative().getId().toString() + "/assignations/" + assignation.getId().toString() + ">") + "transfer" + checkHtml("</a>");
	}
	
	private String getTransferString(List<InitiativeTransfer> transfers) {
		if (transfers.size() > 0) {
			return NumberFormat.getNumberInstance(Locale.US).format(transfers.get(0).getValue()) + " " + transfers.get(0).getTokenType().getName();
		} else {
			return "";
		}
	}
	
	private String getModelViewAnchor(ModelView view) {
		return checkHtml("<a href=" + env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
				view.getInitiative().getId().toString() + "/model/view/" + view.getId().toString() + ">") +
					view.getTitle() +
				checkHtml("</a>");
	}
	
	private String getModelSectionAnchor(ModelSection section) {
		return checkHtml("<a href=" + env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
				section.getInitiative().getId().toString() + "/model/section/" + 
				section.getId().toString() + ">") + section.getTitle() + checkHtml("</a>");
	}
	
	private String getModelCardWrapperAnchor(ModelCardWrapper cardWrapper, ModelSection onSection) {
		return checkHtml("<a href=" + env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
				cardWrapper.getInitiative().getId().toString() + "/model/section/" + 
				onSection.getId().toString() + "/card/" + cardWrapper.getCard().getId().toString() + 
				">") + cardWrapper.getCard().getTitle() + checkHtml("</a>");
	}
	
	private String getModelCardWrapperAnchor(ModelCardWrapper cardWrapper) {
		return checkHtml("<a href=" + env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
				cardWrapper.getInitiative().getId().toString() + "/model/card/" + 
				cardWrapper.getId().toString() + ">") + cardWrapper.getCard().getTitle() + checkHtml("</a>");
	}
	
	private String getUnsuscribeFromInitiativeHref(Initiative initiative) {
		return env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/unsubscribe?fromInitiativeId=" + 
				initiative.getId().toString() + "&fromInitiativeName=" + initiative.getMeta().getName();
	}
	
	private String getUnsuscribeFromAllHref() {
		return env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/unsubscribe?fromAll=true";
	}
	

	
}
