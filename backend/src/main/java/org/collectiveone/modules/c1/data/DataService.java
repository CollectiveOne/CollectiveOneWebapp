package org.collectiveone.modules.c1.data;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.collectiveone.modules.c1.data.dtos.DataDto;
import org.collectiveone.modules.c1.data.dtos.DraftDto;
import org.collectiveone.modules.c1.data.dtos.LinkDto;
import org.collectiveone.modules.c1.data.dtos.NodeDataDto;
import org.collectiveone.modules.c1.data.dtos.TextDataDto;
import org.collectiveone.modules.c1.data.entities.Data;
import org.collectiveone.modules.c1.data.entities.DataIf;
import org.collectiveone.modules.c1.data.entities.Draft;
import org.collectiveone.modules.c1.data.entities.Link;
import org.collectiveone.modules.c1.data.entities.NodeData;
import org.collectiveone.modules.c1.data.entities.TextData;
import org.collectiveone.modules.c1.data.enums.DataType;
import org.collectiveone.modules.c1.data.repositories.DataRepositoryIf;
import org.collectiveone.modules.c1.data.repositories.DraftRepositoryIf;
import org.collectiveone.modules.c1.data.repositories.NodeDataRepositoryIf;
import org.collectiveone.modules.c1.data.repositories.TextDataRepositoryIf;
import org.collectiveone.modules.ipld.IpldService;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.ipfs.multihash.Multihash.Type;

@Service
public class DataService {
	
	@Value("${UPRTLC_DFT_MULTIHASH_TYPE}")
	private Type UPRTCL_DFT_TYPE;
	
	@Autowired
	private DataRepositoryIf dataRepository;
	
	@Autowired
	private DraftRepositoryIf draftRepository;
	
	@Autowired
	private TextDataRepositoryIf textDataRepository;
	
	@Autowired
	private NodeDataRepositoryIf nodeDataRepository;
	
	@Autowired
	private AppUserRepositoryIf appUserRepository;
	
	
	@Transactional(rollbackOn = Exception.class)
	public DataDto getDataDto(byte[] dataId) throws Exception {
		return dataRepository.getOne(dataId).toDto();
	}
	
	@Transactional(rollbackOn = Exception.class)
	public DataDto getDraftDto(byte[] elementId, String requestBy) throws Exception {
		return draftRepository.findByUserIdAndElementId(requestBy, elementId).toDataDto();
	}
	
	@Transactional(rollbackOn = Exception.class)
	public List<byte[]> createDatas(List<DataDto> dataDtos) throws Exception {
		List<byte[]> datasIds = new ArrayList<byte[]>();
		
		for (DataDto dataDto : dataDtos) {
			datasIds.add(createData(dataDto).getId());
		}
		
		return datasIds;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public void createDrafts(List<DraftDto> draftDtos, String requestBy) throws Exception {
		for (DraftDto draftDto : draftDtos) {
			createDraft(draftDto, requestBy);
		}
	}
	
	
	@Transactional(rollbackOn = Exception.class)
	public Data createData(DataDto dataDto) throws Exception {	
		
		Data data= new Data();
		setCustomData(data, dataDto);
		data.setId(UPRTCL_DFT_TYPE);
		
		return dataRepository.save(data);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Draft createDraft(DraftDto draftDto, String requestBy) throws Exception {	
		
		Draft draft = new Draft();
		
		draft.setUser(appUserRepository.getOne(requestBy));
		draft.setElementId(IpldService.encode(draftDto.getElementId()).toBytes());
		
		setCustomData(draft, draftDto.getData());
		
		return draftRepository.save(draft);
	}
	
	private void setCustomData(DataIf data, DataDto dataDto) throws Exception {
		
		switch (dataDto.getType()) {
		
		case TEXT:
			data.setType(DataType.TEXT);
			TextDataDto textDataDto = new ObjectMapper().readValue(dataDto.getJsonData(), TextDataDto.class);
			
			TextData textData = new TextData();
			textData.setText(textDataDto.getText());
			
			textData = textDataRepository.save(textData);
			data.setTextData(textData);
			break;
			
		case NODE:
			data.setType(DataType.NODE);
			NodeDataDto nodeDataDto = new ObjectMapper().readValue(dataDto.getJsonData(), NodeDataDto.class);
			
			TextData textDataOnNode = new TextData();
			textDataOnNode.setText(nodeDataDto.getText());
			textDataOnNode = textDataRepository.save(textDataOnNode);
			
			NodeData nodeData = new NodeData();
			nodeData.setTextData(textDataOnNode);
			
			for (LinkDto linkDto : nodeDataDto.getLinks()) {
				Link link = new Link();
				
				link.setLink(IpldService.encode(linkDto.getLink()).toBytes());
				link.setBefore(linkDto.getBefore() != null ? IpldService.encode(linkDto.getBefore()).toBytes() : null);
				link.setAfter(linkDto.getAfter() != null ? IpldService.encode(linkDto.getAfter()).toBytes() : null);
				
				nodeData.getLinks().add(link);
			}
			
			nodeData = nodeDataRepository.save(nodeData);
			
			data.setNodeData(nodeData);
			
			break;
			
		default:
			throw new Exception();
			
		}
	}
	
}
