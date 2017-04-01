package org.collectiveone.services;

import org.collectiveone.repositories.ActivityRepository;
import org.collectiveone.repositories.ArgumentRepository;
import org.collectiveone.repositories.AuthorizedEmailRepository;
import org.collectiveone.repositories.AuthorizedProjectRepository;
import org.collectiveone.repositories.BidRepository;
import org.collectiveone.repositories.CbtionRepository;
import org.collectiveone.repositories.CommentRepository;
import org.collectiveone.repositories.ContributorRepository;
import org.collectiveone.repositories.DecisionRealmDao;
import org.collectiveone.repositories.DecisionRepository;
import org.collectiveone.repositories.EditionProposalRepository;
import org.collectiveone.repositories.GoalRepository;
import org.collectiveone.repositories.ProjectRepository;
import org.collectiveone.repositories.PromoterRepository;
import org.collectiveone.repositories.ReviewRepository;
import org.collectiveone.repositories.ThesisRepository;
import org.collectiveone.repositories.UserRepository;
import org.collectiveone.repositories.VoterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;

class BaseService {
	
	/* REPOSITORIES */
	
	@Autowired
	protected GoalRepository goalRepository;

	@Autowired
	protected ProjectRepository projectRepository;
	
	@Autowired
	protected ThesisRepository thesisRepository;

	@Autowired 
	protected VoterRepository voterRepository;
	
	@Autowired 
	protected DecisionRealmDao decisionRealmRepository;
	
	@Autowired
	protected ActivityRepository activityRepository;
	
	@Autowired 
	protected ArgumentRepository argumentRepository;
	
	@Autowired
	protected UserRepository userRepository;
	
	@Autowired
	protected DecisionRepository decisionRepository;
	
	@Autowired
	protected CbtionRepository cbtionRepository;
	
	@Autowired
	protected BidRepository bidRepository;
	
	@Autowired
	protected AuthorizedEmailRepository authorizedEmailRepository;
	
	@Autowired 
	protected PromoterRepository promoterRepository;
	
	@Autowired 
	protected CommentRepository commentRepository;
	
	@Autowired 
	protected ContributorRepository contributorRepository;
	
	@Autowired
	protected AuthorizedProjectRepository authorizedProjectRepository;
	
	@Autowired 
	protected ReviewRepository reviewRepository;
	
	@Autowired 
	protected EditionProposalRepository editionProposalRepository;
	
	
	/* SERVICES */
	
	@Autowired
	protected ActivityServiceImp activityService;
		
	@Autowired
	protected AppMailServiceHeroku mailService;
	
	@Autowired
	protected SlackServiceImp slackService;
	
	@Autowired
	protected ContributorServiceImp contributorService;
	
	@Autowired
	protected ProjectServiceImp projectService;
	
	@Autowired
	protected VoterServiceImp voterService;
	
	@Autowired
	protected GoalServiceImp goalService;
	
	@Autowired
	protected BidServiceImp bidService;
	
	@Autowired
	protected UserServiceImp userService;
	
	@Autowired
	protected DecisionRealmServiceImp decisionRealmService;
	
	@Autowired
	protected TimeServiceIf timeService;
	
	
	
	
	/* SYSTEM */
	@Autowired
	protected Environment env;
	
	@Autowired
	protected ApplicationEventPublisher eventPublisher;

	
	
	/* */
	public TimeServiceIf getTimeService() {
		return timeService;
	}

	public void setTimeService(TimeServiceIf timeService) {
		this.timeService = timeService;
	}
	
	
	

}
