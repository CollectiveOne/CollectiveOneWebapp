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
	
	public String getPushMessage(Notification notification) {
		String triggeredUsername = getTriggeredUser(notification);
		return triggeredUsername + " " + getActivityMessage(notification);
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
			return "created the new initiative " + getInitiativeAnchor(initiative);

		case INITIATIVE_EDITED:
			return "edited the " + getInitiativeAnchor(initiative) + " initiative.";
		
		case INITIATIVE_DELETED: 
			return "deleted the initiative " + getInitiativeAnchor(initiative);
				
		case SUBINITIATIVE_CREATED:
			return "created " + getInitiativeAnchor(subInitiative) + " sub-initiative under " +  getInitiativeAnchor(initiative);

		case TOKENS_MINTED: 
			return "minted " + mint.getValue() + " " + mint.getToken().getName() + " in " +  getInitiativeAnchor(initiative);
			
		case TOKEN_CREATED:
			return "created a new token type called " + tokenType.getName() + " in " + getInitiativeAnchor(initiative);
			
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
			message = "created a new peer-reviewed " + getAssignationAnchor(assignation) + " of " + 
					assignation.getBills().get(0).getValue() + " " + " from " + getInitiativeAnchor(initiative); 
			
			if (evaluator != null) {
				Date closeDate = assignation.getConfig().getMaxClosureDate();
				SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
				message += "You are one of the evaluators of this transfer! Please visit the " + 
						getAssignationAnchor(assignation) + " page to make your evaluation." + 
						"You have until " + dateFormat.format(closeDate) + " at this time of the day to do it.";
			}
			
			return message;
		
		case PR_ASSIGNATION_DONE: 
			return "Peer-reviewed " + getAssignationAnchor(assignation) + " has been done."
				+ "" + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() +
				" have been transferred to its receivers.";
			
		case D_ASSIGNATION_CREATED:
			return "made a direct " + getAssignationAnchor(assignation) + " of " + 
				assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() + " from " + getInitiativeAnchor(initiative);  
			
		case INITIATIVE_TRANSFER:
			return "made a transfer of " + 
					transfer.getValue() + " " + transfer.getTokenType().getName() +
					" to " + getInitiativeAnchor(transfer.getTo()); 

		case ASSIGNATION_REVERT_ORDERED: 
			return "wants to revert the " + getAssignationAnchor(assignation) + 
				" of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() + " from " + getInitiativeAnchor(initiative);

			
		case ASSIGNATION_REVERT_CANCELLED: 
			return "ordered a revert of the " + getAssignationAnchor(assignation) + 
				" of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() +  ", but the revert has been cancelled. ";
		
		case ASSIGNATION_REVERTED: 
			return "ordered a revert of the " + getAssignationAnchor(assignation) + 
				" of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() + ", and the revert has been accepted. ";
		
		case ASSIGNATION_DELETED: 
			return "deleted the ongoing transfer. " + getAssignationAnchor(assignation) + 
				" of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName();
			
		case MODEL_VIEW_CREATED:
			return "created the view " + getModelViewAnchor(modelView) + " ";
			
		case MODEL_VIEW_EDITED:
			return "edited the view " + getModelViewAnchor(modelView);
			
		case MODEL_VIEW_DELETED:
			return "deleted the view " + getModelViewAnchor(modelView);
			
		case MODEL_SECTION_CREATED:
			if (onSection != null) {
				message = "created the subsection " + getModelSectionAnchor(modelSection) + 
						" under section " + getModelSectionAnchor(onSection) + " ";
			} else {
				message = "created the section " + getModelSectionAnchor(modelSection) + 
						" under the " + getModelViewAnchor(onView) + " view ";
			}							
			return message;
			
		case MODEL_SECTION_EDITED:
			return "edited the model section " + getModelSectionAnchor(modelSection) + " ";
			
		case MODEL_SECTION_DELETED:
			return "deleted the model section " + getModelSectionAnchor(modelSection) + " ";
			
		case MODEL_CARDWRAPPER_CREATED:
			return "created the card " + getModelCardWrapperAnchor(modelCardWrapper, onSection) + 
					" on section " + getModelSectionAnchor(onSection);
			
		case MODEL_CARDWRAPPER_EDITED:
			return "edited the card " + getModelCardWrapperAnchor(modelCardWrapper) + " ";
			
		case MODEL_CARDWRAPPER_DELETED:
			return "deleted the card " + getModelCardWrapperAnchor(modelCardWrapper) + " ";
			
		case MODEL_SECTION_ADDED: 
			if (onSection != null) {
				message = "added the section " + getModelSectionAnchor(modelSection) + 
						" as sub-section of " + getModelSectionAnchor(onSection) + " ";
			} else {
				message = "added the section " + getModelSectionAnchor(modelSection) + 
						" under the " + getModelViewAnchor(onView) + " view ";
			}							
			return message;
			
		case MODEL_SECTION_MOVED:
			if (onSection != null) {
				if (fromSection != null) {
					message = "moved the section " + getModelSectionAnchor(modelSection) + 
							" from " + getModelSectionAnchor(fromSection) + 
							" to " + getModelSectionAnchor(onSection) + " ";
				} else {
					message = "moved the section " + getModelSectionAnchor(modelSection) + 
							" from " + getModelViewAnchor(fromView) + 
							" to " + getModelSectionAnchor(onSection) + " ";
				}
				
			} else {
				if (fromSection != null) {
					message = "moved the section " + getModelSectionAnchor(modelSection) + 
							" from " + getModelSectionAnchor(fromSection) + 
							" to " + getModelViewAnchor(onView);
				} else {
					message = "moved the section " + getModelSectionAnchor(modelSection) + 
							" from " + getModelViewAnchor(fromView) + 
							" to " + getModelViewAnchor(onView);
				}
			}		
			return message;
			
		case MODEL_SECTION_REMOVED:
			if (fromSection != null) {
				message = "removed the section " + getModelSectionAnchor(modelSection) + 
						" from " + getModelSectionAnchor(fromSection);
			} else {
				message = "removed the section " + getModelSectionAnchor(modelSection) + 
						" from " + getModelSectionAnchor(fromSection);
			}
			return message;
			
		case MODEL_CARDWRAPPER_ADDED:
			return "added the card " + getModelCardWrapperAnchor(modelCardWrapper, onSection) + 
					" under " + getModelSectionAnchor(onSection);
			
		case MODEL_CARDWRAPPER_MOVED:
			return "moved the card " + getModelCardWrapperAnchor(modelCardWrapper, onSection) + 
					" from " + getModelSectionAnchor(fromSection) + 
					" to " + getModelSectionAnchor(onSection);
			
		case MODEL_CARDWRAPPER_REMOVED:
			return "removed the card " + getModelCardWrapperAnchor(modelCardWrapper, fromSection) + 
					" from " + getModelSectionAnchor(fromSection);
		case MESSAGE_POSTED:
			String from = "CollectiveOne";
			if(notification.getActivity().getModelCardWrapper() != null)
				from = getModelCardWrapperAnchor(modelCardWrapper) + " card";
			else if(notification.getActivity().getModelSection() != null)
				from = getModelSectionAnchor(modelSection) + " section";
			else if(notification.getActivity().getModelView() != null)
				from = getModelViewAnchor(modelView) + " view";
			else if(notification.getActivity().getInitiative() != null)
				from = getInitiativeAnchor(initiative) + " initiative";
			
			return "commented in " + from;
		case MODEL_VIEW_MOVED:
			return "moved the view " + getModelViewAnchor(modelView) + " view";
		default:
			break;
		
		}
		
		return "";
	}

