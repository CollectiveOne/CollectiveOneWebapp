package org.collectiveone.modules.activity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.collectiveone.modules.activity.dto.NotificationDto;
import org.collectiveone.modules.assignations.Assignation;
import org.collectiveone.modules.assignations.Evaluator;
import org.collectiveone.modules.assignations.Receiver;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.model.ModelCardWrapper;
import org.collectiveone.modules.model.ModelSection;
import org.collectiveone.modules.model.enums.ElementConsentPositionColor;
import org.collectiveone.modules.tokens.InitiativeTransfer;
import org.collectiveone.modules.tokens.TokenMint;
import org.collectiveone.modules.tokens.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class NotificationDtoBuilder {
	
	@Autowired
	protected Environment env;
	
	private Boolean isHtml = false;

	private String checkHtml(String str){
		return isHtml ? str : "";
	}

	public NotificationDto getNotificationDto(
			Notification notification, 
			Boolean isHtml) {
		
		this.isHtml = isHtml;
		
		Activity act = notification.getActivity();
		
		Initiative initiative = act.getInitiative();
		Initiative subInitiative = act.getSubInitiative();
		TokenType tokenType = act.getTokenType();
		TokenMint mint = act.getMint();
		Assignation assignation = act.getAssignation();
		InitiativeTransfer transfer = act.getInitiativeTransfer();
		
		ModelSection modelSection = null;
		ModelCardWrapper modelCardWrapper = null;
		
		if (act.getModelCardWrapperAddition() != null) {
			modelSection = act.getModelCardWrapperAddition().getSection();
			modelCardWrapper = act.getModelCardWrapperAddition().getCardWrapper();
		} else {
			modelSection = act.getModelSection();
			modelCardWrapper = act.getModelCardWrapper();
		}
		
		ModelSection onSection = null;
		
		if (act.getModelSubsection() != null) {
			modelSection = act.getModelSubsection().getSection();
			onSection = act.getModelSubsection().getParentSection();
		} else {
			onSection = act.getOnSection();	
		}
		
		ModelSection fromSection = act.getFromSection();
		
		if (act.getModelCardWrapperAddition() != null) {
			modelCardWrapper = act.getModelCardWrapperAddition().getCardWrapper();
			onSection = act.getModelCardWrapperAddition().getSection();
		}
		
		String cardText = "";
		if (modelCardWrapper != null) {
			String text = modelCardWrapper.getCard().getText();
			if (text != null) {
				cardText = isHtml ? text : (text.length() > 60 ? text.substring(0, 60) + "..." : text);
				cardText = modelCardWrapper.getCard().getTitle() + (isHtml ? "" : " - ") + checkHtml("</p><p>") + cardText;	
			} else {
				cardText = "";
			}			 
		}
		
		String title = "";
		String message = "";
		String url = "";
		
		switch (notification.getActivity().getType()) {
			
		case INITIATIVE_CREATED:
			title = "created the " + getInitiativeAnchor(initiative);
			message = "and added you as a member";
			url = getInitiativeUrl(initiative.getId());
			break;

		case INITIATIVE_EDITED:
			title = "edited the " + getInitiativeAnchor(initiative) + " initiative";
			message = "";
			url = getInitiativeUrl(initiative.getId());
			break;
		
		case INITIATIVE_DELETED:
			title = "deleted the initiative " + getInitiativeAnchor(initiative);
			message =  "";
			url = getInitiativeUrl(initiative.getId());
			break;
				
		case SUBINITIATIVE_CREATED:
			title = "created the " + getInitiativeAnchor(subInitiative) + " sub-initiative";
			message = "under the " + getInitiativeAnchor(initiative) + " initiative";
			if (act.getInitiativeTransfers() != null) {
				message += checkHtml("<p>") + " And transferred " + getTransferString(act.getInitiativeTransfers()) + " to it." + checkHtml("</p>");
			}	
			url = getInitiativeUrl(subInitiative.getId());
			break;

		case TOKENS_MINTED:
			title = "minted " + mint.getValue() + " " + mint.getToken().getName();
			message = "with motive: " + mint.getMotive();
			url = getInitiativeUrl(initiative.getId());
			break;
			
		case TOKENS_BURNT:
			title = "burnt " + mint.getValue() + " " + mint.getToken().getName();
			message = "with motive: " + mint.getMotive();
			url = getInitiativeUrl(initiative.getId());
			break;
			
		case TOKEN_CREATED:
			title = "created a new token type called " + tokenType.getName(); 
			message = "In " + getInitiativeAnchor(initiative) + ", and minted " + mint.getValue() + " units";
			url = getInitiativeUrl(initiative.getId());
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
			
			title = "created a new peer-reviewed " + getAssignationAnchor(assignation);
			message = "of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() +
					" to be distributed among " + receiversList + ", with motive: " + checkHtml("</p><p>") + assignation.getMotive(); 
			
			if (evaluator != null) {
				Date closeDate = assignation.getConfig().getMaxClosureDate();
				SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
				
				message += "You are one of the evaluators of this transfer! Please visit the " + 
						getAssignationAnchor(assignation) + " page to make your evaluation." + 
						"You have until " + dateFormat.format(closeDate) + " at this time of the day to do it.";
			}
			url = getAssignationUrl(assignation.getInitiative().getId(), assignation.getId());
			break;
		
		case PR_ASSIGNATION_DONE: 
			title = "Peer-reviewed " + getAssignationAnchor(assignation) + " done";
			message = "Peer-reviewed " + getAssignationAnchor(assignation) + " with motive: " + checkHtml("</p>") + 
					checkHtml("<p>") + assignation.getMotive() + checkHtml("</p>") + 
					checkHtml("<p>") + "has been closed." + checkHtml("</p>") + 
					checkHtml("<p>") + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() + 
					" have been transferred to its receivers.";
			
			url = getAssignationUrl(assignation.getInitiative().getId(), assignation.getId());
			break;
			
		case D_ASSIGNATION_CREATED:
			title = "made a transfer of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName();
			message = "with motive: " + 
				checkHtml("</p><p>") + assignation.getMotive();
			url = getAssignationUrl(assignation.getInitiative().getId(), assignation.getId());
			break;
			
		case INITIATIVE_TRANSFER:	
			title = "made a transfer of " + transfer.getValue() + " " + transfer.getTokenType().getName() +
					" to " + getInitiativeAnchor(transfer.getTo());
			message = "with motive: " + 
					checkHtml("</p><p>") + transfer.getMotive();
			url = getInitiativeUrl(initiative.getId());
			break;

		case ASSIGNATION_REVERT_ORDERED:
			title = "wants to revert the " + getAssignationAnchor(assignation);
			message =  "of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() + 
				" with motive: " + assignation.getMotive() + ".</p> ";
			for (Receiver receiver : assignation.getReceivers()) {
				if (receiver.getUser().getC1Id().equals(notification.getSubscriber().getUser().getC1Id()) && isHtml) {
					message += "<p>You were one of the transfer receivers, so you will have to approve this revert by "
							+ "visiting the " + getAssignationAnchor(assignation) + " page.</p>";
				}
			}
			url = getAssignationUrl(assignation.getInitiative().getId(), assignation.getId());
			break;

			
		case ASSIGNATION_REVERT_CANCELLED: 
			title = "order to revert" + getAssignationAnchor(assignation) + "cancelled";
			message = "The order to revert the " + getAssignationAnchor(assignation) + 
				" of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() +  
				" has been cancelled";
			url = getAssignationUrl(assignation.getInitiative().getId(), assignation.getId());
			break;
		
		case ASSIGNATION_REVERTED: 
			title = "order to revert" + getAssignationAnchor(assignation) + "accepted";
			message = "The order to revert the " + getAssignationAnchor(assignation) + 
				" of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() + 
				" with motive: " + assignation.getMotive() + ", and the revert has been accepted ";
			url = getAssignationUrl(assignation.getInitiative().getId(), assignation.getId());
			break;
		
		case ASSIGNATION_DELETED:
			title = "deleted the " + getAssignationAnchor(assignation);
			message = "of " + assignation.getBills().get(0).getValue() + " " + assignation.getBills().get(0).getTokenType().getName() + 
				" with motive: " + assignation.getMotive() + ". No tokens have or will be transferred.";
			url = getAssignationUrl(assignation.getInitiative().getId(), assignation.getId());
			break;
			
		case MODEL_SECTION_CREATED:
			title = "created the subsection " + getModelSectionAnchor(modelSection);
			message = "under section " + getModelSectionAnchor(onSection);
			url = getModelSectionUrl(modelSection.getInitiative().getId(), modelSection.getId());		
			break;
			
		case MODEL_SECTION_EDITED:
			title = "edited the section " + getModelSectionAnchor(modelSection);
			message = cardText;
			url = getModelSectionUrl(modelSection.getInitiative().getId(), modelSection.getId());
			break;
			
		case MODEL_SECTION_DELETED:
			title = "deleted the model section " + getModelSectionAnchor(modelSection);
			message = "";
			url = getModelSectionUrl(modelSection.getInitiative().getId(), modelSection.getId());
			break;
			
		case MODEL_CARDWRAPPER_CREATED:
			title = "created the card " + getModelCardWrapperAnchor(modelCardWrapper, onSection) + " on section " + getModelSectionAnchor(onSection);
			message = cardText;
			url = getModelCardWrapperUrl(modelCardWrapper.getInitiative().getId(), onSection.getId(), modelCardWrapper.getId());
			break;
			
		case MODEL_CARDWRAPPER_MADE_SHARED:
			title = "made the card " + getModelCardWrapperAnchor(modelCardWrapper, onSection) + " shared" +  
				" on section " + getModelSectionAnchor(onSection);
			message = "";
			url = getModelCardWrapperUrl(modelCardWrapper.getInitiative().getId(), onSection.getId(), modelCardWrapper.getId());
			break;
			
		case MODEL_CARDWRAPPER_MADE_COMMON:
			title = "made the card " + getModelCardWrapperAnchor(modelCardWrapper, onSection) + " common" + 
				" on section " + getModelSectionAnchor(onSection);
			message = "";
			url = getModelCardWrapperUrl(modelCardWrapper.getInitiative().getId(), onSection.getId(), modelCardWrapper.getId());
			break;
			
		case MODEL_CARDWRAPPER_EDITED:
			title = "edited the card " + getModelCardWrapperAnchor(modelCardWrapper);
			message = cardText;
			url = getModelCardWrapperUrl(modelCardWrapper.getInitiative().getId(), modelCardWrapper.getId());
			break;
			
		case MODEL_CARDWRAPPER_DELETED:
			title = "deleted the card " + getModelCardWrapperAnchor(modelCardWrapper);
			message = cardText;
			url = getModelCardWrapperUrl(modelCardWrapper.getInitiative().getId(), modelCardWrapper.getId());
			break;
			
		case MODEL_SECTION_ADDED: 
			title = "added the section " + getModelSectionAnchor(modelSection) +
				" as sub-section of " + getModelSectionAnchor(onSection);
			message = "";
			url = getModelSectionUrl(modelSection.getInitiative().getId(), modelSection.getId());		
			break;
			
		case MODEL_SECTION_MOVED:
			title = "moved the section " + getModelSectionAnchor(modelSection);
			message = "from " + getModelSectionAnchor(fromSection) + 
				" to " + getModelSectionAnchor(onSection);
			url = getModelSectionUrl(modelSection.getInitiative().getId(), modelSection.getId());		
			break;
			
		case MODEL_SECTION_REMOVED:
			title = "removed the section " + getModelSectionAnchor(modelSection) + 
					" from " + getModelSectionAnchor(fromSection);
			message = "";
			url = getModelSectionUrl(modelSection.getInitiative().getId(), modelSection.getId());
			break;
			
		case MODEL_CARDWRAPPER_ADDED:
			title = "added the card " + getModelCardWrapperAnchor(modelCardWrapper, onSection) + 
					" under section " + getModelSectionAnchor(onSection);
			message = "";
			url = getModelCardWrapperUrl(modelCardWrapper.getInitiative().getId(), onSection.getId(), modelCardWrapper.getId());
			break;
			
		case MODEL_CARDWRAPPER_MOVED:
			title = "moved the card " + getModelCardWrapperAnchor(modelCardWrapper, onSection) + 
					" from " + getModelSectionAnchor(fromSection) + 
					" to " + getModelSectionAnchor(onSection);
			message = "";
			url = getModelCardWrapperUrl(modelCardWrapper.getInitiative().getId(), onSection.getId(), modelCardWrapper.getId());
			break;
			
		case MODEL_CARDWRAPPER_REMOVED:
			title = "removed the card " + getModelCardWrapperAnchor(modelCardWrapper, fromSection) + 
					" from section " + getModelSectionAnchor(fromSection);
			message = "";
			url = getModelCardWrapperUrl(modelCardWrapper.getInitiative().getId(), modelCardWrapper.getId());
			break;
			
		case MESSAGE_POSTED:
			String from = "CollectiveOne";
			if(modelCardWrapper != null) {
				
				if (onSection != null ) {
					from = getModelCardWrapperAnchor(modelCardWrapper, notification.getActivity().getOnSection()) + " card";	
				} else {
					from = getModelCardWrapperName(modelCardWrapper) + " card";
				}
				
				url = getModelCardWrapperUrl(modelCardWrapper.getInitiative().getId(), modelCardWrapper.getId());
				
			} else if (notification.getActivity().getModelSection() != null) {
				
				from = getModelSectionAnchor(modelSection) + " section";
				url = getModelSectionUrl(modelSection.getInitiative().getId(), modelSection.getId());
				
			} else if (notification.getActivity().getInitiative() != null) {
				
				from = getInitiativeAnchor(initiative) + " initiative";
				url = getInitiativeUrl(initiative.getId());
			}
			
			String text = notification.getActivity().getMessage().getText();
			
			if (act.getMentionedUsers().contains(notification.getSubscriber().getUser())) {
				/* mentioned to user */
				title = "mentioned you in a comment in " + from;
			} else {
				title = "commented in " + from;
			}
			
			String textLimited = "";
			if (text != null) {
				textLimited = isHtml ? text : (text.length() > 60 ? text.substring(0, 60) + "..." : text);	
			}
			message = textLimited;
			
			break;
			
		case CONSENT_STATUS_OPENED:
			title = "started a consent process on card " + getModelCardWrapperAnchor(modelCardWrapper, onSection);
			message = "";
			url = getModelCardWrapperUrl(modelCardWrapper.getInitiative().getId(), modelCardWrapper.getId());
			break;
			
		case CONSENT_STATUS_CLOSED:
			title = "closed a consent process on card " + getModelCardWrapperAnchor(modelCardWrapper, onSection);
			message = "";
			url = getModelCardWrapperUrl(modelCardWrapper.getInitiative().getId(), modelCardWrapper.getId());
			break;
			
		case CONSENT_POSITION_STATED:
		case CONSENT_POSITION_CHANGED:
			title = "decided to " + getPositionText(act.getPositionColor()) + " the card " + getModelCardWrapperAnchor(modelCardWrapper, onSection);
			message = "";
			url = getModelCardWrapperUrl(modelCardWrapper.getInitiative().getId(), modelCardWrapper.getId());
			break;
			
		case CONSENT_STATUS_REOPENED:
			title = "reopened a consent process on card " + getModelCardWrapperAnchor(modelCardWrapper, onSection);
			message = "";
			url = getModelCardWrapperUrl(modelCardWrapper.getInitiative().getId(), modelCardWrapper.getId());
			break;
			
		default:
			title = "CollectiveOne";
			message = "You have received a notification";
			url = "http://www.collectiveone.org";
			break;
		
		}

		NotificationDto dto = new NotificationDto();
		
		dto.setId(notification.getId().toString());
		dto.setSubscriber(notification.getSubscriber().toDto());
		dto.setActivity(notification.getActivity().toDto());
		
		dto.setInAppState(notification.getInAppState().toString());
		dto.setPushState(notification.getPushState().toString());
		dto.setEmailNowState(notification.getEmailNowState().toString());
		dto.setEmailSummaryState(notification.getEmailSummaryState().toString());
		
		dto.setTitle(title);
		dto.setMessage(message);
		dto.setUrl(url);
		dto.setIsHtml(isHtml);
		
		return dto;
	}
	
	private String getInitiativeUrl(UUID initiativeId) {
		return env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
				initiativeId.toString() + "/overview";
	}
	
	private String getAssignationUrl(UUID initiativeId, UUID assignationId) {
		return env.getProperty("collectiveone.webapp.baseurl") +"/#/app/inits/" + 
				initiativeId.toString() + "/assignations/" + assignationId.toString();
	}
	
	private String getModelSectionUrl(UUID initiativeId, UUID sectionId) {
		return env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
				initiativeId.toString() + "/model/section/" + 
				sectionId.toString();
	}
	
	private String getModelCardWrapperUrl(UUID initiativeId, UUID sectionId, UUID cardWrapperId) {
		return env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
				initiativeId.toString() + "/model/section/" + 
				sectionId.toString() + "/cards/" + cardWrapperId.toString();
	}
	
	private String getModelCardWrapperUrl(UUID initiativeId, UUID cardWrapperId) {
		return env.getProperty("collectiveone.webapp.baseurl") + "/#/app/inits/" + 
				initiativeId.toString() + "/model/card/" + cardWrapperId.toString();
	}
	
	private String getInitiativeAnchor(Initiative initiative) {
		return checkHtml("<a href=" + getInitiativeUrl(initiative.getId()) + ">") +  initiative.getMeta().getName() + checkHtml("</a>");
	}
	
	private String getAssignationAnchor(Assignation assignation) {
		
		return checkHtml("<a href=" + getAssignationUrl(assignation.getInitiative().getId(), assignation.getId()) + ">") + "transfer" + checkHtml("</a>");
	}
	
	private String getTransferString(List<InitiativeTransfer> transfers) {
		if (transfers.size() > 0) {
			return NumberFormat.getNumberInstance(Locale.US).format(transfers.get(0).getValue()) + " " + transfers.get(0).getTokenType().getName();
		} else {
			return "";
		}
	}

	private String getModelSectionAnchor(ModelSection section) {
		return checkHtml("<a href=" + getModelSectionUrl(section.getInitiative().getId(), section.getId()) + ">") + section.getTitle() + checkHtml("</a>");
	}
	
	private String getModelCardWrapperAnchor(ModelCardWrapper cardWrapper, ModelSection onSection) {
		return checkHtml("<a href=" + getModelCardWrapperUrl(cardWrapper.getInitiative().getId(), onSection.getId(), cardWrapper.getId()) + ">") + getModelCardWrapperName(cardWrapper) + checkHtml("</a>");
	}
	
	private String getModelCardWrapperAnchor(ModelCardWrapper cardWrapper) {
		return checkHtml("<a href=" + getModelCardWrapperUrl(cardWrapper.getInitiative().getId(),cardWrapper.getId()) + ">") + getModelCardWrapperName(cardWrapper) + checkHtml("</a>");
	}
	
	private String getModelCardWrapperName(ModelCardWrapper cardWrapper) {
		if (!cardWrapper.getCard().getTitle().equals("")) {
			return cardWrapper.getCard().getTitle();
		} else {
			String text = cardWrapper.getCard().getText();
			return text.length() > 32 ? text.substring(0, 31) + "..." : text; 
		}
	}
	
	private String getPositionText(ElementConsentPositionColor color) {
		switch (color) {
		case RED:
			return "block";
		
		case YELLOW:
			return "be neutral on";
			
		case GREEN:
			return "support";
		}
		
		return "";
	}
	
	
}
