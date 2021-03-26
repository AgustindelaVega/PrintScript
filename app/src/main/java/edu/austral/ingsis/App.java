package edu.austral.ingsis;

import edu.austral.ingsis.parser.Parser;
import edu.austral.ingsis.parser.impl.PrintScriptParser;

import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        Parser parser = new PrintScriptParser(new ArrayList<>());
        Interpreter interpreter = new Interpreter();
        System.out.println(lexer.getName());
        System.out.println(parser.parse());
        System.out.println(interpreter.getName());
    }
}
