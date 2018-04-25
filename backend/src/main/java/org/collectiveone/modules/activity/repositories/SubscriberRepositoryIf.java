package org.collectiveone.modules.activity.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.activity.Subscriber;
import org.collectiveone.modules.activity.enums.SubscriptionElementType;
import org.collectiveone.modules.users.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SubscriberRepositoryIf extends CrudRepository<Subscriber, UUID> {
	
	List<Subscriber> findByElementId(UUID elementId);
	
	Subscriber findByElementIdAndTypeAndUser_C1Id(UUID elementId, SubscriptionElementType type, UUID userId);
	
	Subscriber findByUser_C1IdAndType(UUID userId, SubscriptionElementType type);
	
	@Query("SELECT sub FROM Subscriber sub WHERE "
			+ "sub.inheritConfig IS NULL OR "
			+ "sub.inAppConfig IS NULL OR "
			+ "sub.pushConfig IS NULL OR "
			+ "sub.emailNowConfig IS NULL OR "
			+ "sub.emailSummaryConfig IS NULL OR "
			+ "sub.emailSummaryPeriodConfig IS NULL")
	List<Subscriber> findAllNotSet();
	
	@Query("SELECT users FROM AppUser users "
			+ "WHERE c1Id NOT IN ("
			+ "SELECT sub.user.c1Id FROM Subscriber sub WHERE sub.type = ?1)")
	List<AppUser> findAllUsersWithoutSubscriberOfType(SubscriptionElementType type);

}