//	private Personalization basicInitiativePersonalization(Notification notification) {
//		String toEmailString = notification.getSubscriber().getUser().getEmail();
//		String triggeredByUsername = notification.getActivity().getTriggerUser().getProfile().getNickname();
//		String triggerUserPictureUrl = notification.getActivity().getTriggerUser().getProfile().getPictureUrl();
//		Initiative initiative = notification.getActivity().getInitiative();
//		
//		Personalization personalization = new Personalization();
//		
//		Email toEmail = new Email();
//		toEmail.setEmail(toEmailString);
//		
//		personalization.addTo(toEmail);
//		personalization.addSubstitution("$INITIATIVE_NAME$", initiative.getMeta().getName());
//		personalization.addSubstitution("$TRIGGER_USER_NICKNAME$", triggeredByUsername);
//		personalization.addSubstitution("$TRIGGER_USER_PICTURE$", triggerUserPictureUrl);
//		personalization.addSubstitution("$INITIATIVE_ANCHOR$", getInitiativeAnchor(initiative));
//		personalization.addSubstitution("$INITIATIVE_PICTURE$", "http://guillaumeladvie.com/wp-content/uploads/2014/04/ouishare.jpg");
////		
////		personalization.addSubstitution("$UNSUSCRIBE_FROM_INITIATIVE_HREF$", getUnsuscribeFromInitiativeHref(initiative));
////		personalization.addSubstitution("$UNSUSCRIBE_FROM_ALL_HREF$", getUnsuscribeFromAllHref());
////		
//		return personalization;
//	}
//	
	private String getTriggeredUser(Notification notification) {
		return notification.getActivity().getTriggerUser().getProfile().getNickname();
	}
	
	private String getInitiativeAnchor(Initiative initiative) {
		return initiative.getMeta().getName();
	}
	
	private String getAssignationAnchor(Assignation assignation) {
		return "transfer";
	}
	
	private String getTransferString(List<InitiativeTransfer> transfers) {
		if (transfers.size() > 0) {
			return NumberFormat.getNumberInstance(Locale.US).format(transfers.get(0).getValue()) + " " + transfers.get(0).getTokenType().getName();
		} else {
			return "";
		}
	}
	
	private String getModelViewAnchor(ModelView view) {
		return view.getTitle();
	}
	
	private String getModelSectionAnchor(ModelSection section) {
		return section.getTitle();
	}
	
	private String getModelCardWrapperAnchor(ModelCardWrapper cardWrapper, ModelSection onSection) {
		return cardWrapper.getCard().getTitle();
	}
	
	private String getModelCardWrapperAnchor(ModelCardWrapper cardWrapper) {
		return cardWrapper.getCard().getTitle();
	}
//	
//	private String getUnsuscribeFromInitiativeHref(Initiative initiative) {
//		return env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/unsubscribe?fromInitiativeId=" + 
//				initiative.getId().toString() + "&fromInitiativeName=" + initiative.getMeta().getName();
//	}
//	
//	private String getUnsuscribeFromAllHref() {
//		return env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/unsubscribe?fromAll=true";
//	}
//	
}
