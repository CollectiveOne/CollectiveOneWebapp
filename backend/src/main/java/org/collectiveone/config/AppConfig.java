package org.collectiveone.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.sendgrid.SendGrid;

@Configuration
@EnableScheduling
public class AppConfig {
	
	@Autowired
	protected Environment env;
	
	@Bean
	public SendGrid sendGrid() {
		String key = System.getenv("SENDGRID_API_KEY");
		SendGrid sg = new SendGrid(key);
		sg.addRequestHeader("X-Mock", "true");
		return sg;
	}
	
}
