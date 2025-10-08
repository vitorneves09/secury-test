package br.com.neves.blog.exception;

public class NotPermissionException extends RuntimeException {

    public NotPermissionException(String message) {
        super(message);
    }

    public NotPermissionException(String resource, String action) {
        super(String.format("You don't have permission to %s this %s", action, resource));
    }
}
