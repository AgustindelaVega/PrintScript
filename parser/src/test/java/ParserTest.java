import edu.austral.ingsis.expression.impl.AssigmentExpression;
import edu.austral.ingsis.expression.impl.ValueExpression;
import edu.austral.ingsis.parser.Parser;
import edu.austral.ingsis.parser.impl.PrintScriptParser;
import edu.austral.ingsis.statement.Statement;
import edu.austral.ingsis.statement.impl.DeclarationStatement;
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
        tokens.add(new PrintScriptToken(LET, "let", "let", 1));
        tokens.add(new PrintScriptToken(IDENTIFIER, "x", "x", 1));
        tokens.add(new PrintScriptToken(COLON, ":", ":", 1));
        tokens.add(new PrintScriptToken(NUMBER_TYPE, "number", "number", 1));
        tokens.add(new PrintScriptToken(ASSIGNATION, "=", "=", 1));
        tokens.add(new PrintScriptToken(NUMBER, "10", 10, 1));
        tokens.add(new PrintScriptToken(SEMICOLON, ";", ";", 1));
        tokens.add(new PrintScriptToken(EOF, "", "", 1));
        parser = new PrintScriptParser(tokens);
    }

    @Test
    public void parseDeclarationStatement() {
        List<Statement> statements = parser.parse();
        DeclarationStatement statement = (DeclarationStatement) statements.get(0);

        assertEquals(LET, statement.getKeyword().getType());
        assertEquals(IDENTIFIER, statement.getName().getType());
        assertEquals(NUMBER_TYPE, statement.getType());
        assertEquals(10, ((ValueExpression) statement.getExpression()).getValue());
    }
}
