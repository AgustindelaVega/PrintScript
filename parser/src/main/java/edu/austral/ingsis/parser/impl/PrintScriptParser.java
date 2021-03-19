package edu.austral.ingsis.parser.impl;

import edu.austral.ingsis.parser.Parser;
import edu.austral.ingsis.statement.Statement;
import edu.austral.ingsis.token.Token;

import java.util.List;

public class PrintScriptParser implements Parser {
    private List<Token> tokens;

    public PrintScriptParser(List<Token> tokens) {
        this.tokens = tokens;
    }


    @Override
    public List<Statement> parse() {
        return null;
    }
}
