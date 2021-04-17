package edu.austral.ingsis;

import static edu.austral.ingsis.token.TokenType.*;

import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.token.TokenBuilder;
import edu.austral.ingsis.token.TokenType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PrintScriptLexer implements Lexer {

  private final List<Token> tokens = new ArrayList<>();

  private int line = 1;

  private final List<TokenType> patterns = new ArrayList<>();

  public PrintScriptLexer(String version) {
    addPatterns(version);
  }

  private void addPatterns(String version) {
    if (version.equals("1.0")) patterns.addAll(getV1_0Tokens());
    else patterns.addAll(Arrays.asList(values()));
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
  public List<Token> lex(String input) {
    Matcher matcher = getMatcher(input);
    int charCount = 0;

    while (matcher.find()) {
      int matchIndex = matcher.start();

      if (matcher.group().equals("\n")) {
        line++;
        charCount = matcher.end();
        continue;
      }

      checkInvalidGroup(input, charCount, matchIndex);
      extractToken(matcher);
    }
    checkGroupsRemaining(input, charCount);

    addToken(EOF, "", line, null);
    return tokens;
  }

  private void extractToken(Matcher matcher) {
    patterns.stream()
        .filter(tokenType -> matcher.group(tokenType.name()) != null)
        .findFirst()
        .map(
            tokenType -> {
              if (tokenType == NUMBER) {
                return addToken(
                    tokenType, matcher.group(), line, Double.parseDouble(matcher.group()));
              } else if (tokenType == STRING) {
                return addToken(
                    tokenType, matcher.group(), line, matcher.group().replaceAll("[\"']", ""));
              }
              return addToken(tokenType, matcher.group(), line, null);
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

  private Token addToken(TokenType type, String lexeme, int line, Object literal) {
    Token token =
        TokenBuilder.createBuilder()
            .addType(type)
            .addLine(line)
            .addLexeme(lexeme)
            .addLiteral(literal)
            .buildToken();

    tokens.add(token);

    return token;
  }
}
