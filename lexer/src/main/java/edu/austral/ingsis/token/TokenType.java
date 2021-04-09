package edu.austral.ingsis.token;

public enum TokenType {
  LET("let"),
  NUMBER("-?[0-9.]+"),
  NUMBERTYPE("number"),
  STRINGTYPE("string"),
  PRINT("print"),
  IDENTIFIER("[a-zA-Z]+\\w*"),
  STRING("[\\\"'].*[\\\"']"),
  SEMICOLON(";"),
  COLON(":"),
  ASSIGNATION("[=]"),
  MINUS("[-]"),
  PLUS("[+]"),
  MULTIPLY("[*]"),
  DIVIDE("[/]"),
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
