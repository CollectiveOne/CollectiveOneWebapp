package org.collectiveone.web.error;

public final class UserNotAuthorizedException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287163L;

    public UserNotAuthorizedException() {
        super();
    }

    public UserNotAuthorizedException(final String message) {
        super(message);
    }

}
