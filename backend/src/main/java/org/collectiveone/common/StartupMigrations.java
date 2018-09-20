package org.collectiveone.common;


import javax.transaction.Transactional;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupMigrations implements ApplicationListener<ContextRefreshedEvent> {
	
	@EventListener
	@Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
    }
}
