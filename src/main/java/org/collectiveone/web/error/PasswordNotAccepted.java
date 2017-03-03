package org.collectiveone.web.error;

public final class PasswordNotAccepted extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287163L;

    public PasswordNotAccepted() {
        super();
    }

    public PasswordNotAccepted(final String message) {
        super(message);
    }

}
