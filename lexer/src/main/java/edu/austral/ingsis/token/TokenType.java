package edu.austral.ingsis.token;

public enum TokenType {
    LET("let"),
    NUMBER("-?[0-9.]+"),
    NUMBERTYPE("number"),
    STRING("[\\\"'].*[\\\"']"),
    STRINGTYPE("string"),
    SEMICOLON(";"),
    COLON(":"),
    ASSIGNATION("[=]"),
    PRINT("print"),
    MINUS("[-]"),
    PLUS("[+]"),
    MULTIPLY("[*]"),
    DIVIDE("[/]"),
    IDENTIFIER("[a-zA-Z]+\\w*"),
    EOF(""),
    NEWLINE("\n"),
    LEFTPARENTHESIS("[(]"),
    RIGHTPARENTHESIS("[)]");

    private final String regex;

    TokenType(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }
}
