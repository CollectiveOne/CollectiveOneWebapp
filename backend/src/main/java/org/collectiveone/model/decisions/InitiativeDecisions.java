package org.collectiveone.model.decisions;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.collectiveone.model.basic.Decision;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

public class InitiativeDecisions {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	private List<Decision> createSubInitiatives;
	
	
	
}
