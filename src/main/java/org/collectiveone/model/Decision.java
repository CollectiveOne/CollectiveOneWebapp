package org.collectiveone.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.collectiveone.web.dto.DecisionDtoFull;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "DECISIONS")
public class Decision {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String description;
	private Timestamp creationDate;
	private String fromState;
	private String toState;
	@ManyToOne
	private Project project = new Project();
	@ManyToOne
	private User creator;

	@ManyToOne(cascade = CascadeType.ALL)
	private DecisionRealm decisionRealm;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Goal goal;

	/* weights from decision realm are mixed with pps of the voter
	 * in the project to get the weight of each voter. This weight
	 * is stored in the theses (votes) elements.*/
	@OneToMany(mappedBy="decision", cascade=CascadeType.ALL)
	private List<Thesis> thesesCast = new ArrayList<Thesis>();
	private Timestamp openDate;
	private Timestamp actualVerdictDate;
	
	private double verdictHours = 36;
	private int verdict;
	private DecisionState state;
	
	@OneToMany(mappedBy="decision", cascade=CascadeType.ALL)
	private List<Argument> arguments = new ArrayList<Argument>();
	
	/* hold a reference to the affected object */
	private DecisionType type;
	@ManyToOne
	private Cbtion affectedCbtion;
	@ManyToOne
	private Goal affectedGoal;
	@ManyToOne
	private Bid affectedBid;
	
	/* ============================== */
	/* Decisions mechanics parameters */
	public double ci = 0.95;
	public double stab_ratio = 0.5;

	/* Decisions statistics variables */

	/* p-points that can vote */
	public double weightTot;
	/* number of voters that have voted */
	public int n;
	/* Estimation of p */
	public double pest;
	/* p-points that have voted */
	public double ppsCum;
	/* Stability of the decision */
	public double stability;
	/* Clarity of the decision */
	public double clarity;
	/* Minimum  number of votes neeeded to compute the stabilty */
	public int n_min_stab = 8;
	/* Likelihood that should be rejected */
	public double l0 = 1.0;
	/* Likelihood that should be accepted */
	public double l1 = 0.0;
	/* Time elapsed factor */
	public double elapsedFactor = 0.0;
	
	/* Veridc tirggering thresholds */
	@Transient
	public double p_to_flip = 0.5;
	@Transient
	public double pc_ci_low = 0.0;
	@Transient
	public double pc_ci_high = 1.0;
	@Transient
	public double pc_ci_low_ext = 0.0;
	@Transient
	public double pc_ci_high_ext = 1.0;
	@Transient
	public double pc_ci_low_ext_time = 0.0;
	@Transient
	public double pc_ci_high_ext_time = 1.0;
	@Transient
	public double extFactor = 0.0;

	/* ============================== */
	public DecisionDtoFull toDto() {

		DecisionDtoFull dto = new DecisionDtoFull();

		dto.setId(id);
		dto.setCreatorUsername(creator.getUsername());
		dto.setDescription(description);
		if(creationDate != null) dto.setCreationDate(creationDate.getTime());
		if(openDate != null) dto.setOpenDate(openDate.getTime());
		if(actualVerdictDate != null) dto.setActualVerdictDate(actualVerdictDate.getTime());
		dto.setFromState(fromState);
		dto.setToState(toState);
		if(project != null) dto.setProjectName(project.getName());
		if(arguments != null) dto.setNarguments(arguments.size());
		
		if(type != null) { 
			dto.setType(type.toString());
			switch(type) {
				case CBTION:
					if(affectedCbtion != null) {
						dto.setCbtionId(affectedCbtion.getId());
						dto.setCbtionTitle(affectedCbtion.getTitle());
					}
					break;
					
				case GOAL:
					if(affectedGoal != null) {
						dto.setGoalId(affectedGoal.getId());
						dto.setGoalTag(affectedGoal.getGoalTag());
					}
					break;
					
				case BID:
					if(affectedBid != null) {
						dto.setBidId(affectedBid.getId());
						dto.setBidCreatorUsername(affectedBid.getCreator().getUsername());
						dto.setCbtionId(affectedBid.getCbtion().getId());
						dto.setCbtionTitle(affectedBid.getCbtion().getTitle());
					}
					break;
				
				case GENERAL:
					break;
	
			}
		}
		
		dto.setnVoters(decisionRealm.size());
		dto.setPpsTot(weightTot);
		dto.setVerdictHours(verdictHours);
		dto.setVerdict(verdict);
		dto.setState(state.toString());
		dto.setnVotesCasted(thesesCast.size());
		dto.setPpsCum(ppsCum);
		dto.setPest(pest);
		dto.setStability(stability);
		dto.setClarity(clarity);
		
		double log_l1l0 = 0.0;
		if((l1 == 0) && (l0 != 0)) log_l1l0 = -111.0; 
		if((l1 != 0) && (l0 == 0)) log_l1l0 = +111.0;
		if((l1 == 0) && (l0 != 0)) log_l1l0 = 0;
		if((l1 != 0) && (l0 != 0)) log_l1l0 = Math.log10(l1)-Math.log10(l0);
			
		dto.setLog_l1l0(log_l1l0);
		dto.setElapsedFactor(elapsedFactor);
		
		return dto;
	}

