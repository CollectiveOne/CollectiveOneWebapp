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
import org.collectiveone.modules.c1.data.entities.ExternalLink;
import org.collectiveone.modules.c1.data.entities.Link;
import org.collectiveone.modules.c1.data.entities.NodeData;
import org.collectiveone.modules.c1.data.entities.TextData;
import org.collectiveone.modules.c1.data.enums.DataType;
import org.collectiveone.modules.c1.data.repositories.DataRepositoryIf;
import org.collectiveone.modules.c1.data.repositories.DraftRepositoryIf;
import org.collectiveone.modules.c1.data.repositories.NodeDataRepositoryIf;
import org.collectiveone.modules.c1.data.repositories.TextDataRepositoryIf;
import org.collectiveone.modules.uprcl.UprtclService;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DataService {
	
	@Autowired
	private UprtclService uprtclService;
	
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
	public DataDto getDataDto(String dataId) throws Exception {
		return dataRepository.findOne(dataId).toDto();
	}
	
	@Transactional(rollbackOn = Exception.class)
	public DataDto getDraftDto(String elementId, String requestBy) throws Exception {
		return draftRepository.findByUserIdAndElementId(requestBy, elementId).toDataDto();
	}
	
	@Transactional(rollbackOn = Exception.class)
	public List<String> createDatas(List<DataDto> dataDtos) throws Exception {
		List<String> datasIds = new ArrayList<String>();
		
		for (DataDto dataDto : dataDtos) {
			datasIds.add(uprtclService.getLocalLink(createData(dataDto).getId()).toString());
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
		data.setId();
		
		return dataRepository.save(data);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Draft createDraft(DraftDto draftDto, String requestBy) throws Exception {	
		
		Draft draft = new Draft();
		
		draft.setUser(appUserRepository.getOne(requestBy));
		draft.setElementId(draftDto.getElementId());
		
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
				
				link.setLink(new ExternalLink(linkDto.getLink()));
				link.setBefore(linkDto.getBefore() != null ? new ExternalLink(linkDto.getBefore()) : null);
				link.setAfter(linkDto.getAfter() != null ? new ExternalLink(linkDto.getAfter()) : null);
				
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
