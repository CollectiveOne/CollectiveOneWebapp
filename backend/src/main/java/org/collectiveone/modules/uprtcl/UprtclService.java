package org.collectiveone.modules.uprtcl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.collectiveone.modules.ipld.IpldService;
import org.collectiveone.modules.uprtcl.dtos.CommitDto;
import org.collectiveone.modules.uprtcl.dtos.ContextDto;
import org.collectiveone.modules.uprtcl.dtos.PerspectiveDto;
import org.collectiveone.modules.uprtcl.entities.Commit;
import org.collectiveone.modules.uprtcl.entities.Context;
import org.collectiveone.modules.uprtcl.entities.Perspective;
import org.collectiveone.modules.uprtcl.repositories.CommitRepositoryIf;
import org.collectiveone.modules.uprtcl.repositories.ContextRepositoryIf;
import org.collectiveone.modules.uprtcl.repositories.PerspectiveRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.ipfs.cid.Cid;
import io.ipfs.multibase.Multibase;
import io.ipfs.multihash.Multihash.Type;

@Service
public class UprtclService {
	
	@Value("${UPRTLC_ENDPOINT}")
	private String UPRTCL_ENPOINT;
	
	@Value("${UPRTLC_DFT_MULTIHASH_TYPE}")
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
		
		context.setCreatorId(did);
		context.setNonce(0L);
		context.setTimestamp(new Timestamp(0L));
		
		context.setId(UPRTCL_DFT_TYPE);
		context = contextRepository.save(context);
		
		/* create one empty perspective */
		Perspective perspective = createEmptyPerspective(context.getId(), did);
				
