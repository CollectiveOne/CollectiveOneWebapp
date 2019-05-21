package org.collectiveone.modules.uprcl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.collectiveone.modules.c1.data.entities.ExternalLink;
import org.collectiveone.modules.c1.data.enums.NetworkId;
import org.collectiveone.modules.ipld.Ipld;
import org.collectiveone.modules.uprcl.dtos.CommitDto;
import org.collectiveone.modules.uprcl.dtos.ContextDto;
import org.collectiveone.modules.uprcl.dtos.PerspectiveDto;
import org.collectiveone.modules.uprcl.entities.Commit;
import org.collectiveone.modules.uprcl.entities.Context;
import org.collectiveone.modules.uprcl.entities.Perspective;
import org.collectiveone.modules.uprcl.repositories.CommitRepositoryIf;
import org.collectiveone.modules.uprcl.repositories.ContextRepositoryIf;
import org.collectiveone.modules.uprcl.repositories.PerspectiveRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.ipfs.cid.Cid;
import io.ipfs.multibase.Multibase;
import io.ipfs.multibase.Multibase.Base;
import io.ipfs.multihash.Multihash.Type;

@Service
public class UprtclService {
	
	@Value("${UPRTLC_ENDPOINT}")
	private String UPRTCL_ENPOINT;
	
	@Value("${UPRTLC_DFT_BASE}")
	private Base UPRTCL_DFT_BASE;
	
	@Value("${UPRTLC_DFT_BASE}")
	private Type UPRTCL_DFT_TYPE;
	
	@Autowired
	private ContextRepositoryIf contextRepository;
	
	@Autowired
	private PerspectiveRepositoryIf perspectiveRepository;
	
	@Autowired
	private CommitRepositoryIf commitRepository; 
	
	
	
