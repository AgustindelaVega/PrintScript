package edu.austral.ingsis.parser.impl;

import edu.austral.ingsis.exception.ParseException;
import edu.austral.ingsis.expression.Expression;
import edu.austral.ingsis.expression.impl.AssigmentExpression;
import edu.austral.ingsis.parser.Parser;
import edu.austral.ingsis.statement.Statement;
import edu.austral.ingsis.statement.impl.DeclarationStatement;
import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.token.TokenType;

import static edu.austral.ingsis.token.TokenType.*;

import java.util.ArrayList;
import java.util.List;

public class PrintScriptParser implements Parser {

    private List<Token> tokens;
    private int current = 0;

    public PrintScriptParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public List<Statement> parse() {
        List<Statement> statements = new ArrayList<>();

        while(!isAtEnd()) {
            statements.add(declaration());
        }

        return statements;
    }

    private Expression assignment() {
        Expression expr = addition();

        if(match(ASSIGNATION)) {
            Token name = previous();
            Expression value = addition();

            return new AssigmentExpression(name, value);
        }

        return expr;
    }

    private Expression addition() {
        return null;
    }

    private Statement declaration() {
        if(match(LET)) return varDeclaration(previous());
        return statement();
    }

    private Statement statement() {
        if(match(PRINT)) return null;
        throw new ParseException("Parse error", previous());
    }

    private Statement varDeclaration(Token keyword) {
        Token name = consume(IDENTIFIER, "Variable name missing.");
        TokenType type = null;
        Expression expression = null;

        if(match(COLON)) {
            if(match(STRING_TYPE)) {
                type = STRING_TYPE;
            } else if(match(NUMBER_TYPE)) {
                type = NUMBER_TYPE;
            }
        } else {
            throw new ParseException("Variable type missing.", previous());
        }

        if(match(ASSIGNATION)) {
            expression = assignment();
        }

        consume(SEMICOLON, "';' after variable declaration missing.");
        return new DeclarationStatement(keyword, name, type, expression);
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

    private Token consume(TokenType type, String message) {
        if(check(type)) return advance();

        throw new ParseException(message, peek());
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
