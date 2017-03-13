package org.collectiveone.services;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.collectiveone.model.Activity;
import org.collectiveone.model.ActivityType;
import org.collectiveone.model.Cbtion;
import org.collectiveone.model.Comment;
import org.collectiveone.model.Promoter;
import org.collectiveone.model.User;
import org.collectiveone.web.dto.CommentDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImp extends BaseService {
	
	
	
	@Transactional
	public void create(CommentDto commentDto) throws IOException {

		User creator = userRepository.get(commentDto.getCreatorUsername());
		Cbtion cbtion = cbtionRepository.get(commentDto.getCbtionId());

		Comment parent = null;

		if(commentDto.getParentId() != null) {
			parent = commentDao.get(commentDto.getParentId());
		}
		
		Comment comment = new Comment();

		comment.setCreationDate(new Timestamp(System.currentTimeMillis()));
		comment.setCreator(creator);
		comment.setContent(commentDto.getContent());


		if(parent != null) {
			parent.getReplies().add(comment);
			comment.setParent(parent);
			commentDao.save(parent);
		} else {
			cbtion.getComments().add(comment);
		}

		comment.setCbtion(cbtion);

		cbtionRepository.save(cbtion);
		commentDao.save(comment);

		Activity act = new Activity();
		act.setCreationDate(new Timestamp(System.currentTimeMillis()));
		act.setComment(comment);
		act.setType(ActivityType.COMMENT);
		act.setProject(cbtion.getProject());
		act.setEvent("new");
		activityService.saveAndNotify(act);
		
	}

	@Transactional
	public List<CommentDto> getRepliesDtos(Long commentId) {
		List<Comment> replies = commentDao.getRepliesSorted(commentId);
		List<CommentDto> repliesDtos = new ArrayList<CommentDto>();
		for (Comment comment : replies) {
			repliesDtos.add(comment.toDto());
		}
		return repliesDtos;
	}


	@Transactional
	public void promote(Long commentId, Long userId, boolean promoteUp) {
		Comment comment = commentDao.get(commentId);
		commentDao.save(comment);

		Promoter promoter = promoterDao.getOfComment(commentId, userId);

		/* countPromotersDiff will not include the current transaction so this logic 
		 * is used to solve the problem*/
		
		int currentRelevance = commentDao.countPromotersDiff(commentId);
		int newRelevance = currentRelevance;
		
		if(promoter == null) {
			promoter = new Promoter();
			promoter.setUser(userRepository.get(userId));
			comment.getPromoters().add(promoter);
			if(promoteUp) {
				newRelevance = newRelevance + 1;
			} else {
				newRelevance = newRelevance - 1;
			}
		} else {
			if(promoter.getPromoteUp() !=  promoteUp) {
				if(promoteUp) {
					newRelevance = newRelevance + 2;
				} else {
					newRelevance = newRelevance - 2;
				}
			}
		}

		promoter.setCreationDate(new Timestamp(System.currentTimeMillis()));
		promoter.setPromoteUp(promoteUp);
		
		promoterDao.save(promoter);

		/* update relevance to order results */
		comment.setRelevance(newRelevance);
	}

}
