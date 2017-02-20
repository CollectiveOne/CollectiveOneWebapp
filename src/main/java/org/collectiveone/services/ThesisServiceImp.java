package org.collectiveone.services;

import java.sql.Timestamp;

import org.collectiveone.model.Bid;
import org.collectiveone.model.Cbtion;
import org.collectiveone.model.CbtionState;
import org.collectiveone.model.Decision;
import org.collectiveone.model.DecisionState;
import org.collectiveone.model.Thesis;
import org.collectiveone.model.User;
import org.collectiveone.model.Voter;
import org.collectiveone.repositories.BidDao;
import org.collectiveone.repositories.DecisionDao;
import org.collectiveone.repositories.DecisionRealmDao;
import org.collectiveone.repositories.ThesisDao;
import org.collectiveone.web.dto.ThesisDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class ThesisServiceImp {
	
	@Autowired
	protected ThesisDao thesisDao;
	
	@Autowired
	protected DecisionDao decisionDao;
	
	@Autowired 
	protected DecisionRealmDao decisionRealmDao;
	
	@Autowired
	protected BidDao bidDao;

	@Transactional
	public ThesisDto thesisOfUser(Long decId, Long userId) {
		Thesis thesis = thesisDao.getOfUserInDec(decId, userId);
		ThesisDto thesisDto = null;
		if(thesis != null) thesisDto = thesis.toDto();
		return thesisDto;
	}

	@Transactional
	public String thesisOfDecSave(User author, int value, Long decId) {

		Decision dec = decisionDao.get(decId);

		if(dec.getState() == DecisionState.IDLE || dec.getState() == DecisionState.OPEN) {
			/* if decision is still open */
			Voter voter = decisionRealmDao.getVoter(dec.getDecisionRealm().getId(),author.getId());

			if(voter != null) {
				/* if voter is in the realm of the decision */
				Thesis thesis = decisionDao.getThesisCasted(decId, author.getId());				

				if(thesis == null) {
					thesis = new Thesis();
					thesis.setAuthor(author);
					thesis.setDecision(dec);
				}
				
				thesisDao.save(thesis);

				thesis.setValue(value);
				thesis.setCastDate(new Timestamp(System.currentTimeMillis()));

				/* weight of the thesis is set at cast time, as a copy of the realm,
				 * so theses weights need to be updated every time voter weight changes */
				thesis.setWeight(voter.getActualWeight());

				decisionDao.save(dec);

				return "thesis saved";
			} else {
				return "voter not in decision realm";
			}
		} else {
			return "decision is not open";
		}
	}

	@Transactional
	public void thesisAssignOfBidSave(User author, int value, Long bidId) {
		Bid bid = bidDao.get(bidId);
		Cbtion cbtion = bid.getCbtion();
		if((cbtion.getState() == CbtionState.OPEN) || 
				(cbtion.getState() == CbtionState.ASSIGNED)) {

			thesisOfDecSave(author, value, bid.getAssign().getId());	
		}
	}

	@Transactional
	public void thesisAcceptOfBidSave(User author, int value, Long bidId) {
		Bid bid = bidDao.get(bidId);
		Cbtion cbtion = bid.getCbtion();
		if(cbtion.getState() == CbtionState.ASSIGNED) {
			thesisOfDecSave(author, value, bid.getAccept().getId());	
		}
	}

}
