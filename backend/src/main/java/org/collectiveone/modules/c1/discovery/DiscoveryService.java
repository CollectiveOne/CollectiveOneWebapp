package org.collectiveone.modules.c1.discovery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DiscoveryService {

	@Value("${UPRTLC_ENDPOINT}")
	private String UPRTCL_ENPOINT;
	
	public String getOrigin() {
		return UPRTCL_ENPOINT;
	}
	
}
