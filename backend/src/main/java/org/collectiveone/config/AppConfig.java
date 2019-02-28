package org.collectiveone.config;

import java.security.GeneralSecurityException;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.sendgrid.SendGrid;

import nl.martijndwars.webpush.PushService;

@Configuration
@EnableScheduling
public class AppConfig {
	
	@Autowired
	protected Environment env;
	
	@Value("${PUSH_PUBLIC_KEY}")
	private String pubKey;
	
	@Value("${PUSH_PRIVATE_KEY}")
	private String privKey;
	
	@Value("${PUSH_SUBJECT}")
	private String subject;
	
	@Bean
	public SendGrid sendGrid() {
		String key = System.getenv("SENDGRID_API_KEY");
		SendGrid sg = new SendGrid(key);
		sg.addRequestHeader("X-Mock", "true");
		return sg;
	}
	
	@Bean
	public PushService pushService() throws GeneralSecurityException {
		Security.addProvider(new BouncyCastleProvider());
		PushService pushService = new PushService(pubKey, privKey, subject);
		return pushService;		
	}
	
}
