package br.com.neves.blog.exception;

public class TokenExpiedException extends RuntimeException {

    public TokenExpiedException(String message) {
        super(message);
    }
}
