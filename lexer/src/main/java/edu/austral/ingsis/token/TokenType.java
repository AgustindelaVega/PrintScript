package edu.austral.ingsis.token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum TokenType {
  LET("let"),
  CONST("const"),
  PRINT("println"),
  FALSE("false"),
  TRUE("true"),
  IF("if"),
  ELSE("else"),
  NUMBER("[0-9.]+"),
  IDENTIFIER("(?:\\b[_a-zA-Z]|\\B\\$)[_$a-zA-Z0-9]*+"),
  NUMBERTYPE("number"),
  STRINGTYPE("string"),
  BOOLEAN("boolean"),

  STRING("[\"'].*[\"']"),
  SEMICOLON(";"),
  COLON(":"),

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
  GREATEREQUAL(">="),
  LESSEQUAL("<="),
  GREATER("[>]"),
  LESS("[<]"),
  ASSIGNATION("[=]");

  private final String regex;

  TokenType(String regex) {
    this.regex = regex;
  }

  public String getRegex() {
    return regex;
  }

  public static List<TokenType> getV1_1Tokens() {
    return new ArrayList<>(
        Arrays.asList(
            CONST,
            FALSE,
            TRUE,
            IF,
            ELSE,
            BOOLEAN,
            LEFTBRACE,
            RIGHTBRACE,
            GREATER,
            GREATEREQUAL,
            LESS,
            LESSEQUAL));
  }
}
