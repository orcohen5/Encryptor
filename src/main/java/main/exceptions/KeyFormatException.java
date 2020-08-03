package main.exceptions;

import org.springframework.stereotype.Component;

@Component
public class KeyFormatException extends Exception {
    public KeyFormatException(String message) {
        super(message);
    }

    public KeyFormatException() {}
}
