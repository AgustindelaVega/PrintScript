package edu.austral.ingsis.token;

public enum TokenType {
  LET("let"),
  CONST("const"),
  NUMBER("-?[0-9.]+"),
  NUMBERTYPE("number"),
  STRINGTYPE("string"),
  BOOLEAN("boolean"),
  PRINT("print"),
  FALSE("false"),
  TRUE("true"),
  IF("if"),
  ELSE("else"),

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
  RIGHTPARENTHESIS("[)]"),
  LEFTBRACE("[{]"),
  RIGHTBRACE("[}]"),

  GREATER("[>]"),
  GREATEREQUAL(">="),
  LESS("[<]"),
  LESSEQUAL("<="),

  IDENTIFIER("[a-zA-Z]+\\w*");

  private final String regex;

  TokenType(String regex) {
    this.regex = regex;
  }

  public String getRegex() {
    return regex;
  }
}
