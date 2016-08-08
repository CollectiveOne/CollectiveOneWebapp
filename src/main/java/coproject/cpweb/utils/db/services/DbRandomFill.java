package coproject.cpweb.utils.db.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import coproject.cpweb.utils.db.entities.*;

@Service
public class DbRandomFill extends DbServicesImp {

	private Random random_engine;

	private int min_users = 5;
	private int max_users = 20;
	private int max_projects = 5;
	private int max_bids = 6;
	private double voter_ratio_max = 0.7;
	
	public void setRandom_engine(Random random_engine) {
		this.random_engine = random_engine;
	}

	public int get_random_int(int min, int max) {
		return random_engine.nextInt(max - min + 1) + min;
	}

	public List<Integer> get_random_list(int min, int max, int size) {
		// Can't create different numbers if size is too large
		if((max - min) < size - 1) {
			size = max - min;
		}

		List<Integer> array = new ArrayList<Integer>();

		while(array.size() < size) {
			int n = get_random_int(min,max);
			if(!array.contains(n)) {
				array.add(n);
			}
		}

		return array;
	}

	public double get_random_double_normal(double mean, double std) {
		return mean + std*random_engine.nextGaussian();
	}

	public double get_random_double_uniform(double min, double max) {
		return min + (max - min)*random_engine.nextDouble();
	}

	@Transactional
	public void randomUsers() {

		int nusers = this.get_random_int(min_users,max_users);

		List<User> users = new ArrayList<User>();

		for(int ix = 0; ix < nusers; ix++) {
			User user = new User();

			user.setUsername("username" + (ix + 1));
			user.setPassword("password" + (ix + 1));
			user.setEmail("email" + (ix + 1) + "@mail.com");
			user.setFirstname("FirstName" + (ix + 1));
			user.setLastname("LastName" + (ix + 1));
			user.setJoindate(new Timestamp(System.currentTimeMillis()));

			users.add(user);
		}

		for ( int ix=0; ix<users.size(); ix++ ) {
			userDao.save(users.get(ix));
		}
	}

	@Transactional
	public void randomProjects() {

		int nprojects = get_random_int(1,max_projects);

		List<User> users = userDao.getAll(1000);

		for ( int ix_proj = 0; ix_proj<nprojects; ix_proj++ ) {
			
			Project project = new Project();
			projectDao.save(project);

			project.setName("ProjectName" + (ix_proj + 1));

			User proj_creator = userDao.get(this.get_random_int(1,users.size()));

			project.setCreator(proj_creator);
			project.setCreationDate(new Timestamp(System.currentTimeMillis()));
			project.getContributors().add(proj_creator);
			
			/* One decision realm is created for each project */
			DecisionRealm realm = new DecisionRealm();
			realm.setProject(project);
			
			decisionRealmDao.save(realm);
			sessionDao.flush();
		}
	}
	
