package org.collectiveone.modules.users;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitcoinj.core.Base58;
import org.bitcoinj.core.ECKey;
import org.collectiveone.common.crypto.CryptoService;
import org.collectiveone.modules.uprcl.ContextService;
import org.collectiveone.modules.uprcl.entities.Context;
import org.collectiveone.modules.users.nannies.DIDMethod;
import org.collectiveone.modules.users.nannies.DIDNanny;
import org.collectiveone.modules.users.nannies.DIDNannyRepositoryIf;
import org.collectiveone.modules.users.nannies.DIDNannyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;

@Service
public class AppUserService {
	
	private static final Logger logger = LogManager.getLogger(AppUserService.class);
	
	@Autowired
	private ContextService contextService;

	@Autowired
	private CryptoService cryptoService;
	
	@Autowired
	private AppUserRepositoryIf appUserRepository;
	
	@Autowired
	private DIDNannyRepositoryIf DIDNannyRepository;
	
	@Autowired
	private ManagementAPI mgmt;
	
	@Transactional(rollbackOn = Exception.class)
	public AppUser getOrCreateFromAuth0Id(String auth0Id) throws Exception {
		
		AppUser appUser = appUserRepository.findByAuth0Id(auth0Id);
		
		if(appUser == null) {
			/* the first time a user is requested, it 
			 * is added to the local DB */
			appUser = addUserToLocalDB(auth0Id);
		}
    	
    	return appUser;
	}
	
	@Transactional(rollbackOn = Exception.class)
	private AppUser addUserToLocalDB(String auth0Id) throws Exception {
		/* retrieve from Auth0 */
		AppUser appUser = null;
		User auth0User = null;
		
		if (auth0Id.equals("anonymousUser")) {
			return null;
		}
		
		try {
			auth0User = mgmt.users().get(auth0Id, null).execute();
			
			/* create a new user if not */
			DIDNanny nanny = newDIDNanny(DIDMethod.secp256k1, DIDNannyType.AUTH0, auth0User.getId());
			
			appUser = new AppUser();
			
			appUser.setDid(nanny.getDid());
			appUser.setNanny(nanny);
			appUser = appUserRepository.save(appUser);
			
			/* each user has one and only one context associated to his identity */
			Context userContext = contextService.getOrCreateUserContext(appUser.getDid());
			appUser.setContext(userContext);
			
			logger.debug("user root context id: {}", userContext.getId().toString());
				 
			appUser = appUserRepository.save(appUser);
			
		} catch (APIException exception) {
		    System.out.println(exception.getMessage());
		} catch (Auth0Exception exception) {
			System.out.println(exception.getMessage());
		}
		
		return appUser;
	}
	
	@Transactional(rollbackOn = Exception.class)
	private DIDNanny newDIDNanny(DIDMethod method, DIDNannyType nannyType, String extId) throws Exception {
		switch (method) {
			case secp256k1: 
				ECKey kp = new ECKey();
				String pubKeyEncoded = Base58.encode(kp.getPubKey());
				
				String did = "did:" + method.toString() + ":" + pubKeyEncoded;
				DIDNanny nanny = new DIDNanny();
				
				nanny.setDid(did);
				nanny.setPrivateKey(kp.getPrivKeyBytes());
				nanny.setType(nannyType);
				nanny.setExtId(extId);
				
				nanny = DIDNannyRepository.save(nanny);
				
				return nanny;
				
		    default:
		    	throw new Exception(method.toString() + " method not found");
		}
	}

}
