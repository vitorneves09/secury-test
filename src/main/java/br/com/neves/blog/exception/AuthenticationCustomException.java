package br.com.neves.blog.exception;

public class AuthenticationCustomException extends RuntimeException {
    public AuthenticationCustomException() {
        super("Credentials incorrect");
    }

    public AuthenticationCustomException(String messages) {
        super(messages);
    }
}
