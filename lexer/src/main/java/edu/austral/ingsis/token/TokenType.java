package edu.austral.ingsis.token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

  public static List<TokenType> getV1_0Tokens() {
    return new ArrayList<>(
        Arrays.asList(
            LET,
            NUMBER,
            NUMBERTYPE,
            STRINGTYPE,
            PRINT,
            STRING,
            SEMICOLON,
            COLON,
            ASSIGNATION,
            MINUS,
            PLUS,
            MULTIPLY,
            DIVIDE,
            EOF,
            NEWLINE,
            IDENTIFIER,
            LEFTPARENTHESIS,
            RIGHTPARENTHESIS));
  }
}
