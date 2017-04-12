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
	/* p-points that can vote */
	public double weightTot;
	/* number of voters that have voted */
	public int n;
	/* Estimation of p */
	public double pest;
	/* p-points that have voted */
	public double weightCum;
	/* Time elapsed factor */
	public double elapsedFactor = 0.0;
	
	/* Verdict triggering thresholds */
	@Transient
	public double p_to_flip = 0.5;
	@Transient
	public double pc_low = 0.0;
	@Transient
	public double pc_high = 1.0;
	@Transient
	public double shrinkFactor = 0.0;

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
		if(goal != null) dto.setGoalTag(goal.getGoalTag());
		if(arguments != null) dto.setNarguments(arguments.size());
		
		if(type != null) { 
			dto.setType(type.toString());
			switch(type) {
				case CBTION:
					if(affectedCbtion != null) {
						dto.setAffectedCbtionId(affectedCbtion.getId());
						dto.setAffectedCbtionTitle(affectedCbtion.getTitle());
					}
					break;
					
				case GOAL:
					if(affectedGoal != null) {
						dto.setAffectedGoalId(affectedGoal.getId());
						dto.setAffectedGoalTag(affectedGoal.getGoalTag());
					}
					break;
					
				case BID:
					if(affectedBid != null) {
						dto.setAffectedBidId(affectedBid.getId());
						dto.setAffectedBidCreatorUsername(affectedBid.getCreator().getUsername());
						dto.setAffectedCbtionId(affectedBid.getCbtion().getId());
						dto.setAffectedCbtionTitle(affectedBid.getCbtion().getTitle());
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
		dto.setPpsCum(weightCum);
		dto.setPest(pest);
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
	public void updateState(Timestamp now, double weightTotIn) {

		if (state == DecisionState.IDLE) {
			// force open the decision after the first vote
			if(thesesCast.size() > 0) {
				state = DecisionState.OPEN;
				openDate = new Timestamp(System.currentTimeMillis());
			}
		}
		
		if (state == DecisionState.OPEN) {
			
			n = thesesCast.size();
			
			weightTot = weightTotIn;
			
			if (n > 0) {
				
				updatePcum();
				
				if(weightCum > 0) {
					
					updateP();
					updateVerdict();
					
					if (isVerdictTime(now)) {
						if (verdict == 0)
							state = DecisionState.CLOSED_DENIED;
						if (verdict == 1)
							state = DecisionState.CLOSED_ACCEPTED;
						
						actualVerdictDate = new Timestamp(System.currentTimeMillis());
					}
				} else {
					/* back to idle if the voter had zero weight */
					state = DecisionState.IDLE;
				}
			}
		}
	}

	public void updatePcum() {
		weightCum = 0.0;
		for (Thesis thesis : thesesCast) {
			weightCum += thesis.getWeight();
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

	public boolean isVerdictTime(Timestamp now) {
		/*
		 * get the value p_to_flip of p which would flip the outcome of the
		 * decision, if it is obtained with the remaining votes
		 */
		double pps_left = weightTot - weightCum;

		if(pps_left > 0) {
			p_to_flip = -0.1;
			if (pps_left != 0) {
				p_to_flip = (0.5 - pest * weightCum / weightTot) * weightTot / pps_left;
			}

			if((p_to_flip >= 0) && (p_to_flip <= 1)) {
				/*
				 * 	# confidence interval for p_est is assumed to have the
    				# size of the proportion of pps_left/pps_tot
				 */
				
				double pc_range_sz = pps_left/weightTot;
				
				/* extend/contract the confidence intervals based on the time passed */
				double elapsedHours = (now.getTime() - openDate.getTime())
						/ (1000.0 * 60.0 * 60.0);
				
				elapsedFactor = elapsedHours / verdictHours;

				/*
				 * extFactor goes from 1 to -1, passing through 0 when elapsedFactor is
				 * 0.5 which is when elapsedHours are half the verdictHours
				 */
				shrinkFactor = (1 - elapsedFactor);
				if(shrinkFactor < 0) {
					shrinkFactor = 0;
				}

				double pc_range_sz_time = pc_range_sz*shrinkFactor;
				
				/*
				# it should be centered in the current value of p_est, but stopts
			    # at the [0,1] edges
			    */
				
				pc_low =  0.0;
				pc_high =  1.0;
				
			    if(1-pest < pc_range_sz_time/2) {
			      /* # touches upper edge */
			      pc_low = 1 - pc_range_sz_time;
			      pc_high = 1;
			        
			    } else {
			      if(pest < pc_range_sz_time/2) {
			        /* # touches lower edge */
			        pc_low = 0;
			        pc_high = pc_range_sz_time;
			        
			      } else {
			        /* # does not touces any edge */
			        pc_low = pest - pc_range_sz_time/2;
			        pc_high = pest + pc_range_sz_time/2;
			      }
			    }

				/* make the test on p_to_flip */
				if ((p_to_flip <= pc_low)
						|| (pc_high <= p_to_flip)) {
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
		if (pest > 0.5) {
			verdict = 1;
		} else {
			verdict = 0;
		}
	}
}
