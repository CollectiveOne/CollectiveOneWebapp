package org.collectiveone.modules.governance;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CardLikeRepositoryIf extends CrudRepository<CardLike, UUID> {
	
	CardLike findByCardWrapperIdAndAuthor_c1Id(UUID cardWrapperId, UUID authorId);
	
	@Query("SELECT COUNT (lks) FROM CardLike lks WHERE lks.cardWrapper.id = ?1")
	Integer countOfCardInternal(UUID cardWrapperId);
	
	default int countOfCard(UUID cardWrapperId) {
		Integer res = countOfCardInternal(cardWrapperId);
		return res == null ? 0 : res.intValue();
	}

}
