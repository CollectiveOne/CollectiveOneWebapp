package org.collectiveone.services;

import java.sql.Timestamp;

import org.collectiveone.model.Decision;
import org.collectiveone.model.DecisionState;
import org.collectiveone.model.Thesis;
import org.collectiveone.model.User;
import org.collectiveone.model.Voter;
import org.collectiveone.web.dto.ThesisDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ThesisServiceImp extends BaseService {
	
	@Transactional
	public ThesisDto getOfUserDto(Long decId, Long userId) {
		Thesis thesis = thesisRepository.getOfUserInDec(decId, userId);
		ThesisDto thesisDto = null;
		if(thesis != null) thesisDto = thesis.toDto();
		return thesisDto;
	}

	@Transactional
	public String save(User author, int value, Long decId) {

		Decision dec = decisionRepository.get(decId);

		if(dec.getState() == DecisionState.IDLE || dec.getState() == DecisionState.OPEN) {
			/* if decision is still open */
			Voter voter = decisionRealmRepository.getVoter(dec.getDecisionRealm().getId(),author.getId());

			if(voter != null) {
				/* if voter is in the realm of the decision */
				Thesis thesis = decisionRepository.getThesisCasted(decId, author.getId());				

				if(thesis == null) {
					thesis = new Thesis();
					thesis.setAuthor(author);
					thesis.setDecision(dec);
				}
				
				thesisRepository.save(thesis);

				thesis.setValue(value);
				thesis.setCastDate(new Timestamp(System.currentTimeMillis()));

				/* weight of the thesis is set at cast time, as a copy of the realm,
				 * so theses weights need to be updated every time voter weight changes */
				thesis.setWeight(voter.getActualWeight());

				decisionRepository.save(dec);

				return "thesis saved";
			} else {
				return "voter not in decision realm";
			}
		} else {
			return "decision is not open";
		}
	}

}
