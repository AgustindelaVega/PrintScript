package edu.austral.ingsis.token;

public enum TokenType {
  LET("let"),
  CONST("const"),
  NUMBER("-?[0-9.]+"),
  NUMBERTYPE("number"),
  STRINGTYPE("string"),
  BOOLEAN("boolean"),
  PRINT("print"),
  IDENTIFIER("[a-zA-Z]+\\w*"),
  STRING("[\\\"'].*[\\\"']"),
  FALSE("false"),
  TRUE("true"),
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
