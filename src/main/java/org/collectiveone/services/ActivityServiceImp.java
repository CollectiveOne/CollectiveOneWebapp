package org.collectiveone.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.collectiveone.model.Activity;
import org.collectiveone.web.dto.ActivityDto;
import org.collectiveone.web.dto.ActivityDtoListRes;
import org.collectiveone.web.dto.Filters;
import org.collectiveone.web.dto.ObjectListRes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivityServiceImp extends BaseService {
		
	@Transactional
	public ActivityDtoListRes activityDtoGetFiltered(Filters filters) {
		ObjectListRes<Activity> activityRes = activityRepository.get(filters);

		ActivityDtoListRes activityDtosRes = new ActivityDtoListRes();

		activityDtosRes.setResSet(activityRes.getResSet());
		activityDtosRes.setActivityDtos(new ArrayList<ActivityDto>());

		for(Activity activity : activityRes.getObjects()) {
			activityDtosRes.getActivityDtos().add(activity.toDto());
		}

		return activityDtosRes;
	}
	
	public void activitySaveAndNotify(Activity act) throws IOException {
		activityRepository.save(act);
		
		/* send emails */
		String subject = "CollectiveOne - "+act.getProject().getName()+" activity";
	    String body = act.getPrettyMessage(env.getProperty("collectiveone.webapp.baseurl"));
	    
	    List<String> subscribedUsers = mailSubscriptionRepository.getSubscribedAddresses(act.getProject().getId());
	    
	    mailService.sendMail(
	    		subscribedUsers, 
	    		subject, 
	    		body);
	  
	    slackService.sendToSlack(act.getPrettyMessage(env.getProperty("collectiveone.webapp.baseurl")));
	}

}
