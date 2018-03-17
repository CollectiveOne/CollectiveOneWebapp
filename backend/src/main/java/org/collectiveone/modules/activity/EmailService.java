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

import org.collectiveone.modules.activity.enums.ActivityType;
import org.collectiveone.modules.activity.enums.NotificationEmailState;
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
					+			getActivityMessage(notification)
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
				
				String message = getActivityMessage(notification);
				personalization.addSubstitution("$MESSAGE$", message);
				
				mail.addPersonalization(personalization);
			} 
			
			notification.setEmailState(NotificationEmailState.DELIVERED);
			notificationRepository.save(notification);
		}
		
		mail.setTemplateId(env.getProperty("collectiveone.webapp.initiative-activity-template"));
		
		return mail;
	}
	
	private String getActivityMessage(Notification notification) {
		
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
		
		switch (notification.getActivity().getType()) {
			
		case INITIATIVE_CREATED:
			return "<p>created the " + getInitiativeAnchor(initiative) + " initiative and added you as a member.</p>";
			
		case SUBINITIATIVE_CREATED:
			return "<p>created the " + getInitiativeAnchor(subInitiative) + " sub-initiative and transferred " + transferredAssets + " to it.</p>";

		case INITIATIVE_EDITED:
			return "<p>edited the name or purpose of the " + getInitiativeAnchor(initiative) + " initiative.</p>";
		
		case INITIATIVE_DELETED: 
			return "<p>deleted the initiative " + getInitiativeAnchor(initiative) + 
				". All its assets, if any, have been transferred to its parent initiative,"
				+ "if the parent exist.</p> ";
		
		case TOKENS_MINTED: 
			return "<p>minted " + mint.getValue() + " " + mint.getToken().getName() + " with motive: " + mint.getMotive() + ".</p>";
			
		case TOKEN_CREATED:
			return "<p>created a new token type called " + tokenType.getName() + " in " + getInitiativeAnchor(initiative) + ", and minted " + mint.getValue() + " units.</p>";
			
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
			message = "<p>created a new peer-reviewed " + getAssignationAnchor(assignation) + " of " + 
					assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() +
					" to be distributed among " + receiversList + ", with motive: </p><p>" + assignation.getMotive() + ".</p>"; 
			
			if (evaluator != null) {
				Date closeDate = assignation.getConfig().getMaxClosureDate();
				SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
				message += "<p>You are one of the evaluators of this transfer! Please visit the " + 
						getAssignationAnchor(assignation) + " page to make your evaluation.</p>" + 
						"<p>You have until " + dateFormat.format(closeDate) + " at this time of the day to do it.</p>";
			}
			
			return message;
		
		case PR_ASSIGNATION_DONE: 
			return "<p>Peer-reviewed " + getAssignationAnchor(assignation) + " with motive: </p>"
				+ "<p>" + assignation.getMotive() + "</p>"
				+ "<p>has been closed.</p>"
				+ "<p>" + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() +
				" have been transferred to its receivers.</p>";
			
		case D_ASSIGNATION_CREATED:
			return "<p>made a direct " + getAssignationAnchor(assignation) + " of " + 
				assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() +
				" to " + assignation.getReceivers().get(0).getUser().getProfile().getNickname() + ", with motive: </p><p>" + assignation.getMotive() + ".</p>";
			
		case INITIATIVE_TRANSFER:
			return "<p>made a transfer of " + 
					transfer.getValue() + " " + transfer.getTokenType().getName() +
					" to " + getInitiativeAnchor(transfer.getTo()) + ", with motive: </p><p>" + transfer.getMotive() + ".</p>"; 

		case ASSIGNATION_REVERT_ORDERED: 
			message = "<p>wants to revert the " + getAssignationAnchor(assignation) + 
				" of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() + 
				" with motive: " + assignation.getMotive() + ".</p> ";
		
			for (Receiver receiver : assignation.getReceivers()) {
				if (receiver.getUser().getC1Id().equals(notification.getSubscriber().getUser().getC1Id())) {
					message += "<p>You were one of the transfer receivers, so you will have to approve this revert by "
							+ "visiting the " + getAssignationAnchor(assignation) + " page.</p>";
				}
			}
			return message;
			
		case ASSIGNATION_REVERT_CANCELLED: 
			return "<p>ordered a revert of the " + getAssignationAnchor(assignation) + 
				" of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() + 
				" with motive: " + assignation.getMotive() + ", but this revert has been cancelled.</p> ";
		
		case ASSIGNATION_REVERTED: 
			return "<p>ordered a revert of the " + getAssignationAnchor(assignation) + 
				" of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() + 
				" with motive: " + assignation.getMotive() + ", and the revert has been accepted.</p> ";
		
		case ASSIGNATION_DELETED: 
			return "<p>deleted the ongoing " + getAssignationAnchor(assignation) + 
				" of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() + 
				" with motive: " + assignation.getMotive() + ". No tokens have or will be transferred.</p> ";
			
		case MODEL_VIEW_CREATED:
			return "<p>created a new model view named " + getModelViewAnchor(modelView) + "</p> ";
			
		case MODEL_VIEW_EDITED:
			return "<p>edited the model " + getModelViewAnchor(modelView) + " view</p> ";
			
		case MODEL_VIEW_DELETED:
			return "<p>deleted the model " + getModelViewAnchor(modelView) + " view</p> ";
			
		case MODEL_SECTION_CREATED:
			if (onSection != null) {
				message = "<p>created a new section " + getModelSectionAnchor(modelSection) + 
						" under section " + getModelSectionAnchor(onSection) + "</p> ";
			} else {
				message = "<p>created a new section " + getModelSectionAnchor(modelSection) + 
						" under the " + getModelViewAnchor(onView) + " view</p> ";
			}							
			return message;
			
		case MODEL_SECTION_EDITED:
			return "<p>edited the model section " + getModelSectionAnchor(modelSection) + "</p> ";
			
		case MODEL_SECTION_DELETED:
			return "<p>deleted the model section " + getModelSectionAnchor(modelSection) + "</p> ";
			
		case MODEL_CARDWRAPPER_CREATED:
			return "<p>created a new card " + getModelCardWrapperAnchor(modelCardWrapper, onSection) + 
					" in the " + getModelSectionAnchor(onSection) + " section</p> ";
			
		case MODEL_CARDWRAPPER_EDITED:
			return "<p>edited the model card " + getModelCardWrapperAnchor(modelCardWrapper) + "</p> ";
			
		case MODEL_CARDWRAPPER_DELETED:
			return "<p>deleted the model card " + getModelCardWrapperAnchor(modelCardWrapper) + "</p> ";
			
		case MODEL_SECTION_ADDED: 
			if (onSection != null) {
				message = "<p>added the section " + getModelSectionAnchor(modelSection) + 
						" as sub-section of " + getModelSectionAnchor(onSection) + "</p> ";
			} else {
				message = "<p>added the section " + getModelSectionAnchor(modelSection) + 
						" under the " + getModelViewAnchor(onView) + " view</p> ";
			}							
			return message;
			
		case MODEL_SECTION_MOVED:
			if (onSection != null) {
				if (fromSection != null) {
					message = "<p>moved the section " + getModelSectionAnchor(modelSection) + 
							" from section " + getModelSectionAnchor(fromSection) + 
							" to section " + getModelSectionAnchor(onSection) + "</p> ";
				} else {
					message = "<p>moved the section " + getModelSectionAnchor(modelSection) + 
							" from view " + getModelViewAnchor(fromView) + 
							" to section " + getModelSectionAnchor(onSection) + "</p> ";
				}
				
			} else {
				if (fromSection != null) {
					message = "<p>moved the section " + getModelSectionAnchor(modelSection) + 
							" from section " + getModelSectionAnchor(fromSection) + 
							" to the " + getModelViewAnchor(onView) + "view</p> ";
				} else {
					message = "<p>moved the section " + getModelSectionAnchor(modelSection) + 
							" from view " + getModelViewAnchor(fromView) + 
							" to the " + getModelViewAnchor(onView) + " view</p> ";
				}
			}		
			return message;
			
		case MODEL_SECTION_REMOVED:
			if (fromSection != null) {
				message = "<p>removed the section " + getModelSectionAnchor(modelSection) + 
						" from section " + getModelSectionAnchor(fromSection) + "</p> ";
			} else {
				message = "<p>removed the section " + getModelSectionAnchor(modelSection) + 
						" from the " + getModelSectionAnchor(fromSection) + " view</p> ";
			}
			return message;
			
		case MODEL_CARDWRAPPER_ADDED:
			return "<p>added the card " + getModelCardWrapperAnchor(modelCardWrapper, onSection) + 
					" under section " + getModelSectionAnchor(onSection) + "</p> ";
			
		case MODEL_CARDWRAPPER_MOVED:
			return "<p>moved the card " + getModelCardWrapperAnchor(modelCardWrapper, onSection) + 
					" from section " + getModelSectionAnchor(fromSection) + 
					" to section " + getModelSectionAnchor(onSection) + "</p> ";
			
		case MODEL_CARDWRAPPER_REMOVED:
			return "<p>removed the card " + getModelCardWrapperAnchor(modelCardWrapper, fromSection) + 
					" from section " + getModelSectionAnchor(fromSection) + "</p> ";
			
		default:
			break;
		
		}
		
		return "";
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
	
	private String getAssignationAnchor(Assignation assignation) {
		return "<a href=" + env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
				assignation.getInitiative().getId().toString() + "/assignations/" + assignation.getId().toString() + ">transfer</a>";
	}
	
	private String getTransferString(List<InitiativeTransfer> transfers) {
		if (transfers.size() > 0) {
			return NumberFormat.getNumberInstance(Locale.US).format(transfers.get(0).getValue()) + " " + transfers.get(0).getTokenType().getName();
		} else {
			return "";
		}
	}
	
	private String getModelViewAnchor(ModelView view) {
		return "<a href=" + env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
				view.getInitiative().getId().toString() + "/model/view/" + 
				view.getId().toString() + ">" + view.getTitle() + "</a>";
	}
	
	private String getModelSectionAnchor(ModelSection section) {
		return "<a href=" + env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
				section.getInitiative().getId().toString() + "/model/section/" + 
				section.getId().toString() + ">" + section.getTitle() + "</a>";
	}
	
	private String getModelCardWrapperAnchor(ModelCardWrapper cardWrapper, ModelSection onSection) {
		return "<a href=" + env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
				cardWrapper.getInitiative().getId().toString() + "/model/section/" + 
				onSection.getId().toString() + "/card/" + cardWrapper.getCard().getId().toString() + 
				">" + cardWrapper.getCard().getTitle() + "</a>";
	}
	
	private String getModelCardWrapperAnchor(ModelCardWrapper cardWrapper) {
		return "<a href=" + env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
				cardWrapper.getInitiative().getId().toString() + "/model/card/" + 
				cardWrapper.getId().toString() + ">" + cardWrapper.getCard().getTitle() + "</a>";
	}
	
	private String getUnsuscribeFromInitiativeHref(Initiative initiative) {
		return env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/unsubscribe?fromInitiativeId=" + 
				initiative.getId().toString() + "&fromInitiativeName=" + initiative.getMeta().getName();
	}
	
	private String getUnsuscribeFromAllHref() {
		return env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/unsubscribe?fromAll=true";
	}
	
}
