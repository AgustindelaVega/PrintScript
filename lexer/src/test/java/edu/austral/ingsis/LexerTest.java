package edu.austral.ingsis;

import static org.junit.Assert.assertThrows;

import edu.austral.ingsis.token.Token;
import edu.austral.ingsis.token.TokenType;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class LexerTest {

  @Test
  public void testLexerValidInput() {
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
  public void testLexerValidInput2() {
    Lexer lexer = new PrintScriptLexer();
    List<Token> tokens = lexer.lex("./src/test/resources/lexer_test02.txt");

    Assert.assertEquals(15, tokens.size());
    // print("test");
    // let num: number = 14 + 12;

    Assert.assertEquals(TokenType.PRINT, tokens.get(0).getType());
    Assert.assertEquals(TokenType.LEFTPARENTHESIS, tokens.get(1).getType());
    Assert.assertEquals(TokenType.STRING, tokens.get(2).getType());
    Assert.assertEquals("test", tokens.get(2).getLiteral());
    Assert.assertEquals(TokenType.RIGHTPARENTHESIS, tokens.get(3).getType());
    Assert.assertEquals(TokenType.SEMICOLON, tokens.get(4).getType());

    Assert.assertEquals(TokenType.LET, tokens.get(5).getType());
    Assert.assertEquals(TokenType.IDENTIFIER, tokens.get(6).getType());
    Assert.assertEquals("num", tokens.get(6).getLexeme());
    Assert.assertEquals(TokenType.COLON, tokens.get(7).getType());
    Assert.assertEquals(TokenType.NUMBERTYPE, tokens.get(8).getType());
    Assert.assertEquals(TokenType.ASSIGNATION, tokens.get(9).getType());
    Assert.assertEquals(TokenType.NUMBER, tokens.get(10).getType());
    Assert.assertEquals(14.0, tokens.get(10).getLiteral());
    Assert.assertEquals(TokenType.PLUS, tokens.get(11).getType());
    Assert.assertEquals(TokenType.NUMBER, tokens.get(12).getType());
    Assert.assertEquals(12.0, tokens.get(12).getLiteral());
    Assert.assertEquals(TokenType.SEMICOLON, tokens.get(13).getType());

    Assert.assertEquals(TokenType.EOF, tokens.get(14).getType());
  }

  @Test
  public void testLexerInvalidInput() {
    Lexer lexer = new PrintScriptLexer();
    Exception exception =
        assertThrows(
            LexerException.class, () -> lexer.lex("./src/test/resources/lexer_test03.txt"));

    String expectedMessage = "(1, 13) : Error matching group \" {} \"";
    String actualMessage = exception.getMessage();

    Assert.assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void testLexerInvalidInput2() {
    Lexer lexer = new PrintScriptLexer();
    Exception exception =
        assertThrows(
            LexerException.class, () -> lexer.lex("./src/test/resources/lexer_test04.txt"));

    String expectedMessage = "(2, 15) : Error matching group \"  ! \"";
    String actualMessage = exception.getMessage();

    Assert.assertTrue(actualMessage.contains(expectedMessage));
  }
}