	/* getters/setters */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	public String getFromState() {
		return fromState;
	}
	public void setFromState(String fromState) {
		this.fromState = fromState;
	}
	public String getToState() {
		return toState;
	}
	public void setToState(String toState) {
		this.toState = toState;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}
	public DecisionRealm getDecisionRealm() {
		return decisionRealm;
	}
	public void setDecisionRealm(DecisionRealm decisionRealm) {
		this.decisionRealm = decisionRealm;
	}
	public Goal getGoal() {
		return goal;
	}
	public void setGoal(Goal goal) {
		this.goal = goal;
	}
	public Timestamp getOpenDate() {
		return openDate;
	}
	public void setOpenDate(Timestamp openDate) {
		this.openDate = openDate;
	}
	public Timestamp getActualVerdictDate() {
		return actualVerdictDate;
	}
	public void setActualVerdictDate(Timestamp actualVerdictDate) {
		this.actualVerdictDate = actualVerdictDate;
	}

	public List<Thesis> getThesesCast() {
		return thesesCast;
	}
	public void setThesesCast(List<Thesis> thesesCast) {
		this.thesesCast = thesesCast;
	}
	public double getVerdictHours() {
		return verdictHours;
	}
	public void setVerdictHours(double verdictHours) {
		this.verdictHours = verdictHours;
	}
	public int getVerdict() {
		return verdict;
	}
	public void setVerdict(int verdict) {
		this.verdict = verdict;
	}
	public DecisionState getState() {
		return state;
	}
	public void setState(DecisionState state) {
		this.state = state;
	}
	public List<Argument> getArguments() {
		return arguments;
	}
	public void setArguments(List<Argument> arguments) {
		this.arguments = arguments;
	}
	public Cbtion getAffectedCbtion() {
		return affectedCbtion;
	}
	public void setAffectedCbtion(Cbtion affectedCbtion) {
		this.affectedCbtion = affectedCbtion;
	}
	public Goal getAffectedGoal() {
		return affectedGoal;
	}
	public void setAffectedGoal(Goal affectedGoal) {
		this.affectedGoal = affectedGoal;
	}
	public Bid getAffectedBid() {
		return affectedBid;
	}
	public void setAffectedBid(Bid affectedBid) {
		this.affectedBid = affectedBid;
	}
	public DecisionType getType() {
		return type;
	}
	public void setType(DecisionType type) {
		this.type = type;
	}

	/*
	 * Decision Engine logic
	 */
	public void updateState(Timestamp now) {

		if (state == DecisionState.IDLE) {
			// force open the decision after the first vote
			if(thesesCast.size() > 0) {
				state = DecisionState.OPEN;
				openDate = new Timestamp(System.currentTimeMillis());
			}
		}
		
		if (state == DecisionState.OPEN) {
			
			n = thesesCast.size();
			
			/*TODO: inefficient to compute at every update... */
			weightTot = decisionRealm.getWeightTot();
			
			if (n > 0) {
				
				updatePcum();
				updateP();
				updateClarity();
				updateStability();

				/* determine if the decision shall be closed */
				boolean isTime = isVerdictTime(now);

				updateVerdict();
				
				if (isTime) {
					if (verdict == 0)
						state = DecisionState.CLOSED_DENIED;
					if (verdict == 1)
						state = DecisionState.CLOSED_ACCEPTED;
					
					actualVerdictDate = new Timestamp(System.currentTimeMillis());
				}
			}
		}
	}

	public void updatePcum() {
		ppsCum = 0.0;
		for (Thesis thesis : thesesCast) {
			ppsCum += thesis.getWeight();
		}
	}

	public double estimateP() {
		int[] ixs = new int[n];

		for (int ix = 0; ix < n; ix++) {
			ixs[ix] = ix;
		}

		return estimateP(ixs);
	}

	public double estimateP(int[] ixs) {
		double pest = 0;
		double weight_cum = 0;

		for (Integer ix : ixs) {
			Thesis thesis = thesesCast.get(ix);
			pest += thesis.getValue() * thesis.getWeight();
			weight_cum += thesis.getWeight();
		}

		pest = pest / weight_cum;

		return pest;
	}

	public void updateP() {
		pest = estimateP();
	}

