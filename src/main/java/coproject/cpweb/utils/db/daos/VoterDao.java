package coproject.cpweb.utils.db.daos;

import org.springframework.stereotype.Service;

import coproject.cpweb.utils.db.entities.Voter;

@Service
public class VoterDao extends BaseDao {

	public Voter get(int id) {
		return (Voter) super.get(id,Voter.class);
	}
	
	
}
