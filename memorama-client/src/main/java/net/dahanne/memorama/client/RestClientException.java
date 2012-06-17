package net.dahanne.memorama.client;

public class RestClientException extends Exception {

    private static final long serialVersionUID = 1849841477339158124L;

    public RestClientException(String message) {
        super(message);
    }

    public RestClientException(Throwable cause) {
        super(cause);
    }

}
