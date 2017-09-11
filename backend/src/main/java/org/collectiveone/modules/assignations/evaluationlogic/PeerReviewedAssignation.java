package org.collectiveone.modules.assignations.evaluationlogic;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.assignations.Assignation;
import org.collectiveone.modules.assignations.EvaluationGrade;
import org.collectiveone.modules.assignations.EvaluationGradeState;
import org.collectiveone.modules.assignations.EvaluationGradeType;
import org.collectiveone.modules.assignations.Evaluator;
import org.collectiveone.modules.assignations.EvaluatorState;
import org.collectiveone.modules.assignations.Receiver;
import org.collectiveone.modules.assignations.ReceiverType;

public class PeerReviewedAssignation {
	private Assignation assignation;
	private PeerReviewedAssignationState state = PeerReviewedAssignationState.OPEN;
	private List<ReceiverData> receiversData = new ArrayList<ReceiverData>();
	
	
	private int indexOfReceiver(String receiverId) {
		for(int ix = 0; ix < assignation.getReceivers().size(); ix++) {
			if(assignation.getReceivers().get(ix).getId().toString().equals(receiverId)) {
				return ix;
			}
		}
		return -1;
	}
	
	private int countPendingEvaluators() {
		int count = 0;
		for(Evaluator evaluator : assignation.getEvaluators()) {
			if(evaluator.getState() == EvaluatorState.PENDING ) {
				count++;
			}
		}
		return count;
	}
	