	@Transactional
	public void addOpenCbtionToProject(int projectId) {
		
		Project project = projectDao.get(projectId);
		
		Cbtion cbtion = new Cbtion();
		cbtionDao.save(cbtion);

		cbtion.setProject(project);
		project.getCbtions().add(cbtion);

		int n_users = userGetN();
		
		User cbtion_creator = userDao.get(get_random_int(1,n_users));
		userDao.save(cbtion_creator);

		cbtion.setCreator(cbtion_creator);
		cbtion_creator.getCbtionsCreated().add(cbtion);

		cbtion.setCreationDate(new Timestamp(System.currentTimeMillis()));
		cbtion.setTitle("Improve CoProjects" );
		cbtion.setDescription("Lorem ipsum dolor sit amet, eu illum facilis eam, eu mei regione vivendo lobortis. Usu te epicuri consetetur posidonium. Usu in tritani voluptaria quaerendum. Feugiat pertinax nominati qui te, nam ea doctus debitis, ius vide minim possit id.");
		cbtion.setRelevance(get_random_int(-1000,1000));
		cbtion.setState(CbtionState.OPEN);

		int nbids = get_random_int(0,max_bids);

		for(int ix_bid = 0; ix_bid < nbids ; ix_bid++) {

			Bid bid = new Bid();
			bidDao.save(bid);

			User bid_creator = userDao.get(get_random_int(1,n_users));

			bid.setCbtion(cbtion);
			cbtion.getBids().add(bid);

			bid.setCreator(bid_creator);
			bid.setCreationDate(new Timestamp(System.currentTimeMillis()));
			bid.setPpoints(get_random_double_normal(1000,150));
			bid.setDescription("Bid to cbtion:"+bid.getCbtion().getId()+" from project:"+bid.getCbtion().getProject().getId());
			bid.setState(BidState.OFFERED);

			DecisionRealm realm = decisionRealmDao.getFromProjectId(project.getId());
			decisionRealmDao.save(realm);
			
			Decision assign_bid = new Decision();
			decisionDao.save(assign_bid);

			Decision accept_bid = new Decision();
			decisionDao.save(accept_bid);

			bid.setAssign(assign_bid);
			bid.setAccept(accept_bid);

			assign_bid.setCreationDate(new Timestamp(System.currentTimeMillis()));
			assign_bid.setDescription("assign bid "+bid.getId()+" of cbtion:"+bid.getCbtion().getId()+" to:"+bid.getCreator().getUsername());
			assign_bid.setState(DecisionState.OPEN);
			assign_bid.setDecisionRealm(realm);
			
			accept_bid.setCreationDate(new Timestamp(System.currentTimeMillis()));
			accept_bid.setDescription("accept bid "+bid.getId()+" of cbtion:"+bid.getCbtion().getId()+" to:"+bid.getCreator().getUsername());
			accept_bid.setState(DecisionState.OPEN);
			accept_bid.setDecisionRealm(realm);
			
			sessionDao.flush();
		}
	}
	
	@Transactional
	public void addAcceptedCbtionToUser(int projectId, int userId) {
		
		Project project = projectDao.get(projectId);
		User user = userDao.get(userId);
		
		Cbtion cbtion = new Cbtion();
		cbtionDao.save(cbtion);

		cbtion.setProject(project);
		project.getCbtions().add(cbtion);
		projectDao.save(project);

		User cbtion_creator = userDao.get(get_random_int(1,userGetN()));

		cbtion.setCreator(cbtion_creator);
		cbtion_creator.getCbtionsCreated().add(cbtion);
		userDao.save(cbtion_creator);

		cbtion.setCreationDate(new Timestamp(System.currentTimeMillis()));
		cbtion.setTitle("Dummy to user " + user.getUsername());
		cbtion.setDescription("Created to give user ppoints");
		cbtion.setRelevance(get_random_int(-1000,1000));

		Bid bid = new Bid();
		bidDao.save(bid);

		bid.setCbtion(cbtion);
		cbtion.getBids().add(bid);

		bid.setCreator(user);
		bid.setCreationDate(new Timestamp(System.currentTimeMillis()));
		bid.setPpoints(get_random_double_normal(1000,150));
		bid.setDescription("Dummy bid from project:"+bid.getCbtion().getProject().getId()+" to be accepted to user "+ user.getUsername());
		bid.setState(BidState.OFFERED);

		DecisionRealm realm = decisionRealmDao.getFromProjectId(project.getId());
		decisionRealmDao.save(realm);
		
		Decision assign_bid = new Decision();
		decisionDao.save(assign_bid);

		Decision accept_bid = new Decision();
		decisionDao.save(accept_bid);

		bid.setAssign(assign_bid);
		bid.setAccept(accept_bid);

		assign_bid.setCreationDate(new Timestamp(System.currentTimeMillis()));
		assign_bid.setDescription("assign bid "+bid.getId()+" of cbtion:"+bid.getCbtion().getId()+" to:"+bid.getCreator().getUsername());
		assign_bid.setState(DecisionState.CLOSED_ACCEPTED);
		assign_bid.setDecisionRealm(realm);
		
		accept_bid.setCreationDate(new Timestamp(System.currentTimeMillis()));
		accept_bid.setDescription("accept bid "+bid.getId()+" of cbtion:"+bid.getCbtion().getId()+" to:"+bid.getCreator().getUsername());
		accept_bid.setState(DecisionState.CLOSED_ACCEPTED);
		accept_bid.setDecisionRealm(realm);

		/* simulate the bid acceptance process */
		bid.setState(BidState.ACCEPTED);

		cbtion.setAssignedPpoints(bid.getPpoints());
		cbtion.setContributor(bid.getCreator());
		cbtion.setState(CbtionState.ACCEPTED);

		user.getCbtionsAccepted().add(cbtion);
		userDao.addProjectContributed(user.getId(),project.getId());
		
		project.getCbtionsAccepted().add(cbtion);
		projectDao.addContributor(user.getId(), project.getId());
		
		/* add user to decision realm */
		voterDao.updateOrAdd(realm.getId(), user.getId(), bid.getPpoints());
		
		sessionDao.flush();
	}

	


