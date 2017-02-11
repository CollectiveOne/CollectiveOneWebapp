package org.collectiveone.slack;

import org.collectiveone.model.User;
import org.springframework.context.ApplicationEvent;

public class OnSlackPublishAsked extends ApplicationEvent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String message;
    
    public OnSlackPublishAsked(final User user, final String message) {
        super(user);
    	this.message = message;
    }

	public String getMessage() {
		return message;
	}

    
}
