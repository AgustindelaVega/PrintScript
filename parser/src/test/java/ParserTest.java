import edu.austral.ingsis.expression.impl.AssigmentExpression;
import edu.austral.ingsis.expression.impl.BinaryExpression;
import edu.austral.ingsis.expression.impl.ValueExpression;
import edu.austral.ingsis.parser.Parser;
import edu.austral.ingsis.parser.impl.PrintScriptParser;
import edu.austral.ingsis.statement.Statement;
import edu.austral.ingsis.statement.impl.AssigmentStatement;
import edu.austral.ingsis.statement.impl.DeclarationStatement;
import edu.austral.ingsis.statement.impl.PrintStatement;
import edu.austral.ingsis.token.PrintScriptToken;
import edu.austral.ingsis.token.Token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static edu.austral.ingsis.token.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    private Parser parser;
    private List<Token> tokens;

    @BeforeEach
    public void setUp() {
        tokens = new ArrayList<>();
        parser = new PrintScriptParser(tokens);
    }

    @Test
    public void TestParseDeclarationStatement_ValueExpression() {
        tokens.add(new PrintScriptToken(LET, "let", "let", 1));
        tokens.add(new PrintScriptToken(IDENTIFIER, "x", "x", 1));
        tokens.add(new PrintScriptToken(COLON, ":", ":", 1));
        tokens.add(new PrintScriptToken(NUMBER_TYPE, "number", "number", 1));
        tokens.add(new PrintScriptToken(ASSIGNATION, "=", "=", 1));
        tokens.add(new PrintScriptToken(NUMBER, "10", 10, 1));
        tokens.add(new PrintScriptToken(SEMICOLON, ";", ";", 1));
        tokens.add(new PrintScriptToken(EOF, "", "", 1));

        List<Statement> statements = parser.parse();
        DeclarationStatement statement = (DeclarationStatement) statements.get(0);

        assertEquals(LET, statement.getKeyword().getType());
        assertEquals(IDENTIFIER, statement.getName().getType());
        assertEquals("x", statement.getName().getLexeme());
        assertEquals(NUMBER_TYPE, statement.getType());
        assertEquals(10, ((ValueExpression) statement.getExpression()).getValue());
    }

    @Test
    public void TestParseDeclarationStatement_BinaryExpression_Addition() {
        tokens.add(new PrintScriptToken(LET, "let", "let", 1));
        tokens.add(new PrintScriptToken(IDENTIFIER, "x", "x", 1));
        tokens.add(new PrintScriptToken(COLON, ":", ":", 1));
        tokens.add(new PrintScriptToken(NUMBER_TYPE, "number", "number", 1));
        tokens.add(new PrintScriptToken(ASSIGNATION, "=", "=", 1));
        tokens.add(new PrintScriptToken(NUMBER, "10", 10, 1));
        tokens.add(new PrintScriptToken(PLUS, "+", "+", 1));
        tokens.add(new PrintScriptToken(NUMBER, "9", 9, 1));
        tokens.add(new PrintScriptToken(SEMICOLON, ";", ";", 1));
        tokens.add(new PrintScriptToken(EOF, "", "", 1));

        List<Statement> statements = parser.parse();
        DeclarationStatement statement = (DeclarationStatement) statements.get(0);

        assertEquals(LET, statement.getKeyword().getType());
        assertEquals(IDENTIFIER, statement.getName().getType());
        assertEquals("x", statement.getName().getLexeme());
        assertEquals(NUMBER_TYPE, statement.getType());
        assertEquals(10, ((ValueExpression)((BinaryExpression) statement.getExpression()).getLeft()).getValue());
        assertEquals(PLUS, ((BinaryExpression) statement.getExpression()).getOperator().getType());
        assertEquals(9, ((ValueExpression)((BinaryExpression) statement.getExpression()).getRight()).getValue());
    }

    @Test
    public void TestParseDeclarationStatement_BinaryExpression_Multiplication() {
        tokens.add(new PrintScriptToken(LET, "let", "let", 1));
        tokens.add(new PrintScriptToken(IDENTIFIER, "x", "x", 1));
        tokens.add(new PrintScriptToken(COLON, ":", ":", 1));
        tokens.add(new PrintScriptToken(NUMBER_TYPE, "number", "number", 1));
        tokens.add(new PrintScriptToken(ASSIGNATION, "=", "=", 1));
        tokens.add(new PrintScriptToken(NUMBER, "10", 10, 1));
        tokens.add(new PrintScriptToken(MULTIPLY, "*", "*", 1));
        tokens.add(new PrintScriptToken(NUMBER, "9", 9, 1));
        tokens.add(new PrintScriptToken(SEMICOLON, ";", ";", 1));
        tokens.add(new PrintScriptToken(EOF, "", "", 1));

        List<Statement> statements = parser.parse();
        DeclarationStatement statement = (DeclarationStatement) statements.get(0);

        assertEquals(LET, statement.getKeyword().getType());
        assertEquals(IDENTIFIER, statement.getName().getType());
        assertEquals("x", statement.getName().getLexeme());
        assertEquals(NUMBER_TYPE, statement.getType());
        assertEquals(10, ((ValueExpression)((BinaryExpression) statement.getExpression()).getLeft()).getValue());
        assertEquals(MULTIPLY, ((BinaryExpression) statement.getExpression()).getOperator().getType());
        assertEquals(9, ((ValueExpression)((BinaryExpression) statement.getExpression()).getRight()).getValue());
    }

    @Test
    public void TestParsePrintStatement_ValueExpression() {
        tokens.add(new PrintScriptToken(PRINT, "print", "print", 1));
        tokens.add(new PrintScriptToken(NUMBER, "10", 10, 1));
        tokens.add(new PrintScriptToken(SEMICOLON, ";", ";", 1));
        tokens.add(new PrintScriptToken(EOF, "", "", 1));

        List<Statement> statements = parser.parse();
        PrintStatement statement = (PrintStatement) statements.get(0);

        assertTrue(statements.get(0) instanceof PrintStatement);
        assertEquals(10, ((ValueExpression) statement.getExpression()).getValue());
    }

    @Test
    public void TestParseAssigmentStatement_AssigmentExpression() {
        tokens.add(new PrintScriptToken(IDENTIFIER, "x", null, 1));
        tokens.add(new PrintScriptToken(ASSIGNATION, "=", null, 1));
        tokens.add(new PrintScriptToken(NUMBER, "10", 10, 1));
        tokens.add(new PrintScriptToken(SEMICOLON, ";", null, 1));
        tokens.add(new PrintScriptToken(EOF, "", null, 1));

        List<Statement> statements = parser.parse();
        AssigmentStatement statement = (AssigmentStatement) statements.get(0);

        assertTrue(statements.get(0) instanceof AssigmentStatement);
        assertEquals(IDENTIFIER, ((AssigmentExpression) statement.getExpression()).getName().getType());
        assertEquals("x", ((AssigmentExpression) statement.getExpression()).getName().getLexeme());
        assertEquals(10, ((ValueExpression)((AssigmentExpression) statement.getExpression()).getExpression()).getValue());
    }

    @Test
    public void TestParseMultipleStatements() {
        tokens.add(new PrintScriptToken(LET, "let", "let", 1));
        tokens.add(new PrintScriptToken(IDENTIFIER, "x", "x", 1));
        tokens.add(new PrintScriptToken(COLON, ":", ":", 1));
        tokens.add(new PrintScriptToken(NUMBER_TYPE, "number", "number", 1));
        tokens.add(new PrintScriptToken(SEMICOLON, ";", ";", 1));
        tokens.add(new PrintScriptToken(IDENTIFIER, "x", null, 1));
        tokens.add(new PrintScriptToken(ASSIGNATION, "=", null, 1));
        tokens.add(new PrintScriptToken(NUMBER, "10", 10, 1));
        tokens.add(new PrintScriptToken(SEMICOLON, ";", null, 1));
        tokens.add(new PrintScriptToken(EOF, "", null, 1));

        List<Statement> statements = parser.parse();
        DeclarationStatement declarationStatement = (DeclarationStatement) statements.get(0);
        AssigmentStatement assigmentStatement = (AssigmentStatement) statements.get(1);

        assertEquals(LET, declarationStatement.getKeyword().getType());
        assertEquals(IDENTIFIER, declarationStatement.getName().getType());
        assertEquals("x", declarationStatement.getName().getLexeme());
        assertEquals(NUMBER_TYPE, declarationStatement.getType());
        assertNull(declarationStatement.getExpression());
        assertTrue(statements.get(1) instanceof AssigmentStatement);
        assertEquals(IDENTIFIER, ((AssigmentExpression) assigmentStatement.getExpression()).getName().getType());
        assertEquals("x", ((AssigmentExpression) assigmentStatement.getExpression()).getName().getLexeme());
        assertEquals(10, ((ValueExpression)((AssigmentExpression) assigmentStatement.getExpression()).getExpression()).getValue());
    }
}