	@Transactional
	public void randomProjectsFollowed() {
		// ==================
		// Add projects (being followed) to each user
		List<User> users = userDao.getAll(100);
		List<Project> projects = projectDao.getAll(100);

		for(User user:users) {

			userDao.save(user);

			int nproj = get_random_int(1,projects.size());
			List<Integer> ixs_proj = get_random_list(0,projects.size()-1,nproj);

			for(Integer ix_proj:ixs_proj) {
				Project this_project = projectDao.get(projects.get(ix_proj).getId());
				user.getProjectsFollowing().add(this_project);
			}
		}
	} 

	@Transactional
	public void randomBidAssignTheses() {

		List<Bid> bids = bidDao.getAll(10000);

		// Simulate assign votes
		for(Bid bid : bids) {

			if(bid.getState() != BidState.ACCEPTED) {
				Project project = bid.getCbtion().getProject();
				DecisionRealm realm = decisionRealmDao.getFromProjectId(project.getId());
				
				int realmSize = realm.getVoters().size();
				int n_voters = (int)Math.round(realmSize*get_random_double_uniform(0, voter_ratio_max));
				List<Integer> ixs_voters = get_random_list(0,realmSize-1,n_voters);

				for(Integer ix_voter : ixs_voters) {
					User voterUser = realm.getVoters().get(ix_voter).getVoterUser();
					
					Thesis thesis = new Thesis();

					thesis.setAuthor(voterUser);
					thesis.setCastDate(new Timestamp(System.currentTimeMillis()));

					double p = get_random_double_uniform(0,1);
					int value = 0;
					if(p > 0.2) value = 1;
					
					thesisAssignOfBidSave(voterUser,value,bid.getId());
					
					sessionDao.flush();
				}
			}
		}
	}
	
	@Transactional
	public void randomBidAcceptTheses() {

		List<Bid> bids = bidDao.getAll(10000);

		// Simulate assign votes
		for(Bid bid : bids) {

			if(bid.getState() == BidState.ASSIGNED) {
				Project project = bid.getCbtion().getProject();
				List<User> contributors = project.getContributors();
				
				int voters_num = (int)Math.round(contributors.size()*get_random_double_uniform(0, voter_ratio_max));
				List<Integer> ixs_voters = get_random_list(0,contributors.size()-1,voters_num);

				for(Integer ix_voter : ixs_voters) {
					User voterUser = contributors.get(ix_voter);
					
					Thesis thesis = new Thesis();

					thesis.setAuthor(voterUser);
					thesis.setCastDate(new Timestamp(System.currentTimeMillis()));

					double p = get_random_double_uniform(0,1);
					int value = 0;
					if(p > 0.2) value = 1;

					thesisAcceptOfBidSave(voterUser,value,bid.getId());
					
					sessionDao.flush();
				}
			}
		}
	}
}
