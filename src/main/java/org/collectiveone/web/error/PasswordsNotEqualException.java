package org.collectiveone.web.error;

public final class PasswordsNotEqualException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287163L;

    public PasswordsNotEqualException() {
        super();
    }

    public PasswordsNotEqualException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PasswordsNotEqualException(final String message) {
        super(message);
    }

    public PasswordsNotEqualException(final Throwable cause) {
        super(cause);
    }

}