	public void updateClarity() {
		/* inverse and scaled function of the variance, 
		 * 0 when variance is max p = 0.5 and var = 0.25
		 * 1 when variance is min p = 1 or p = 0 and var = 0 */ 
		clarity = 1-4*pest*(1-pest);
	}
	
	public void updateStability() {

		if(n >= n_min_stab) {
		
			int n_base = (int) Math.round(n * stab_ratio);
			int n_cmp = n - n_base;
	
			int[] ixs_base = new int[n_base];
			int[] ixs_cmp = new int[n_cmp];
	
			for (int ix = 0; ix < n_base; ix++)
				ixs_base[ix] = ix;
			for (int ix = 0; ix < n_cmp; ix++)
				ixs_cmp[ix] = ix;
	
			double pest_base = estimateP(ixs_base);
			double pest_cmp = estimateP(ixs_cmp);
	
			stability = pest_cmp - pest_base;
		} else {
			stability = 0.0;
		} 
	}

	public boolean isVerdictTime(Timestamp now) {
		/*
		 * get the value p_to_flip of p which would flip the outcome of the
		 * decision, if it is obtained with the remaining votes
		 */
		double pps_left = weightTot - ppsCum;

		if(pps_left > 0) {
			p_to_flip = -0.1;
			if (pps_left != 0) {
				p_to_flip = (0.5 - pest * ppsCum / weightTot) * weightTot / pps_left;
			}

			if((p_to_flip >= 0) && (p_to_flip <= 1)) {
				/*
				 * find the probability that p_to_flip will in fact be obtained fron the
				 * rest of votes, assuming they are iid Bernoulli trials with p = pest
				 */
				NormalDistribution ndist = new NormalDistribution(0.0, 1.0);

				double a = 1 - ci;
				pc_ci_low = pest - ndist.inverseCumulativeProbability(1 - a / 2)
						* Math.sqrt(pest * (1 - pest) / n);
				pc_ci_high = pest
						- ndist.inverseCumulativeProbability(a - a / 2)
						* Math.sqrt(pest * (1 - pest) / n);

				// protection for pest = 1 or pest = 0
				if (pc_ci_low == pc_ci_high) {
					pc_ci_low = 0;
					pc_ci_high = 1;
				}

				/* extend the confidence intervals based on the instability of the votes */
				pc_ci_low_ext = pc_ci_low - pc_ci_low * Math.abs(stability);
				pc_ci_high_ext = pc_ci_high + (1 - pc_ci_high)
						* Math.abs(stability);

				/* extend/contract the confidence intervals based on the time passed */
				double elapsedHours = (now.getTime() - openDate.getTime())
						/ (1000.0 * 60.0 * 60.0);
				elapsedFactor = elapsedHours / verdictHours;

				/*
				 * extFactor goes from 1 to -1, passing through 0 when elapsedFactor is
				 * 0.5 which is when elapsedHours are half the verdictHours
				 */
				extFactor = 2 * (1 - elapsedFactor) - 1;

				if (extFactor > 0) {
					/* expand towards the [0,1] borders */
					pc_ci_low_ext_time = pc_ci_low_ext - pc_ci_low_ext * extFactor;
					pc_ci_high_ext_time = pc_ci_high_ext + (1 - pc_ci_high_ext)
							* extFactor;
				} else {
					/* contract towards the center of the interval */
					double ci_mean = (pc_ci_low_ext + pc_ci_high_ext) / 2;
					double low = (ci_mean - pc_ci_low_ext) * (1 + extFactor);
					double high = (pc_ci_high_ext - ci_mean) * (1 + extFactor);
					pc_ci_low_ext_time = ci_mean - low;
					pc_ci_high_ext_time = ci_mean + high;
				}

				/* make the test on p_to_flip */
				if ((p_to_flip < pc_ci_low_ext_time)
						|| (pc_ci_high_ext_time < p_to_flip)) {
					return true;
				} else {
					return false;
				}
			} else {
				// If p to flip is not feasible, the verdict time has been reached
				return true;
			}
		} else {
			// If all pps have voted
			return true;
		}
		
	}

	public void updateVerdict() {

		l0 = 1.0;
		l1 = 1.0;

		for (int ix = 0; ix < n; ix++) {
			Thesis thesis = thesesCast.get(ix);

			/*
			 * compute the probability for this thesis of being correct
			 */
			double p0 = 0.5 * (1 + thesis.getWeight() / ppsCum);
			double p = 0.5 + clarity * (p0 - 0.5);

			/* update the likelihood for both outcomes */
			if (thesis.getValue() == 0) {
				l0 *= p;
				l1 *= (1 - p);
			} else {
				l0 *= 1 - p;
				l1 *= p;
			}
		}

		/* chose the outcome with higher likelihood */
		if (l1 > l0) {
			verdict = 1;
		} else {
			verdict = 0;
		}

	}
}
