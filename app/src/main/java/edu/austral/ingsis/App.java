package edu.austral.ingsis;

import edu.austral.ingsis.token.Token;

import java.util.List;

public class App {
    public static void main(String[] args) {
        Lexer lexer = new PrintScriptLexer();
        List<Token> tokens = lexer.lex("./app/src/main/resources/file.txt");

    }
}