		return perspective;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Context getContext(byte[] contextId) throws Exception {
		return contextRepository.getOne(contextId);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public byte[] getContextId(ContextDto contextDto) throws Exception {
		Context context = new Context();
		
		context.setCreatorId(contextDto.getCreatorId());
		context.setNonce(contextDto.getNonce());
		context.setTimestamp(new Timestamp(contextDto.getTimestamp()));
		context.setId(UPRTCL_DFT_TYPE);
		
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
	public ContextDto getContextDto(byte[] contextId) throws Exception {
		return contextRepository.getOne(contextId).toDto();
	}
	
	@Transactional(rollbackOn = Exception.class)
	public List<byte[]> createContexts(List<ContextDto> contextDtos, String requestBy) throws Exception {
		List<byte[]> contextIds = new ArrayList<byte[]>();
		
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
			Cid cid = IpldService.encode(contextDto.getId());
			context = contextRepository.getOne(cid.toBytes());
		}
		
		if (context == null) {
			context = new Context();
		}
		
		if (clone) {
			context.setCreatorId(contextDto.getCreatorId());
			context.setTimestamp(new Timestamp(contextDto.getTimestamp()));
		} else {
			context.setCreatorId(requestBy);
			context.setTimestamp(new Timestamp(
					contextDto.getTimestamp() != null ? contextDto.getTimestamp() : System.currentTimeMillis()));
		}
		
		context.setNonce(contextDto.getNonce());
		
		if (clone) {
			/* Use the same hash type and base encoding to have the same 
			 * id string */	
			
			Cid existingCid = IpldService.encode(contextDto.getId());
			context.setId(existingCid.getType());
			
			/* validate the ID is as expected */
			if (!context.getId().equals(existingCid.toBytes())) {
				throw new Exception(
						"object ID inconsistent, expecting \"" 
						+ context.getId() 
						+ "\" but it was \"" 
						+ Multibase.encode(Multibase.encoding(contextDto.getId()), existingCid.toBytes()) + "\"");
			}
		} else {
			context.setId(UPRTCL_DFT_TYPE);
		}
		
		context = contextRepository.save(context);
		
		return context;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public PerspectiveDto getPerspectiveDto(byte[] perspectiveId) throws Exception {
		return perspectiveRepository.getOne(perspectiveId).toDto();
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Perspective createEmptyPerspective(byte[] contextId, String requestBy) throws Exception {
		PerspectiveDto perspectiveDto = new PerspectiveDto();
		
		perspectiveDto.setContextId(IpldService.decode(contextId));
		perspectiveDto.setName("default");
		perspectiveDto.setHeadId(null);
		
		return createPerspective(perspectiveDto, requestBy);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public List<byte[]> createPerspectives(List<PerspectiveDto> perspectiveDtos, String requestBy) throws Exception {
		List<byte[]> perspectiveIds = new ArrayList<byte[]>();
		
		for (PerspectiveDto perspectiveDto : perspectiveDtos) {
			perspectiveIds.add(createPerspective(perspectiveDto, requestBy).getId());
		}
		
		return perspectiveIds;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public byte[] updatePerspective(byte[] perspectiveId, byte[] headId) throws Exception {
		Perspective perspective = perspectiveRepository.getOne(perspectiveId);
		perspective.setHeadId(headId);
		return perspectiveRepository.save(perspective).getId();
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Perspective createPerspective(PerspectiveDto perspectiveDto, String requestBy) throws Exception {
		
		Boolean clone = false;
		if (perspectiveDto.getId() != null && perspectiveDto.getCreatorId() != null) {
			clone = true;
			throw new Exception("perspectives cant be cloned, they are platform dependant");
		}
		
		Perspective perspective = new Perspective();
		perspective.setCreatorId(requestBy);
		perspective.setTimestamp(new Timestamp(
				perspectiveDto.getTimestamp() != null ? perspectiveDto.getTimestamp() : System.currentTimeMillis()));
		
		if (perspectiveDto.getContextId() == null) {
			throw new Exception("context cannot be empty");
		} 
			
		perspective.setContextId(IpldService.encode(perspectiveDto.getContextId()).toBytes());
		perspective.setName(perspectiveDto.getName());
		perspective.setOrigin(UPRTCL_ENPOINT);
		
		if (perspectiveDto.getHeadId() != null) perspective.setHeadId(IpldService.encode(perspectiveDto.getHeadId()).toBytes());
		
		perspective.setId(UPRTCL_DFT_TYPE);
		perspective = perspectiveRepository.save(perspective);

		return perspective;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public CommitDto getCommitDto(byte[] commitId) throws Exception {
		return commitRepository.getOne(commitId).toDto();
	}
	
	@Transactional(rollbackOn = Exception.class)
	public List<String> createCommits(List<CommitDto> commitDtos, String requestBy) throws Exception {
		List<String> commitsIds = new ArrayList<String>();
		
		for (CommitDto commitDto : commitDtos) {
			commitsIds.add(IpldService.decode(createCommit(commitDto, requestBy).getId()));
		}
		
		return commitsIds;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Commit createCommit(CommitDto commitDto, String requestBy) throws Exception {

		Commit commit = new Commit();
		
		commit.setCreatorId(requestBy);
		commit.setTimestamp(new Timestamp(
				commitDto.getTimestamp() != null ? commitDto.getTimestamp() : System.currentTimeMillis()));
		
		commit.setMessage(commitDto.getMessage());
		
		for (String parentIdString : commitDto.getParentsIds()) {
			commit.getParentsIds().add(IpldService.encode(parentIdString).toBytes());	
		}

		commit.setDataId(IpldService.encode(commitDto.getDataId()).toBytes());
		commit.setId(UPRTCL_DFT_TYPE);
		
		commit = commitRepository.save(commit);
		
		return commit;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public String getElementCreator(String elementId) throws Exception {
		return getElementCreator(IpldService.encode(elementId).toBytes());
	}
	
	@Transactional(rollbackOn = Exception.class)
	public String getElementCreator(byte[]  elementId) {
		
		Context context = contextRepository.getOne(elementId);
		
		if (context != null) {
			return context.getCreatorId();
		}
		
		Perspective perspective = perspectiveRepository.getOne(elementId);
		
		if (perspective != null) {
			return perspective.getCreatorId();
		}
		
		Commit commit = commitRepository.getOne(elementId);
		
		if (commit != null) {
			return commit.getCreatorId();
		}
		
		return null;
	}

}