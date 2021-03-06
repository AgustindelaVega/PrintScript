package edu.austral.ingsis;

import static edu.austral.ingsis.token.TokenType.*;

import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.token.TokenBuilder;
import edu.austral.ingsis.token.TokenType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PrintScriptLexer implements Lexer {

  private final List<Token> tokens = new ArrayList<>();

  private int line = 1;

  private final List<TokenType> patterns = new ArrayList<>();

  private String version;

  public PrintScriptLexer() {

    addPatterns();
  }

  private void addPatterns() {
    patterns.addAll(Arrays.asList(values()));
    patterns.remove(EOF);
  }

  private Matcher getMatcher(String input) {
    return Pattern.compile(
            patterns.stream()
                .map(
                    tokenType ->
                        String.format("|(?<%s>%s)", tokenType.name(), tokenType.getRegex()))
                .collect(Collectors.joining())
                .substring(1))
        .matcher(input);
  }

  @Override
  public List<Token> lex(String input, String version) {
    this.version = version;
    Matcher matcher = getMatcher(input);
    int charCount = 0;
    int columnCount = 0;

    while (matcher.find()) {
      int matchIndex = matcher.start();

      if (matcher.group().equals("\n")) {
        line++;
        columnCount = 0;
        charCount = matcher.end();
        continue;
      }

      checkInvalidGroup(input, charCount, matchIndex);
      List<Integer> column = new ArrayList<>();

      extractToken(matcher, column::add, columnCount);
      columnCount += column.get(0);
    }
    checkGroupsRemaining(input, charCount);

    addToken(EOF, "", line, null, 0);
    if (version.equals("1.0")
        && tokens.stream().map(Token::getType).anyMatch(getV1_1Tokens()::contains)) {
      throw new LexerException("Group not supported by version 1.0", line, columnCount);
    }

    return tokens;
  }

  private void extractToken(Matcher matcher, Consumer<Integer> columnIncrease, int columnCount) {
    patterns.stream()
        .filter(tokenType -> matcher.group(tokenType.name()) != null)
        .findFirst()
        .map(
            tokenType -> {
              columnIncrease.accept(matcher.group().length());
              if (tokenType == NUMBER) {
                return addToken(
                    tokenType,
                    matcher.group(),
                    line,
                    Double.parseDouble(matcher.group()),
                    columnCount);
              } else if (tokenType == STRING) {
                return addToken(
                    tokenType,
                    matcher.group(),
                    line,
                    matcher.group().replaceAll("[\"']", ""),
                    columnCount);
              } else if (tokenType == IDENTIFIER && matcher.group().equals("number")) {
                return addToken(NUMBERTYPE, matcher.group(), line, null, columnCount);
              } else if (tokenType == IDENTIFIER && matcher.group().equals("string")) {
                return addToken(STRINGTYPE, matcher.group(), line, null, columnCount);
              } else if (tokenType == IDENTIFIER && matcher.group().equals("boolean")) {
                return addToken(BOOLEAN, matcher.group(), line, null, columnCount);
              }
              return addToken(tokenType, matcher.group(), line, null, columnCount);
            });
  }

  private void checkGroupsRemaining(String input, int charCount) {
    if (!tokens.isEmpty()) {
      int lastIndex = input.lastIndexOf(tokens.get(tokens.size() - 1).getLexeme()) + 1;

      if (!input
          .substring(lastIndex)
          .trim()
          .isEmpty()) // characters remaining after last valid token
      throw new LexerException(
            "Error matching group \" " + input.substring(lastIndex) + " \"",
            line,
            lastIndex - charCount);
    } else if (!input.trim().isEmpty()) { // no tokens recognized and characters left
      throw new LexerException("Error matching group \" " + input + " \"", line, input.length());
    }
  }

  private void checkInvalidGroup(String input, int charCount, int matchIndex) {
    if (!tokens.isEmpty()) {
      String lastLexeme = tokens.get(tokens.size() - 1).getLexeme();
      String processedInput = input.substring(0, matchIndex);
      int endIndex = processedInput.lastIndexOf(lastLexeme) + lastLexeme.length();

      if (endIndex != matchIndex
          && !input
              .substring(endIndex, matchIndex)
              .trim()
              .isEmpty()) { // characters remaining between last token and actual match
        throw new LexerException(
            "Error matching group \" " + input.substring(endIndex, matchIndex) + " \"",
            line,
            endIndex - charCount);
      }
    }
  }

  private Token addToken(TokenType type, String lexeme, int line, Object literal, int column) {
    Token token =
        TokenBuilder.createBuilder()
            .addType(type)
            .addLine(line)
            .addColumn(column)
            .addLexeme(lexeme)
            .addLiteral(literal)
            .buildToken();

    tokens.add(token);

    return token;
  }
}
