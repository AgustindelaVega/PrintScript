package edu.austral.ingsis;

import static edu.austral.ingsis.token.TokenType.*;

import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.token.TokenBuilder;
import edu.austral.ingsis.token.TokenType;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PrintScriptLexer implements Lexer {

  private final List<Token> tokens = new ArrayList<>();

  private int line = 1;

  private final Map<TokenType, String> tokenTypePatterns = new EnumMap<>(TokenType.class);

  public PrintScriptLexer() {
    addPatterns();
  }

  private void addPatterns() {
    for (TokenType value : values()) {
      if (value != EOF) tokenTypePatterns.put(value, value.getRegex());
    }
  }

  private Matcher getMatcher(String input) {
    return Pattern.compile(
            Arrays.stream(TokenType.values())
                .map(
                    tokenType ->
                        String.format(
                            "|(?<%s>%s)", tokenType.name(), tokenTypePatterns.get(tokenType)))
                .collect(Collectors.joining())
                .substring(1))
        .matcher(input);
  }

  private String getFileLines(String src) {
    InputStreamReader inputReader = null;
    try {
      inputReader = new InputStreamReader(new FileInputStream(src));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    assert inputReader != null;
    return new BufferedReader(inputReader).lines().collect(Collectors.joining("\n"));
  }

  @Override
  public List<Token> lex(String src) {
    String input = getFileLines(src);
    Matcher matcher = getMatcher(input);
    int charCount = 0;

    while (matcher.find()) {
      String match = matcher.group();
      int matchIndex = matcher.start();
      if (match.equals("\n")) {
        line++;
        charCount = matcher.end();
        continue;
      }
      if (!tokens.isEmpty()) {
        String lexeme = tokens.get(tokens.size() - 1).getLexeme();
        String processedInput = input.substring(0, matchIndex);
        int endIndex = processedInput.lastIndexOf(lexeme) + lexeme.length();
        if (endIndex != matchIndex && !input.substring(endIndex, matchIndex).trim().isEmpty()) {
          throw new LexerException(
              "Error matching group \" " + input.substring(endIndex, matchIndex) + " \"",
              line,
              endIndex - charCount);
        }
      }
      tokenTypePatterns.keySet().stream()
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
                } else {
                  return addToken(tokenType, matcher.group(), line, null);
                }
              })
          .orElseThrow(
              () -> new LexerException("Error matching group \" " + matcher.group(), line));
    }
    if (!tokens.isEmpty()) {
      int lastIndex = input.lastIndexOf(tokens.get(tokens.size() - 1).getLexeme()) + 1;
      if (!input.substring(lastIndex).trim().isEmpty())
        throw new LexerException(
            "Error matching group \" " + input.substring(lastIndex) + " \"",
            line,
            lastIndex - charCount);
    } else if (!input.trim().isEmpty()) {
      throw new LexerException("Error matching group \" " + input + " \"", line, input.length());
    }

    tokens.add(
        TokenBuilder.createBuilder()
            .addType(EOF)
            .addLine(line)
            .addLexeme("")
            .addLiteral(null)
            .buildToken());
    return tokens;
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
