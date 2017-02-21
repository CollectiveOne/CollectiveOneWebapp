package org.collectiveone.repositories;

import org.collectiveone.model.Voter;
import org.springframework.stereotype.Repository;

@Repository
public class VoterRepository extends BaseRepository {

	public VoterRepository() {
		super();
	}

	public Voter get(int id) {
		return (Voter) super.get(id,Voter.class);
	}
	
	
}
