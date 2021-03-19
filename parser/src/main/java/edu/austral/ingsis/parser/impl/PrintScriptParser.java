package edu.austral.ingsis.parser.impl;

import edu.austral.ingsis.exception.ParseException;
import edu.austral.ingsis.parser.Parser;
import edu.austral.ingsis.statement.Statement;
import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.token.TokenType;

import static edu.austral.ingsis.token.TokenType.*;

import java.util.List;

public class PrintScriptParser implements Parser {

    private List<Token> tokens;
    private int current = 0;

    public PrintScriptParser(List<Token> tokens) {
        this.tokens = tokens;
    }


    @Override
    public List<Statement> parse() {
        return null;
    }

    private Statement declaration() {
        if(match(LET)) return null;
        return statement();
    }

    private Statement statement() {
        if(match(PRINT)) return null;
        throw new ParseException("Parse error", previous());
    }


    private boolean match(TokenType... types) {
        for(TokenType type : types) {
            if(check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private boolean check(TokenType type) {
        if(isAtEnd()) return false;
        return peek().getType() == type;
    }

    private Token advance() {
        if(!isAtEnd()) current++;
        return previous();
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private boolean isAtEnd() {
        return peek().getType() == EOF;
    }
}
