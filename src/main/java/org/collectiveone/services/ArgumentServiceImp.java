package org.collectiveone.services;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.collectiveone.model.Activity;
import org.collectiveone.model.ActivityType;
import org.collectiveone.model.Argument;
import org.collectiveone.model.ArgumentTendency;
import org.collectiveone.model.Decision;
import org.collectiveone.model.User;
import org.collectiveone.web.dto.ArgumentDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArgumentServiceImp extends BaseService {
	
	@Transactional
	public ArgumentDto argumentGetDto(Long id) {
		return argumentRepository.get(id).toDto();
	}

	@Transactional
	public List<ArgumentDto> argumentGetOfDecisionDto(Long decisionId, ArgumentTendency tendency) {
		List<Argument> arguments = argumentRepository.getOfDecision(decisionId,tendency);
		List<ArgumentDto> argumentDtos = new ArrayList<ArgumentDto>();
		for (Argument argument : arguments) {
			argumentDtos.add(argument.toDto());
		}
		return argumentDtos;
	}


	@Transactional
	public String argumentCreate(ArgumentDto argumentDto) throws IOException {

		User creator = userRepository.get(argumentDto.getCreatorUsername());
		Decision decision = decisionRepository.get(argumentDto.getDecisionId());
		Argument argument = new Argument();

		argument.setCreationDate(new Timestamp(System.currentTimeMillis()));
		argument.setCreator(creator);
		argument.setDecision(decision);
		argument.setDescription(argumentDto.getDescription());
		argument.setTendency(ArgumentTendency.valueOf(argumentDto.getTendency()));

		decision.getArguments().add(argument);

		argumentRepository.save(argument);
		decisionRepository.save(decision);

		Activity act = new Activity();
		act.setCreationDate(new Timestamp(System.currentTimeMillis()));
		act.setArgument(argument);
		act.setType(ActivityType.ARGUMENT);
		act.setProject(argument.getDecision().getProject());
		act.setEvent("created");
		activityService.activitySaveAndNotify(act);

		return "argument created";
	}

	@Transactional
	public String argumentBack(Long argId, Long userId) {
		User user = userRepository.get(userId);
		return argumentRepository.back(argId,user);
	}

	@Transactional
	public String argumentUnBack(Long argId, Long userId) {
		User user = userRepository.get(userId);
		return argumentRepository.unBack(argId,user);
	}

	@Transactional
	public boolean argumentIsBacked(Long argId, Long userId) {
		if(argumentRepository.getBacker(argId,userId) == null) {
			return false;
		} else {
			return true;
		}
	}

}
