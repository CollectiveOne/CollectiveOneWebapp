package org.collectiveone.modules.activity;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.collectiveone.modules.assignations.Assignation;
import org.collectiveone.modules.assignations.Evaluator;
import org.collectiveone.modules.assignations.Receiver;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.tokens.InitiativeTransfer;
import org.collectiveone.modules.tokens.TokenMint;
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
		
		String result = "success";
		
		for (List<Notification> theseNotifications : segmentedNotifications) {
			String subresult = sendSegmentedNotifications(theseNotifications);
			if (!subresult.equals("success")) {
				result = "error sending notifications";
			}
		}
		
		return result;
	}
	
	private int indexOfType(ActivityType type) {
		for (int ix = 0; ix < segmentedNotifications.size(); ix++) {
			if (segmentedNotifications.get(ix).get(0).getActivity().getType().equals(type)) {
				return ix; 
			}
		}
		return -1;
	}
	
	private String sendSegmentedNotifications(List<Notification> notifications) throws IOException {
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
					
				case INITIATIVE_CREATED:
					mail = prepareInitiativeCreatedEmail(notifications);
					break;
					
				case TOKENS_MINTED:
					mail = prepareTokensMintedEmail(notifications);
					break;
					
				case PR_ASSIGNATION_CREATED:
					mail = preparePRAssignationCreatedEmail(notifications);
					break;
					
				case D_ASSIGNATION_CREATED:
					mail = prepareDAssignationEmail(notifications);
					break;
					
				case INITIATIVE_TRANSFER:
					mail = prepareInitiativeTransferEmail(notifications);
					break;
					
				case PR_ASSIGNATION_DONE:
					mail = preparePRAssignationDoneEmail(notifications);
					break;
					
				case ASSIGNATION_REVERT_ORDERED:
					mail = prepareAssignationRevertOrderedEmail(notifications);
					break;
					
				case ASSIGNATION_REVERT_CANCELLED:
					mail = prepareAssignationRevertCancelledEmail(notifications);
					break;
					
				case ASSIGNATION_REVERTED:
					mail = prepareAssignationRevertedEmail(notifications);
					break;
					
				case ASSIGNATION_DELETED:
					mail = prepareAssignationDeletedEmail(notifications);
					break;
					
				case INITIATIVE_DELETED:
					mail = prepareInitiativeDeletedEmail(notifications);
					break;
					
				default:
					break;
				}
				
				if (mail != null) {
					try {
						request.method = Method.POST;
						request.endpoint = "mail/send";
						request.body = mail.build();
						
						Response response = sg.api(request);
						
						if(response.statusCode == 202) {
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
	
	private Mail prepareInitiativeCreatedEmail(List<Notification> notifications) {
		Mail mail = new Mail();
		
		Email fromEmail = new Email();
		fromEmail.setName(env.getProperty("collectiveone.webapp.from-mail-name"));
		fromEmail.setEmail(env.getProperty("collectiveone.webapp.from-mail"));
		mail.setFrom(fromEmail);
		mail.setSubject("Initiative created");
	
		for(Notification notification : notifications) {
			if(notification.getSubscriber().getUser().getEmailNotificationsEnabled()) {
				Personalization personalization = basicInitiativePersonalization(notification);
				
				Initiative initiative = notification.getActivity().getInitiative();
				
				String message = "Created the " + getInitiativeAnchor(initiative) + " initiative and added you as a member.";
				personalization.addSubstitution("$MESSAGE$", message);
				
				mail.addPersonalization(personalization);
			} 
			
			notification.setEmailState(NotificationEmailState.DELIVERED);
			notificationRepository.save(notification);
		}
		
		mail.setTemplateId(env.getProperty("collectiveone.webapp.new-subinitiative-template"));
		
		return mail;
	}
	

	private Mail prepareSubinitaitiveCreatedEmail(List<Notification> notifications)	{
		Mail mail = new Mail();
		
		Email fromEmail = new Email();
		fromEmail.setName(env.getProperty("collectiveone.webapp.from-mail-name"));
		fromEmail.setEmail(env.getProperty("collectiveone.webapp.from-mail"));
		mail.setFrom(fromEmail);
		mail.setSubject("Subinitiative created");
	
		for(Notification notification : notifications) {
			if(notification.getSubscriber().getUser().getEmailNotificationsEnabled()) {
				Personalization personalization = basicInitiativePersonalization(notification);
				
				Initiative subInitiative = notification.getActivity().getSubInitiative();
				String transferredAssets = getTransferString(notification.getActivity().getInitiativeTransfers());
				
				String message = "<p>created the " + getInitiativeAnchor(subInitiative) + " sub-initiative and transferred " + transferredAssets + " to it.</p>";
				personalization.addSubstitution("$MESSAGE$", message);
				
				mail.addPersonalization(personalization);
			}
			
			notification.setEmailState(NotificationEmailState.DELIVERED);
			notificationRepository.save(notification);
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
		mail.setSubject("Initiative edited");
	
		for(Notification notification : notifications) {
			if(notification.getSubscriber().getUser().getEmailNotificationsEnabled()) {
				Personalization personalization = basicInitiativePersonalization(notification);
				
				Initiative initiative = notification.getActivity().getInitiative();
				String oldName = notification.getActivity().getOldName();
				String oldDriver = notification.getActivity().getOldDriver();
				
				String message = "";
				boolean changedName = false;
				boolean changedDriver = false;
				
				if(!initiative.getMeta().getName().equals(oldName)) {
					changedName = true;
				}
				
				if(!initiative.getMeta().getDriver().equals(oldDriver)) {
					changedDriver = true;
				}
				
				if(changedName && changedDriver) {
					message = "<p>changed the initiative name from " + oldName + " to " + initiative.getMeta().getName() + 
							", and the driver from <br /><br /><i>" + oldDriver + "</i><br /><br /> to <br /><br />" + initiative.getMeta().getDriver() + "</p>";
				} else if (changedName) {
					message = "<p>changed the initiative name from " + oldName + " to " + initiative.getMeta().getName() + "</p>";
				} else if (changedDriver) {
					message = "<p>changed the initiative driver from <br /><br /><i>" + oldDriver + "</i><br /><br /> to <br /><br /><i>" + initiative.getMeta().getDriver() + "</i></p>";
				} else {
					message = "";
				}
				
				personalization.addSubstitution("$MESSAGE$", message);
				
				mail.addPersonalization(personalization);
			}
			
			notification.setEmailState(NotificationEmailState.DELIVERED);
			notificationRepository.save(notification);
		}
		
		mail.setTemplateId(env.getProperty("collectiveone.webapp.new-subinitiative-template"));
		
		return mail;

	}
	
	private Mail prepareTokensMintedEmail(List<Notification> notifications)	{
		Mail mail = new Mail();
		
		Email fromEmail = new Email();
		fromEmail.setName(env.getProperty("collectiveone.webapp.from-mail-name"));
		fromEmail.setEmail(env.getProperty("collectiveone.webapp.from-mail"));
		mail.setFrom(fromEmail);
		mail.setSubject("Tokens minted");
	
		for(Notification notification : notifications) {
			if(notification.getSubscriber().getUser().getEmailNotificationsEnabled()) {
				Personalization personalization = basicInitiativePersonalization(notification);
				
				TokenMint mint = notification.getActivity().getMint();
				String message = "<p>minted " + mint.getValue() + " " + mint.getToken().getName() + " with motive: " + mint.getMotive() + ".</p>";
				
				personalization.addSubstitution("$MESSAGE$", message);
				
				mail.addPersonalization(personalization);
			}
			
			notification.setEmailState(NotificationEmailState.DELIVERED);
			notificationRepository.save(notification);
		}
		
		mail.setTemplateId(env.getProperty("collectiveone.webapp.new-subinitiative-template"));
		
		return mail;
	}
	
	
	private Mail preparePRAssignationCreatedEmail(List<Notification> notifications)	{
		Mail mail = new Mail();
		
		Email fromEmail = new Email();
		fromEmail.setName(env.getProperty("collectiveone.webapp.from-mail-name"));
		fromEmail.setEmail(env.getProperty("collectiveone.webapp.from-mail"));
		mail.setFrom(fromEmail);
		mail.setSubject("Peer-reviewed transfer created");
	
		for(Notification notification : notifications) {
			if(notification.getSubscriber().getUser().getEmailNotificationsEnabled()) {
				Personalization personalization = basicInitiativePersonalization(notification);
				
				Assignation assignation = notification.getActivity().getAssignation();
				
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
						receiversList += assignation.getReceivers().get(ix).getUser().getNickname();
					}
					
					
				}
				
				String message = "<p>created a new peer-reviewed " + getAssignationAnchor(assignation) + " of " + 
						assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() +
						" to be distributed among " + receiversList + ", with motive: </p><p>" + assignation.getMotive() + ".</p>"; 
				
				if (evaluator != null) {
					Date closeDate = assignation.getConfig().getMaxClosureDate();
					SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
					message += "<p>You are one of the evaluators of this transfer! Please visit the " + 
							getAssignationAnchor(assignation) + " page to make your evaluation.</p>" + 
							"<p>You have until " + dateFormat.format(closeDate) + " at this time of the day to do it.</p>";
				}
				
				personalization.addSubstitution("$MESSAGE$", message);
				
				mail.addPersonalization(personalization);
			}
			
			notification.setEmailState(NotificationEmailState.DELIVERED);
			notificationRepository.save(notification);
		}
		
		mail.setTemplateId(env.getProperty("collectiveone.webapp.new-subinitiative-template"));
		
		return mail;
	}
	
	
	private Mail preparePRAssignationDoneEmail(List<Notification> notifications)	{
		Mail mail = new Mail();
		
		Email fromEmail = new Email();
		fromEmail.setName(env.getProperty("collectiveone.webapp.from-mail-name"));
		fromEmail.setEmail(env.getProperty("collectiveone.webapp.from-mail"));
		mail.setFrom(fromEmail);
		mail.setSubject("Peer-reviewed transfer done");
	
		for(Notification notification : notifications) {
			if(notification.getSubscriber().getUser().getEmailNotificationsEnabled()) {
				Personalization personalization = basicInitiativePersonalization(notification);
				
				Assignation assignation = notification.getActivity().getAssignation();
				
				String message = "<p>Peer-reviewed " + getAssignationAnchor(assignation) + " with motive: </p>"
						+ "<p>" + assignation.getMotive() + "</p>"
						+ "<p>has been closed.</p>"
						+ "<p>" + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() +
						" have been transferred to its receivers.</p>";
				
				personalization.addSubstitution("$MESSAGE$", message);
				
				mail.addPersonalization(personalization);
			}
			
			notification.setEmailState(NotificationEmailState.DELIVERED);
			notificationRepository.save(notification);
		}
		
		mail.setTemplateId(env.getProperty("collectiveone.webapp.new-subinitiative-template"));
		
		return mail;
	}
	
	private Mail prepareDAssignationEmail(List<Notification> notifications)	{
		Mail mail = new Mail();
		
		Email fromEmail = new Email();
		fromEmail.setName(env.getProperty("collectiveone.webapp.from-mail-name"));
		fromEmail.setEmail(env.getProperty("collectiveone.webapp.from-mail"));
		mail.setFrom(fromEmail);
		mail.setSubject("Direct transfer ordered");
	
		for(Notification notification : notifications) {
			if(notification.getSubscriber().getUser().getEmailNotificationsEnabled()) {
				Personalization personalization = basicInitiativePersonalization(notification);
				
				Assignation assignation = notification.getActivity().getAssignation();
				
				String message = "<p>made a direct " + getAssignationAnchor(assignation) + " of " + 
						assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() +
						" to " + assignation.getReceivers().get(0).getUser().getNickname() + ", with motive: </p><p>" + assignation.getMotive() + ".</p>"; 

				personalization.addSubstitution("$MESSAGE$", message);
				
				mail.addPersonalization(personalization);
			}
			
			notification.setEmailState(NotificationEmailState.DELIVERED);
			notificationRepository.save(notification);
		}
		
		mail.setTemplateId(env.getProperty("collectiveone.webapp.new-subinitiative-template"));
		
		return mail;
	}
	
	private Mail prepareInitiativeTransferEmail(List<Notification> notifications)	{
		Mail mail = new Mail();
		
		Email fromEmail = new Email();
		fromEmail.setName(env.getProperty("collectiveone.webapp.from-mail-name"));
		fromEmail.setEmail(env.getProperty("collectiveone.webapp.from-mail"));
		mail.setFrom(fromEmail);
		mail.setSubject("New transfer to intiative");
	
		for(Notification notification : notifications) {
			if(notification.getSubscriber().getUser().getEmailNotificationsEnabled()) {
				Personalization personalization = basicInitiativePersonalization(notification);
				
				InitiativeTransfer transfer = notification.getActivity().getInitiativeTransfer();
				
				String message = "<p>made a transfer of " + 
						transfer.getValue() + " " + transfer.getTokenType().getName() +
						" to " + getInitiativeAnchor(transfer.getTo()) + ", with motive: </p><p>" + transfer.getMotive() + ".</p>"; 

				personalization.addSubstitution("$MESSAGE$", message);
				
				mail.addPersonalization(personalization);
			}
			
			notification.setEmailState(NotificationEmailState.DELIVERED);
			notificationRepository.save(notification);
		}
		
		mail.setTemplateId(env.getProperty("collectiveone.webapp.new-subinitiative-template"));
		
		return mail;
	}
	
	
	private Mail prepareAssignationRevertOrderedEmail(List<Notification> notifications)	{
		Mail mail = new Mail();
		
		Email fromEmail = new Email();
		fromEmail.setName(env.getProperty("collectiveone.webapp.from-mail-name"));
		fromEmail.setEmail(env.getProperty("collectiveone.webapp.from-mail"));
		mail.setFrom(fromEmail);
		mail.setSubject("Transfer revert ordered");
	
		for(Notification notification : notifications) {
			if(notification.getSubscriber().getUser().getEmailNotificationsEnabled()) {
				Personalization personalization = basicInitiativePersonalization(notification);
				
				Assignation assignation = notification.getActivity().getAssignation();
				
				String message = "<p>wants to revert the " + getAssignationAnchor(assignation) + 
						" of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() + 
						" with motive: " + assignation.getMotive() + ".</p> ";
				
				for (Receiver receiver : assignation.getReceivers()) {
					if (receiver.getUser().getC1Id().equals(notification.getSubscriber().getUser().getC1Id())) {
						message += "<p>You were one of the transfer receivers, so you will have to approve this revert by "
								+ "visiting the " + getAssignationAnchor(assignation) + " page.</p>";
					}
				}

				personalization.addSubstitution("$MESSAGE$", message);
				
				mail.addPersonalization(personalization);
			}
			
			notification.setEmailState(NotificationEmailState.DELIVERED);
			notificationRepository.save(notification);
		}
		
		mail.setTemplateId(env.getProperty("collectiveone.webapp.new-subinitiative-template"));
		
		return mail;
	}
	
	private Mail prepareAssignationRevertCancelledEmail(List<Notification> notifications)	{
		Mail mail = new Mail();
		
		Email fromEmail = new Email();
		fromEmail.setName(env.getProperty("collectiveone.webapp.from-mail-name"));
		fromEmail.setEmail(env.getProperty("collectiveone.webapp.from-mail"));
		mail.setFrom(fromEmail);
		mail.setSubject("Transfer revert cancelled");
	
		for(Notification notification : notifications) {
			if(notification.getSubscriber().getUser().getEmailNotificationsEnabled()) {
				Personalization personalization = basicInitiativePersonalization(notification);
				
				Assignation assignation = notification.getActivity().getAssignation();
				
				String message = "<p>ordered a revert of the " + getAssignationAnchor(assignation) + 
						" of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() + 
						" with motive: " + assignation.getMotive() + ", but this revert has been cancelled.</p> ";
				
				personalization.addSubstitution("$MESSAGE$", message);
				
				mail.addPersonalization(personalization);
			}
			
			notification.setEmailState(NotificationEmailState.DELIVERED);
			notificationRepository.save(notification);
		}
		
		mail.setTemplateId(env.getProperty("collectiveone.webapp.new-subinitiative-template"));
		
		return mail;
	}
	
	
	
	private Mail prepareAssignationRevertedEmail(List<Notification> notifications)	{
		Mail mail = new Mail();
		
		Email fromEmail = new Email();
		fromEmail.setName(env.getProperty("collectiveone.webapp.from-mail-name"));
		fromEmail.setEmail(env.getProperty("collectiveone.webapp.from-mail"));
		mail.setFrom(fromEmail);
		mail.setSubject("Transfer revert accepted");
	
		for(Notification notification : notifications) {
			if(notification.getSubscriber().getUser().getEmailNotificationsEnabled()) {
				Personalization personalization = basicInitiativePersonalization(notification);
				
				Assignation assignation = notification.getActivity().getAssignation();
				
				String message = "<p>ordered a revert of the " + getAssignationAnchor(assignation) + 
						" of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() + 
						" with motive: " + assignation.getMotive() + ", and the revert has been accepted.</p> ";
				
				personalization.addSubstitution("$MESSAGE$", message);
				
				mail.addPersonalization(personalization);
			}
			
			notification.setEmailState(NotificationEmailState.DELIVERED);
			notificationRepository.save(notification);
		}
		
		mail.setTemplateId(env.getProperty("collectiveone.webapp.new-subinitiative-template"));
		
		return mail;
	}
	
	private Mail prepareAssignationDeletedEmail(List<Notification> notifications)	{
		Mail mail = new Mail();
		
		Email fromEmail = new Email();
		fromEmail.setName(env.getProperty("collectiveone.webapp.from-mail-name"));
		fromEmail.setEmail(env.getProperty("collectiveone.webapp.from-mail"));
		mail.setFrom(fromEmail);
		mail.setSubject("Transfer deleted");
	
		for(Notification notification : notifications) {
			if(notification.getSubscriber().getUser().getEmailNotificationsEnabled()) {
				Personalization personalization = basicInitiativePersonalization(notification);
				
				Assignation assignation = notification.getActivity().getAssignation();
				
				String message = "<p>deleted the ongoing " + getAssignationAnchor(assignation) + 
						" of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() + 
						" with motive: " + assignation.getMotive() + ". No tokens have or will be transferred.</p> ";
				
				personalization.addSubstitution("$MESSAGE$", message);
				
				mail.addPersonalization(personalization);
			}
			
			notification.setEmailState(NotificationEmailState.DELIVERED);
			notificationRepository.save(notification);
		}
		
		mail.setTemplateId(env.getProperty("collectiveone.webapp.new-subinitiative-template"));
		
		return mail;
	}
	
	private Mail prepareInitiativeDeletedEmail(List<Notification> notifications)	{
		Mail mail = new Mail();
		
		Email fromEmail = new Email();
		fromEmail.setName(env.getProperty("collectiveone.webapp.from-mail-name"));
		fromEmail.setEmail(env.getProperty("collectiveone.webapp.from-mail"));
		mail.setFrom(fromEmail);
		mail.setSubject("Transfer deleted");
	
		for(Notification notification : notifications) {
			if(notification.getSubscriber().getUser().getEmailNotificationsEnabled()) {
				Personalization personalization = basicInitiativePersonalization(notification);
				
				Initiative initiative = notification.getActivity().getInitiative();
				
				String message = "<p>deleted the initiative " + getInitiativeAnchor(initiative) + 
						". All its assets, if any, have been transferred to its parent initiative,"
						+ "if the parent exist.</p> ";
				
				personalization.addSubstitution("$MESSAGE$", message);
				
				mail.addPersonalization(personalization);
			}
			
			notification.setEmailState(NotificationEmailState.DELIVERED);
			notificationRepository.save(notification);
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
		return "<a href=" + env.getProperty("collectiveone.webapp.baseurl") +"/#/inits/" + 
				initiative.getId().toString() + "/overview>" + initiative.getMeta().getName() + "</a>";
	}
	
	private String getAssignationAnchor(Assignation assignation) {
		return "<a href=" + env.getProperty("collectiveone.webapp.baseurl") +"/#/inits/" + 
				assignation.getInitiative().getId().toString() + "/assignations/" + assignation.getId().toString() + ">transfer</a>";
	}
	
	private String getTransferString(List<InitiativeTransfer> transfers) {
		return NumberFormat.getNumberInstance(Locale.US).format(transfers.get(0).getValue()) + " " + transfers.get(0).getTokenType().getName();
	}
	
	private String getUnsuscribeFromInitiativeHref(Initiative initiative) {
		return env.getProperty("collectiveone.webapp.baseurl") +"/#/inits/unsubscribe?fromInitiativeId=" + 
				initiative.getId().toString() + "&fromInitiativeName=" + initiative.getMeta().getName();
	}
	
	private String getUnsuscribeFromAllHref() {
		return env.getProperty("collectiveone.webapp.baseurl") +"/#/inits/unsubscribe?fromAll=true";
	}


}
