package edu.austral.ingsis;

import edu.austral.ingsis.parser.Parser;

public class App {
    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Interpreter interpreter = new Interpreter();
        System.out.println(lexer.getName());
        System.out.println(parser.getName());
        System.out.println(interpreter.getName());
    }
}
