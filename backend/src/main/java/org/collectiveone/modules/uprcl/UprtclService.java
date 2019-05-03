package org.collectiveone.modules.uprcl;

import java.sql.Timestamp;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.collectiveone.modules.c1.data.DataType;
import org.collectiveone.modules.c1.data.TextContent;
import org.collectiveone.modules.c1.data.TextContentRepositoryIf;
import org.collectiveone.modules.uprcl.dtos.CommitDto;
import org.collectiveone.modules.uprcl.dtos.ContentDto;
import org.collectiveone.modules.uprcl.dtos.ContextDto;
import org.collectiveone.modules.uprcl.dtos.DataDto;
import org.collectiveone.modules.uprcl.dtos.LinkDto;
import org.collectiveone.modules.uprcl.dtos.PerspectiveDto;
import org.collectiveone.modules.uprcl.entities.Commit;
import org.collectiveone.modules.uprcl.entities.Content;
import org.collectiveone.modules.uprcl.entities.Context;
import org.collectiveone.modules.uprcl.entities.Data;
import org.collectiveone.modules.uprcl.entities.Link;
import org.collectiveone.modules.uprcl.entities.Perspective;
import org.collectiveone.modules.uprcl.repositories.CommitRepositoryIf;
import org.collectiveone.modules.uprcl.repositories.ContentRepositoryIf;
import org.collectiveone.modules.uprcl.repositories.ContextRepositoryIf;
import org.collectiveone.modules.uprcl.repositories.DataRepositoryIf;
import org.collectiveone.modules.uprcl.repositories.LinkRepositoryIf;
import org.collectiveone.modules.uprcl.repositories.PerspectiveRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UprtclService {
	
	private static final Logger logger = LogManager.getLogger(Service.class);
	
	@Autowired
	private ContextRepositoryIf contextRepository;
	
	@Autowired
	private PerspectiveRepositoryIf perspectiveRepository;
	
	@Autowired
	private CommitRepositoryIf commitRepository; 
	
	@Autowired
	private ContentRepositoryIf contentRepository;
	
	@Autowired
	private LinkRepositoryIf linkRepository;
	
	@Autowired
	private TextContentRepositoryIf textContentRepository;
	
	@Autowired
	private DataRepositoryIf dataRepository;
	
	
	@Transactional(rollbackOn = Exception.class)
	public Context getOrCreateUserContext(String did) {
		
		Context context = new Context();
		
		context.setCreator(did);
		context.setNonce(0L);
		context.setTimestamp(new Timestamp(0L));
		
		context.setId();
		context = contextRepository.save(context);
		
		return context;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Context getOrCreateContext(ContextDto contextDto, String requestBy) throws Exception {
		
		Context context = null;
		
		if (contextDto.getId() != null) context = contextRepository.findOne(contextDto.getId());
		
		if (context == null) {
			context = createContext(contextDto, requestBy);
		} 
		
		return context;
	}

	@Transactional(rollbackOn = Exception.class)
	public Context createContext(ContextDto contextDto, String requestBy) throws Exception {
		
		Context context = new Context();
		
		context.setCreator(requestBy);
		context.setNonce(contextDto.getNonce());
		context.setTimestamp(new Timestamp(contextDto.getTimestamp()));
		
		context.setId();
		context = contextRepository.save(context);
		
		for (PerspectiveDto perspectiveDto : contextDto.getPerspectives()) {
			perspectiveDto.getContext().setId(context.getId());;
			createPerspective(perspectiveDto, requestBy);
		}
		
		return context;
		
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Perspective getOrCreatePerspective(PerspectiveDto perspectiveDto, String requestBy) throws Exception {
		Perspective perspective = null;
				
		if (perspectiveDto.getId() != null) perspective = perspectiveRepository.findOne(perspectiveDto.getId());
		
		if (perspective == null) {
			perspective = createPerspective(perspectiveDto, requestBy);
		} 
		
		return perspective;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Perspective createPerspective(PerspectiveDto perspectiveDto, String requestBy) throws Exception {
		
		Context context = getOrCreateContext(perspectiveDto.getContext(), requestBy);
		
		Perspective perspective = new Perspective();
		
		perspective.setCreator(requestBy);
		perspective.setName(perspectiveDto.getName());
		perspective.setContext(context);
		
		/* commit */
		Commit head = getOrCreateCommit(perspectiveDto.getHead(), requestBy);
		
		perspective.setHead(head);
		perspective.setType(perspectiveDto.getType());
		perspective.setTimestamp(new Timestamp(perspectiveDto.getTimestamp()));
		
		perspective.setId();
		
		perspective = perspectiveRepository.save(perspective);
		
		return perspective;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Commit getOrCreateCommit(CommitDto commitDto, String requestBy) throws Exception {
		Commit commit = null;
				
		if (commitDto.getId() != null) commit = commitRepository.findOne(commitDto.getId());
		
		if (commit == null) {
			commit = createCommit(commitDto, requestBy);
		} 
		
		return commit;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Commit createCommit(CommitDto commitDto, String requestBy) throws Exception {

		Commit commit = new Commit();
		
		commit.setCreator(requestBy);
		commit.setMessage(commitDto.getMessage());
		
		Content content = getOrCreateContent(commitDto.getContent(), requestBy);
		commit.setContent(content);
		
		for (Map.Entry<String, CommitDto> parentEntry : commitDto.getParents().entrySet()) {
			
			CommitDto parentDto = parentEntry.getValue();
			
			Commit parent = getOrCreateCommit(parentDto, requestBy);
			commit.getParents().put(parent.getId(), parent);	
		}

		commit.setId();
		commit = commitRepository.save(commit);
		
		return commit;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Content getOrCreateContent(ContentDto contentDto, String requestBy) throws Exception {
		Content content = null;
		
		if (contentDto.getId() != null) content = contentRepository.findOne(contentDto.getId());
		
		if (content == null) {
			content = createContent(contentDto, requestBy);
		} 
		
		return content;
	}
		
	@Transactional(rollbackOn = Exception.class)
	public Content createContent(ContentDto contentDto, String requestBy) throws Exception {
		
		Data data = getOrCreateData(contentDto.getData(), requestBy);
		
		Content content = new Content();
		
		content.setData(data);
		
		for (Map.Entry<String, LinkDto> linkEntry : contentDto.getLinks().entrySet()) {
			LinkDto linkDto = linkEntry.getValue();

			/* 
			 * POTENTIALLY A RECURSVIE CALL TO CREATE PERSPECTIVE OF SUBCONTEXTS AS PART
			 * OF THE PROCESS OF CREATING A PERSPECTIVE
			 *  
			 * */
			Perspective perspective = getOrCreatePerspective(linkDto.getPerspective(), requestBy);
			
			Link link = new Link(); 
			
			link.setType(linkDto.getType());
			link.setPerspective(perspective);
			link.setPositionType(linkDto.getPositionType());
			
			link = linkRepository.save(link);
			
			switch (linkDto.getPositionType()) {
			
			case DOUBLE_LINKED_LIST:
				link.setAfter(getOrCreatePerspective(linkDto.getAfter(), requestBy));
				link.setBefore(getOrCreatePerspective(linkDto.getBefore(), requestBy));
			break;
			
			case SINGLE_LINKED_LIST:
				link.setAfter(getOrCreatePerspective(linkDto.getAfter(), requestBy));
			break;
			
			}
			
			content.getLinks().put(link.getId(), link);
		}
		
		content.setId();
		
		return contentRepository.save(content);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Data getOrCreateData(DataDto dataDto, String requestBy) throws Exception {
		Data data = null;
		
		if (dataDto.getId() != null) data = dataRepository.findOne(dataDto.getId()); 
		
		if (data == null) {
			data = createData(dataDto, requestBy);
		} 
		
		return data;
	}
	
	
	@Transactional(rollbackOn = Exception.class)
	public Data createData(DataDto dataDto, String requestBy) throws Exception {	
		
		Data data = new Data();
		
		switch (dataDto.getType()) {
		
		case TEXT:
			data.setType(DataType.TEXT);
			TextContent textContent = new TextContent(dataDto.getTextContent().getText());
			textContent = textContentRepository.save(textContent);
			data.setTextContent(textContent);
			break;
			
		default:
			throw new Exception();
			
		}
		
		data.setId();
		return dataRepository.save(data);
	}
	

}