package org.collectiveone.modules.c1.views;

import javax.transaction.Transactional;

import org.collectiveone.modules.ipld.IpldService;
import org.collectiveone.modules.uprtcl.UprtclService;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ViewService {

	@Autowired
	private ViewRepositoryIf viewRepository;
	
	@Autowired
	private AppUserRepositoryIf appUserRepository;
	
	@Autowired
	private UprtclService uprtclService;
	
	
	@Transactional(rollbackOn = Exception.class)
	public String setView(ViewDto viewDto, String requestBy) throws Exception {
		byte[] elementId = IpldService.encode(viewDto.getElementId()).toBytes();
		byte[] inElementId = viewDto.getInElementId() != null ? IpldService.encode(viewDto.getInElementId()).toBytes() : null;
				
		View view = null;
		
		if (inElementId != null) {
			view = viewRepository.findByElementIdAndInElementIdAndUserDid(elementId, inElementId, requestBy);
		} else {
			view = viewRepository.findByElementIdAndUserDid(elementId, requestBy);
		}
		
		if (view == null) {
			view = new View();
			view.setElementId(elementId);
			view.setInElementId(inElementId);
		}
		
		view.setUser(appUserRepository.getOne(requestBy));
		view.setElementViewType(viewDto.getElementViewType());
		
		view = viewRepository.save(view);
		
		return view.getId().toString();		
	}
	
	@Transactional(rollbackOn = Exception.class)
	public ViewDto getView(byte[] elementId, byte[] inElementId, String requestBy) throws Exception {
		
		View view = null;
		
		if (inElementId != null) {
			view = viewRepository.findByElementIdAndInElementIdAndUserDid(elementId, inElementId, requestBy);
		} else {
			view = viewRepository.findByElementIdAndUserDid(elementId, requestBy);
		}
		
		/* if this user don't have a view for this element check the view of the creator */
		if (view == null) {
			String creatorId = uprtclService.getElementCreator(elementId);
			
			if (inElementId != null) {
				view = viewRepository.findByElementIdAndInElementIdAndUserDid(elementId, inElementId, creatorId);
			} else {
				view = viewRepository.findByElementIdAndUserDid(elementId, creatorId);
			}
		}
		
		/* if the creator don't have a view, check the oldest (original?) view */
		if (view == null) {
			view = viewRepository.findOldestOfElementId(elementId);
		}
		
		return view.toDto();
		
	}
	
	
	
}
