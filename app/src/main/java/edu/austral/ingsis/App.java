package edu.austral.ingsis;

import edu.austral.ingsis.parser.Parser;
import edu.austral.ingsis.parser.impl.PrintScriptParser;

import java.util.ArrayList;

import edu.austral.ingsis.token.Token;

import java.util.List;

public class App {
    public static void main(String[] args) {
        Parser parser = new PrintScriptParser(new ArrayList<>());
        Lexer lexer = new PrintScriptLexer();
        List<Token> tokens = lexer.lex("./app/src/main/resources/file.txt");

    }
}