	public void updateState() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		
		if (state == PeerReviewedAssignationState.OPEN) {
			if(now.getTime() > assignation.getConfig().getMaxClosureDate().getTime()) {
				close();
			} else {
				if(countPendingEvaluators() == 0) {
					if(now.getTime() > assignation.getConfig().getMinClosureDate().getTime()) {
						close();
					}
				}
			}
		}
	}
	
	private void close() {
		/* First, prepare the receiversData array, then, fill it with the evaluations
		 * and their weights, and, finally, combine all evaluations into a final
		 * percent */
		
		addReceiversEvaluations();
		combineEvaluations();
		removeDonors();
		updateReceivers();
		
		state = PeerReviewedAssignationState.CLOSED;
	}
	
	private void addReceiversEvaluations() {
		
		/* Update percentages  */
		for (Receiver receiver : assignation.getReceivers()) {
			ReceiverData receiverData = new ReceiverData();
			
			receiverData.receiverId = receiver.getId().toString();
			
			receiverData.isDonor = false;
			if (receiver.getType() == ReceiverType.DONOR) {
				receiverData.isDonor = true;
			}
			
			List<EvaluationGrade> gradesSet = getReceiverGradesSet(receiver.getId()); 
			
			double mean = 0.0;
			boolean evaluated;
			
			if (gradesSet.size() == 0) {
				/* no body evaluated this receiver? mean is zero then */
				mean = 0.0;
				evaluated = false;
			} else {
			
				evaluated = true;
				
				/* compute the mean of evaluations set */
				double totalWeight = 0.0;
				
				for (EvaluationGrade grade : gradesSet) {
					double thisWeight = grade.getEvaluator().getWeight(); 
					totalWeight += thisWeight;
					mean += grade.getPercent() * thisWeight;
				}
				
				mean = mean / totalWeight;
			}
				
			/* update the receiver evaluations */
			List<EvaluationGrade> grades = getReceiverGrades(receiver.getId());
			
			for (EvaluationGrade grade : grades) {
				EvaluationData evaluation = new EvaluationData();
				
				evaluation.setEvaluatorId(grade.getEvaluator().getId().toString());
				evaluation.setWeight(grade.getEvaluator().getWeight());
				
				if (evaluated) {
					if (grade.getType() == EvaluationGradeType.SET) {
						evaluation.setValue(grade.getPercent());	
					} else {
						evaluation.setValue(mean);
					}
				} else {
					evaluation.setValue(mean);
				}
				
				receiverData.evaluations.add(evaluation);
			}
		
			
			receiversData.add(receiverData);
		}
	}
	
	/** Get all the grades of a receiver */
	private List<EvaluationGrade> getReceiverGrades(UUID receiverId) { 
		
		List<EvaluationGrade> grades = new ArrayList<EvaluationGrade>();
		
		for (Evaluator evaluator : this.assignation.getEvaluators()) {
			for (EvaluationGrade grade : evaluator.getGrades()) {
				if (grade.getReceiver().getId() == receiverId) {
					if (grade.getState() == EvaluationGradeState.DONE) {
						grades.add(grade);
					}
				}
			}
		}
		
		return grades;
	}
	
	/** Get all the grades of a receiver which are SET (not DONT_KNOW) */
	private List<EvaluationGrade> getReceiverGradesSet(UUID receiverId) { 
		
		List<EvaluationGrade> grades = new ArrayList<EvaluationGrade>();
		
		for (Evaluator evaluator : this.assignation.getEvaluators()) {
			for (EvaluationGrade grade : evaluator.getGrades()) {
				if (grade.getReceiver().getId() == receiverId) {
					if (grade.getState() == EvaluationGradeState.DONE) {
						if (grade.getType() == EvaluationGradeType.SET) {
							grades.add(grade);
						}
					}
				}
			}
		}
		
		return grades;
	}
	
	
	private void combineEvaluations() {
		/* combine all evaluations and weights into a single percentage for each receiver */
		
		/* compute the mean percentage of each receiver (not normalized), may not sum 100% */
		double totalPercentage = 0.0;
		for(ReceiverData  receiver : receiversData) {
			double totalWeight = 0.0;
			for(EvaluationData evaluation : receiver.evaluations) {
				totalWeight += evaluation.weight;
			}
			
			receiver.nonNormalizedPercent = 0.0;
			for(EvaluationData evaluation : receiver.evaluations) {
				receiver.nonNormalizedPercent += evaluation.value * evaluation.weight / totalWeight;
			}
			
			totalPercentage += receiver.nonNormalizedPercent;
		}
		
		/* normalize the percentages */
		double scale = totalPercentage / 100.0;
		for(ReceiverData  receiver : receiversData) {
			receiver.normalizedPercent = receiver.nonNormalizedPercent / scale;
		}
	}
	
	private void removeDonors () {
		double donatedPercentage = 0.0;
		for(ReceiverData receiverData : receiversData) {
			if (receiverData.isDonor) {
				donatedPercentage += receiverData.normalizedPercent;
			}
		}
		
		double remainingPercent = 100.0 - donatedPercentage;
		double scale = 100.0 / remainingPercent;
		
		for(ReceiverData receiverData : receiversData) {
			if (receiverData.isDonor) {
				receiverData.afterDonorPercent = 0.0;
			} else {
				receiverData.afterDonorPercent = receiverData.normalizedPercent * scale; 
			}
		}
	}
	
	private void updateReceivers() {
		/* copy the normalized percents from receiversData into
		 * assignation receivers */
		for(ReceiverData receiverData : receiversData) {
			int receiverIx = indexOfReceiver(receiverData.receiverId);
			this.assignation.getReceivers().get(receiverIx).setEvaluatedPercent(receiverData.normalizedPercent);
			this.assignation.getReceivers().get(receiverIx).setAssignedPercent(receiverData.afterDonorPercent);
		}
	}
	
	
	/* getter/setters */
	
	public Assignation getAssignation() {
		return assignation;
	}

	public void setAssignation(Assignation assignation) {
		this.assignation = assignation;
	}

	public List<ReceiverData> getReceiversData() {
		return receiversData;
	}

	public void setReceiversData(List<ReceiverData> receiverComps) {
		this.receiversData = receiverComps;
	}

	public PeerReviewedAssignationState getState() {
		return state;
	}

	public void setState(PeerReviewedAssignationState state) {
		this.state = state;
	}
	
	


}
