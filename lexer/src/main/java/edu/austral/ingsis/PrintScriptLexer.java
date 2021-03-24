package edu.austral.ingsis;

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

import static edu.austral.ingsis.token.TokenType.*;

public class PrintScriptLexer implements Lexer {

    private final List<Token> tokens = new ArrayList<>();

    private int line = 1;

    private final Map<TokenType, String> tokenTypePatterns = new EnumMap<>(TokenType.class);

    public PrintScriptLexer() {
        addPatterns();
    }

    private void addPatterns() {
        tokenTypePatterns.put(LET, "let");
        tokenTypePatterns.put(STRINGTYPE, "string");
        tokenTypePatterns.put(NUMBERTYPE, "number");
        tokenTypePatterns.put(PRINT, "print");

        tokenTypePatterns.put(STRING, "[\\\"'].*[\\\"']");
        tokenTypePatterns.put(NUMBER, "-?[0-9.]+");
        tokenTypePatterns.put(IDENTIFIER, "[a-zA-Z]+\\w*");

        tokenTypePatterns.put(SEMICOLON, ";");
        tokenTypePatterns.put(COLON, ":");
        tokenTypePatterns.put(NEWLINE, "\n");

        tokenTypePatterns.put(ASSIGNATION, "[=]");
        tokenTypePatterns.put(PLUS, "[+]");
        tokenTypePatterns.put(MINUS, "[-]");
        tokenTypePatterns.put(DIVIDE, "[/]");
        tokenTypePatterns.put(MULTIPLY, "[*]");

    }

    private Matcher getMatcher(String input) {
        return Pattern.compile(
                Arrays.stream(TokenType.values())
                        .map(tokenType -> String.format("|(?<%s>%s)", tokenType.name(), tokenTypePatterns.get(tokenType)))
                        .collect(Collectors.joining())
                        .substring(1)
        ).matcher(input);
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
        Matcher matcher = getMatcher(getFileLines(src));

        while (matcher.find()) {
            if(matcher.group().equals("\n")) {
                line++;
                continue;
            }
            tokenTypePatterns.keySet().stream()
                    .filter(tokenType -> matcher.group(tokenType.name()) != null)
                    .findFirst()
                    .map(tokenType -> {
                        if(tokenType == NUMBER){
                            return addToken(tokenType, matcher.group(), this.line, Double.parseDouble(matcher.group()));
                        }else if(tokenType == STRING) {
                            return addToken(tokenType, matcher.group(), this.line, matcher.group().replaceAll("[\"']", ""));
                        } else {
                            return addToken(tokenType, matcher.group(), this.line,null);
                        }
                    })
                    .orElseThrow(() -> new LexerException("Error matching group \" " + matcher.group() + " \"", this.line));
        }

        tokens.add(
                TokenBuilder.createBuilder()
                        .addType(EOF)
                        .addLine(this.line)
                        .addLexeme("")
                        .addLiteral(null)
                        .buildToken());
        return tokens;
    }

    private Token addToken(TokenType type, String lexeme, int line, Object literal) {
        Token token = TokenBuilder
                .createBuilder()
                .addType(type)
                .addLine(line)
                .addLexeme(lexeme)
                .addLiteral(literal)
                .buildToken();

        tokens.add(token);

        return token;
    }
}
