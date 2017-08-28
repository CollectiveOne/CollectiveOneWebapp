package org.collectiveone.modules.assignations.evaluationlogic;

import java.util.ArrayList;
import java.util.List;

public class ReceiverData {
	
	public String receiverId;
	public List<EvaluationData> evaluations = new ArrayList<EvaluationData>();
	public boolean isDonor;
	public double nonNormalizedPercent;
	public double normalizedPercent;
	public double afterDonorPercent;

}
