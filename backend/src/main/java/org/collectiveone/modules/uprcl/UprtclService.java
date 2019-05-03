package org.collectiveone.modules.uprcl;

import java.sql.Timestamp;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.collectiveone.modules.c1.data.DataType;
import org.collectiveone.modules.c1.data.PositionType;
import org.collectiveone.modules.c1.data.TextContent;
import org.collectiveone.modules.c1.data.TextContentRepositoryIf;
import org.collectiveone.modules.c1.userSupport.WorkingCommit;
import org.collectiveone.modules.c1.userSupport.WorkingCommitRepositoryIf;
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
import org.collectiveone.modules.uprcl.entities.LinkType;
import org.collectiveone.modules.uprcl.entities.Perspective;
import org.collectiveone.modules.uprcl.entities.PerspectiveType;
import org.collectiveone.modules.uprcl.repositories.CommitRepositoryIf;
import org.collectiveone.modules.uprcl.repositories.ContentRepositoryIf;
import org.collectiveone.modules.uprcl.repositories.ContextRepositoryIf;
import org.collectiveone.modules.uprcl.repositories.DataRepositoryIf;
import org.collectiveone.modules.uprcl.repositories.LinkRepositoryIf;
import org.collectiveone.modules.uprcl.repositories.PerspectiveRepositoryIf;
import org.collectiveone.modules.users.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UprtclService {
	
	private static final Logger logger = LogManager.getLogger(Service.class);
	
	@Autowired
	private AppUserService appUserService;
	
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
	
	@Autowired
	private WorkingCommitRepositoryIf workingCommitRepository;
	
	
	
	@Transactional(rollbackOn = Exception.class)
	public Perspective getOrCreateUserContext(String did) throws Exception {
		
		Context context = new Context();
		
		context.setCreator(did);
		context.setNonce(0L);
		context.setTimestamp(new Timestamp(0L));
		
		context.setId();
		context = contextRepository.save(context);
		
		/* create one empty perspective */
		Perspective perspective = createEmptyPerspective(context.getId(), did, null);
				
		return perspective;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Context getOrCreateContext(ContextDto contextDto, String requestBy, LinkDto linkDto) throws Exception {
		
		Context context = null;
		
		if (contextDto.getId() != null) context = contextRepository.findOne(contextDto.getId());
		
		if (context == null) {
			context = createContext(contextDto, requestBy, linkDto);
		} 
		
		return context;
	}

	@Transactional(rollbackOn = Exception.class)
	public Context createContext(ContextDto contextDto, String requestBy, LinkDto linkDto) throws Exception {
		
		Context context = new Context();
		
		context.setCreator(requestBy);
		context.setNonce(contextDto.getNonce());
		context.setTimestamp(new Timestamp(contextDto.getTimestamp()));
		
		context.setId();
		context = contextRepository.save(context);
		
		for (PerspectiveDto perspectiveDto : contextDto.getPerspectives()) {
			perspectiveDto.getContext().setId(context.getId());;
			createPerspective(linkDto, requestBy);
		}
		
		return context;
		
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Perspective getPerspective(String perspectiveId) throws Exception {
		return perspectiveRepository.findOne(perspectiveId);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Perspective getOrCreatePerspective(LinkDto linkDto, String requestBy) throws Exception {
		
		PerspectiveDto perspectiveDto = linkDto.getPerspective(); 
		Perspective perspective = null;
				
		if (perspectiveDto.getId() != null) perspective = perspectiveRepository.findOne(perspectiveDto.getId());
		
		if (perspective == null) {
			perspective = createPerspective(linkDto, requestBy);
		} 
		
		return perspective;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Perspective createPerspective(LinkDto linkDto, String requestBy) throws Exception {
		
		PerspectiveDto perspectiveDto = linkDto.getPerspective();
		
		Context context = getOrCreateContext(perspectiveDto.getContext(), requestBy, linkDto);
		
		Perspective perspective = new Perspective();
		
		perspective.setCreator(requestBy);
		perspective.setName(perspectiveDto.getName());
		perspective.setContext(context);
		
		/* commit */
		Commit head = null;
		if (perspectiveDto.getHead() != null) head = getOrCreateCommit(perspectiveDto.getHead(), requestBy);
		
		perspective.setHead(head);
		perspective.setType(perspectiveDto.getType());
		perspective.setTimestamp(new Timestamp(perspectiveDto.getTimestamp()));
		
		perspective.setId();
		
		perspective = perspectiveRepository.save(perspective);
		
		/* perspectives are usually created inside a parent perspective */
		if (linkDto != null){
			WorkingCommit parentWorkingCommit = 
					workingCommitRepository.findByUserIdAndPerspectiveId(requestBy, linkDto.getParentPerspective().getId());
			
			Link link = getOrCreateLink(linkDto, requestBy);
			parentWorkingCommit.getLinks().put(link.getId(), link);
			
			parentWorkingCommit = workingCommitRepository.save(parentWorkingCommit);
		}
		
		return perspective;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Perspective createEmptyPerspective(String contextId, String requestBy, LinkDto linkDto) throws Exception {
		PerspectiveDto perspectiveDto = new PerspectiveDto();
		
		perspectiveDto.setCreator(requestBy);
		perspectiveDto.setContext(new ContextDto(contextId));
		perspectiveDto.setHead(null);
		perspectiveDto.setName("DEFAULT");
		perspectiveDto.setTimestamp(System.currentTimeMillis());
		perspectiveDto.setType(PerspectiveType.DYNAMIC);
		
		return createPerspective(linkDto, requestBy);
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

			Link link = getOrCreateLink(linkDto, requestBy);
			
			content.getLinks().put(link.getId(), link);
		}
		
		content.setId();
		
		return contentRepository.save(content);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Link getOrCreateLink(LinkDto linkDto, String requestBy) throws Exception {
		Link link = null;
		
		if (linkDto.getId() != null) link = linkRepository.findOne(linkDto.getId());
		
		if (link == null) {
			link = createLink(linkDto, requestBy);
		} 
		
		return link;
	}
	
	public Link createLink(LinkDto linkDto, String requestBy) throws Exception {
		
		Perspective perspective = getPerspective(linkDto.getPerspective().getId());
		
		Link link = new Link(); 
		
		link.setType(linkDto.getType());
		link.setPerspective(perspective);
		link.setPositionType(linkDto.getPositionType());
		
		link = linkRepository.save(link);
		
		switch (linkDto.getPositionType()) {
		
		case DOUBLE_LINKED_LIST:
			link.setAfter(getPerspective(linkDto.getAfter().getId()));
			link.setBefore(getPerspective(linkDto.getBefore().getId()));
		break;
		
		case SINGLE_LINKED_LIST:
			link.setAfter(getPerspective(linkDto.getAfter().getId()));
		break;
		
		}
		
		return link;
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