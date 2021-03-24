package edu.austral.ingsis;

public class LexerException extends RuntimeException {
    private final String message;
    private final Integer line;

    public LexerException(String message, Integer line) {
        this.message = message;
        this.line = line;
    }

    @Override
    public String getMessage() {
        return line + ": " + message;
    }
}