	@Transactional(rollbackOn = Exception.class)
	public Perspective getOrCreateUserContext(String did) throws Exception {
		
		Context context = new Context();
		
		context.setCreator(did);
		context.setNonce(0L);
		context.setTimestamp(new Timestamp(0L));
		
		context.setId(UPRTCL_DFT_TYPE, UPRTCL_DFT_BASE);
		context = contextRepository.save(context);
		
		/* create one empty perspective */
		Perspective perspective = createEmptyPerspective(context.getId(), did);
				
		return perspective;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Context getContext(String contextId) throws Exception {
		return contextRepository.findOne(contextId);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public String getContextId(ContextDto contextDto) throws Exception {
		Context context = new Context();
		
		context.setCreator(contextDto.getCreatorId());
		context.setNonce(contextDto.getNonce());
		context.setTimestamp(new Timestamp(contextDto.getTimestamp()));
		context.setId(UPRTCL_DFT_TYPE, UPRTCL_DFT_BASE);
		
		return context.getId();
	}
	
	@Transactional(rollbackOn = Exception.class)
	public List<PerspectiveDto> getContextPerspectives(String contextId) throws Exception {
		return perspectiveRepository
				.findByContextId(contextId)
				.stream()
				.map(p -> {
					try { return p.toDto(); } 
					catch (Exception e) { e.printStackTrace(); }
					return null;
				}).collect(Collectors.toList());
	}
	
	@Transactional(rollbackOn = Exception.class)
	public ContextDto getContextDto(String contextId) throws Exception {
		return contextRepository.findOne(contextId).toDto();
	}
	
	@Transactional(rollbackOn = Exception.class)
	public List<String> createContexts(List<ContextDto> contextDtos, String requestBy) throws Exception {
		List<String> contextIds = new ArrayList<String>();
		
		for (ContextDto contextDto : contextDtos) {
			contextIds.add(createContext(contextDto, requestBy).getId());
		}
		
		return contextIds;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Context createContext(ContextDto contextDto, String requestBy) throws Exception {
		
		Boolean clone = false;
		if (contextDto.getId() != null && contextDto.getCreatorId() != null) {
			clone = true;
		}
		
		Context context = null;
		
		if (clone) {
			context = contextRepository.getOne(contextDto.getId());
		}
		
		if (context == null) {
			context = new Context();
		}
		
		if (clone) {
			context.setCreator(contextDto.getCreatorId());
			context.setTimestamp(new Timestamp(contextDto.getTimestamp()));
		} else {
			context.setCreator(requestBy);
			context.setTimestamp(new Timestamp(
					contextDto.getTimestamp() != null ? contextDto.getTimestamp() : System.currentTimeMillis()));
		}
		
		context.setNonce(contextDto.getNonce());
		
		if (clone) {
			/* Use the same hash type and base encoding to have the same 
			 * id string */	
			String existingId = contextDto.getId();
			context.setId(
					Ipld.parse(existingId).getType(), 
					Multibase.encoding(existingId));
			
			if (!context.getId().equals(existingId)) {
				throw new Exception(
						"object ID inconsistent, expecting \"" 
						+ context.getId() 
						+ "\" but it was \"" + existingId + "\"");
			}
		}
		
		context = contextRepository.save(context);
		
		return context;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public PerspectiveDto getPerspectiveDto(String perspectiveId) throws Exception {
		return perspectiveRepository.findOne(perspectiveId).toDto();
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Perspective createEmptyPerspective(String contextId, String requestBy) throws Exception {
		PerspectiveDto perspectiveDto = new PerspectiveDto();
		
		perspectiveDto.setContextId(contextId);
		perspectiveDto.setName("default");
		perspectiveDto.setHeadLink(null);
		
		return createPerspective(perspectiveDto, requestBy);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public List<String> createPerspectives(List<PerspectiveDto> perspectiveDtos, String requestBy) throws Exception {
		List<String> perspectiveLinks = new ArrayList<String>();
		
		for (PerspectiveDto perspectiveDto : perspectiveDtos) {
			perspectiveLinks.add(getLocalLink(createPerspective(perspectiveDto, requestBy).getId()).toString());
		}
		
		return perspectiveLinks;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public String updatePerspective(String perspectiveId, String headLink) throws Exception {
		Perspective perspective = perspectiveRepository.findOne(perspectiveId);
		perspective.setHeadLink(new ExternalLink(headLink));
		return getLocalLink(perspectiveRepository.save(perspective).getId()).toString();
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Perspective createPerspective(PerspectiveDto perspectiveDto, String requestBy) throws Exception {
		
		Perspective perspective = new Perspective();

		perspective.setCreatorId(requestBy);
		
		perspective.setTimestamp(new Timestamp(
				perspectiveDto.getTimestamp() != null ? perspectiveDto.getTimestamp() : System.currentTimeMillis()));
		
		perspective.setContextId(perspectiveDto.getContextId());
		perspective.setName(perspectiveDto.getName());
		
		perspective.setHeadLink(
				perspectiveDto.getHeadLink() != null ? 
						new ExternalLink(perspectiveDto.getHeadLink()) : null);
		
		perspective.setId();
		perspective = perspectiveRepository.save(perspective);

		return perspective;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public CommitDto getCommitDto(String commitId) throws Exception {
		return commitRepository.findOne(commitId).toDto();
	}
	
	@Transactional(rollbackOn = Exception.class)
	public List<String> createCommits(List<CommitDto> commitDtos, String requestBy) throws Exception {
		List<String> commitsLinks = new ArrayList<String>();
		
		for (CommitDto commitDto : commitDtos) {
			commitsLinks.add(getLocalLink(createCommit(commitDto, requestBy).getId()).toString());
		}
		
		return commitsLinks;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Commit createCommit(CommitDto commitDto, String requestBy) throws Exception {

		Commit commit = new Commit();
		
		commit.setCreatorId(requestBy);
		commit.setTimestamp(new Timestamp(
				commitDto.getTimestamp() != null ? commitDto.getTimestamp() : System.currentTimeMillis()));
		
		commit.setMessage(commitDto.getMessage());
		
		for (String parent : commitDto.getParentsLinks()) {
			commit.getParentsLinks().add(new ExternalLink(parent));	
		}

		commit.setDataLink(new ExternalLink(commitDto.getDataLink()));
		
		commit.setId();
		commit = commitRepository.save(commit);
		
		return commit;
	}
	
	
	public ExternalLink getLocalLink(String elementId) {
		return new ExternalLink(NetworkId.http + "://" + UPRTCL_ENPOINT + "/" + elementId);
	}
	
	public Boolean isLocalLink(ExternalLink link) {
		return (link.getNetwork() == NetworkId.http) && (link.getNetworkAddress() == UPRTCL_ENPOINT);
	}

}