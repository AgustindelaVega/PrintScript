package edu.austral.ingsis;

import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.token.TokenType;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class LexerTest {

    @Test
    public void testLexerTokens(){
        Lexer lexer = new PrintScriptLexer();

        List<Token> tokens = lexer.lex("./src/test/resources/lexer_test01.txt");

        Assert.assertEquals(15, tokens.size());

        Assert.assertEquals(TokenType.LET, tokens.get(0).getType());
        Assert.assertEquals(TokenType.IDENTIFIER, tokens.get(1).getType());
        Assert.assertEquals("str", tokens.get(1).getLexeme());
        Assert.assertEquals(TokenType.COLON, tokens.get(2).getType());
        Assert.assertEquals(TokenType.STRINGTYPE, tokens.get(3).getType());
        Assert.assertEquals(TokenType.ASSIGNATION, tokens.get(4).getType());
        Assert.assertEquals(TokenType.STRING, tokens.get(5).getType());
        Assert.assertEquals("test string", tokens.get(5).getLiteral());
        Assert.assertEquals(TokenType.SEMICOLON, tokens.get(6).getType());

        Assert.assertEquals(TokenType.LET, tokens.get(7).getType());
        Assert.assertEquals(TokenType.IDENTIFIER, tokens.get(8).getType());
        Assert.assertEquals("num", tokens.get(8).getLexeme());
        Assert.assertEquals(TokenType.COLON, tokens.get(9).getType());
        Assert.assertEquals(TokenType.NUMBERTYPE, tokens.get(10).getType());
        Assert.assertEquals(TokenType.ASSIGNATION, tokens.get(11).getType());
        Assert.assertEquals(TokenType.NUMBER, tokens.get(12).getType());
        Assert.assertEquals(13.0, tokens.get(12).getLiteral());
        Assert.assertEquals(TokenType.SEMICOLON, tokens.get(13).getType());

        Assert.assertEquals(TokenType.EOF, tokens.get(14).getType());
    }

    @Test
    public void testLexerTokens2(){
        Lexer lexer = new PrintScriptLexer();
        List<Token> tokens = lexer.lex("./src/test/resources/lexer_test02.txt");


        Assert.assertEquals(13, tokens.size());

        Assert.assertEquals(TokenType.PRINT, tokens.get(0).getType());
        Assert.assertEquals(TokenType.STRING, tokens.get(1).getType());
        Assert.assertEquals("test", tokens.get(1).getLiteral());
        Assert.assertEquals(TokenType.SEMICOLON, tokens.get(2).getType());

        Assert.assertEquals(TokenType.LET, tokens.get(3).getType());
        Assert.assertEquals(TokenType.IDENTIFIER, tokens.get(4).getType());
        Assert.assertEquals("num", tokens.get(4).getLexeme());
        Assert.assertEquals(TokenType.COLON, tokens.get(5).getType());
        Assert.assertEquals(TokenType.NUMBERTYPE, tokens.get(6).getType());
        Assert.assertEquals(TokenType.ASSIGNATION, tokens.get(7).getType());
        Assert.assertEquals(TokenType.NUMBER, tokens.get(8).getType());
        Assert.assertEquals(14.0, tokens.get(8).getLiteral());
        Assert.assertEquals(TokenType.PLUS, tokens.get(9).getType());
        Assert.assertEquals(TokenType.NUMBER, tokens.get(10).getType());
        Assert.assertEquals(12.0, tokens.get(10).getLiteral());
        Assert.assertEquals(TokenType.SEMICOLON, tokens.get(11).getType());

        Assert.assertEquals(TokenType.EOF, tokens.get(12).getType());
    }
}