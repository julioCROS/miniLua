package syntatic;

import interpreter.command.Command;
import lexical.Lexeme;
import lexical.LexicalAnalysis;
import lexical.TokenType;

public class SyntaticAnalysis {

    private LexicalAnalysis lex;
    private Lexeme current;

    public SyntaticAnalysis(LexicalAnalysis lex) {
        this.lex = lex;
        this.current = lex.nextToken();
    }

    public Command start() {
        procCode();
        eat(TokenType.END_OF_FILE);

        return null;
    }

    private void advance() {
        System.out.println("Advanced (\"" + current.token + "\", " +
                current.type + ")");
        current = lex.nextToken();
    }

    private void eat(TokenType type) {
        System.out.println("Expected (..., " + type + "), found (\"" +
                current.token + "\", " + current.type + ")");
        if (type == current.type) {
            current = lex.nextToken();
        } else {
            showError();
        }
    }

    private void showError() {
        System.out.printf("%02d: ", lex.getLine());

        switch (current.type) {
            case INVALID_TOKEN:
                System.out.printf("Lexema inválido [%s]\n", current.token);
                break;
            case UNEXPECTED_EOF:
            case END_OF_FILE:
                System.out.printf("Fim de arquivo inesperado\n");
                break;
            default:
                System.out.printf("Lexema não esperado [%s]\n", current.token);
                break;
        }

        System.exit(1);
    }

    // <code> ::= { <cmd> }
    private void procCode() {
        while (current.type == TokenType.IF ||
                current.type == TokenType.WHILE ||
                current.type == TokenType.REPEAT ||
                current.type == TokenType.FOR ||
                current.type == TokenType.PRINT ||
                current.type == TokenType.ID) {
            procCmd();
        }
    }

    // <cmd> ::= (<if> | <while> | <repeat> | <for> | <print> | <assign>) [';']
    private void procCmd() {
        if (current.type == TokenType.IF) {
            procIf();
        } else if (current.type == TokenType.WHILE) {
            procWhile();
        } else if (current.type == TokenType.REPEAT) {
            procRepeat();
        } else if (current.type == TokenType.FOR) {
            procFor();
        } else if (current.type == TokenType.PRINT) {
            procPrint();
        } else if (current.type == TokenType.WHILE) {
            procWhile();
        } else if (current.type == TokenType.ID) {
            procAssign();
        } else {
            showError();
        }

        if (current.type == TokenType.SEMI_COLON) {
            advance();
        }
    }

    // <if> ::= if <expr> then <code> { elseif <expr> then <code> } [ else <code> ]
    // end
    private void procIf() {
        eat(TokenType.IF);
        procExpr();
        eat(TokenType.THEN);
        procCode();
        while (current.type == TokenType.ELSEIF) {
            advance();
            procExpr();
            eat(TokenType.THEN);
            procCode();
        }
        if (current.type == TokenType.ELSE) {
            advance();
            procCode();
        }
        eat(TokenType.END);
    }

    // <while> ::= while <expr> do <code> end
    private void procWhile() {
        eat(TokenType.WHILE);
        procExpr();
        eat(TokenType.DO);
        procCode();
        eat(TokenType.END);
    }

    // <repeat> ::= repeat <code> until <expr>
    private void procRepeat() {
        eat(TokenType.REPEAT);
        procCode();
        eat(TokenType.UNTIL);
        procExpr();
    }

    // <for> ::= for <name> (('=' <expr> ',' <expr> [',' <expr>]) | ([',' <name>] in
    // <expr>)) do <code> end
    private void procFor() {
        eat(TokenType.FOR);
        procName();
        if (current.type == TokenType.ASSIGN) {
            advance();
            procExpr();
            eat(TokenType.COLON);
            procExpr();
            if (current.type == TokenType.COLON) {
                advance();
                procExpr();
            }
        } else if (current.type == TokenType.COLON) {
            advance();
            procName();
            eat(TokenType.IN);
            procExpr();
        } else {
            showError();
        }
        eat(TokenType.DO);
        procCode();
        eat(TokenType.END);
    }

    // <print> ::= print '(' [ <expr> ] ')'
    private void procPrint() {
        eat(TokenType.PRINT);
        eat(TokenType.OPEN_PAR);
        if (current.type == TokenType.OPEN_PAR || current.type == TokenType.SUB
                || current.type == TokenType.HASH || current.type == TokenType.NOT || current.type == TokenType.NUMBER
                || current.type == TokenType.STRING || current.type == TokenType.FALSE
                || current.type == TokenType.TRUE || current.type == TokenType.NIL
                || current.type == TokenType.READ || current.type == TokenType.TONUMBER
                || current.type == TokenType.TOSTRING || current.type == TokenType.OPEN_CUR
                || current.type == TokenType.OPEN_PAR || current.type == TokenType.ID) {
            procExpr();
        } else {
            showError();
        }
        eat(TokenType.CLOSE_PAR);
    }

    // <assign> ::= <lvalue> { ',' <lvalue> } '=' <expr> { ',' <expr> }
    private void procAssign() {
        procLValue();
        while (current.type == TokenType.COLON) {
            advance();
            procLValue();
        }
        eat(TokenType.ASSIGN);
        procExpr();
        while (current.type == TokenType.COLON) {
            advance();
            procExpr();
        }
    }

    // <expr> ::= <rel> { (and | or) <rel> }
    private void procExpr() {
        procRel();
        while (current.type == TokenType.AND || current.type == TokenType.OR) {
            advance();
            procRel();
        }
    }

    // <rel> ::= <concat> [ ('<' | '>' | '<=' | '>=' | '~=' | '==') <concat> ]
    private void procRel() {
        procConcat();
        if (current.type == TokenType.LOWER_THAN || current.type == TokenType.GREATER_THAN
                || current.type == TokenType.LOWER_EQUAL || current.type == TokenType.GREATER_EQUAL
                || current.type == TokenType.NOT_EQUAL || current.type == TokenType.EQUAL) {
            procConcat();
        }
    }

    // <concat> ::= <arith> { '..' <arith> }
    private void procConcat() {
        procArith();
        while (current.type == TokenType.CONCAT) {
            advance();
            procArith();
        }
    }

    // <arith> ::= <term> { ('+' | '-') <term> }
    private void procArith() {
        procTerm();
        while (current.type == TokenType.ADD || current.type == TokenType.SUB) {
            advance();
            procTerm();
        }
    }

    // <term> ::= <factor> { ('*' | '/' | '%') <factor> }
    private void procTerm() {
        procFactor();
        while (current.type == TokenType.MUL || current.type == TokenType.DIV
                || current.type == TokenType.MOD) {
            advance();
            procFactor();
        }
    }

    // <factor> ::= '(' <expr> ')' | [ '-' | '#' | not] <rvalue>
    private void procFactor() {
        if (current.type == TokenType.OPEN_PAR) {
            procExpr();
            eat(TokenType.CLOSE_PAR);
        } else if (current.type == TokenType.SUB || current.type == TokenType.OPEN_PAR
                || current.type == TokenType.HASH || current.type == TokenType.NUMBER
                || current.type == TokenType.STRING || current.type == TokenType.FALSE
                || current.type == TokenType.TRUE || current.type == TokenType.NIL
                || current.type == TokenType.READ || current.type == TokenType.TONUMBER
                || current.type == TokenType.TOSTRING || current.type == TokenType.OPEN_CUR
                || current.type == TokenType.ID) {
            procRValue();
        }
    }

    // <lvalue> ::= <name> { '.' <name> | '[' <expr> ']' }
    private void procLValue() {
        procName();
        while (current.type == TokenType.DOT ||
                current.type == TokenType.OPEN_BRA) {
            if (current.type == TokenType.DOT) {
                advance();
                procName();
            } else if (current.type == TokenType.DOT) {
                advance();
                procExpr();
                eat(TokenType.CLOSE_BRA);
            } else {
                showError();
            }
        }
    }

    // <rvalue> ::= <const> | <function> | <table> | <lvalue>
    private void procRValue() {
        if (current.type == TokenType.NUMBER ||
                current.type == TokenType.STRING ||
                current.type == TokenType.FALSE ||
                current.type == TokenType.TRUE ||
                current.type == TokenType.NIL) {
            procConst();
        } else if (current.type == TokenType.READ ||
                current.type == TokenType.TONUMBER ||
                current.type == TokenType.TOSTRING) {
            procFunction();
        } else if (current.type == TokenType.OPEN_CUR) {
            procTable();
        } else if (current.type == TokenType.ID) {
            procLValue();
        } else {
            showError();
        }
    }

    // <const> ::= <number> | <string> | false | true | nil
    private void procConst() {
        if (current.type == TokenType.NUMBER) {
            procNumber();
        } else if (current.type == TokenType.STRING) {
            procString();
        } else if (current.type == TokenType.FALSE) {
            advance();
        } else if (current.type == TokenType.TRUE) {
            advance();
        } else if (current.type == TokenType.NIL) {
            advance();
        } else {
            showError();
        }
    }

    // <function> ::= (read | tonumber | tostring) '(' [ <expr> ] ')'
    private void procFunction() {
        if ((current.type == TokenType.READ) || (current.type == TokenType.TONUMBER)
                || (current.type == TokenType.TOSTRING)) {
            advance();
        } else {
            showError();
        }
        eat(TokenType.OPEN_PAR);
        if (current.type == TokenType.OPEN_PAR || current.type == TokenType.SUB
                || current.type == TokenType.HASH || current.type == TokenType.NOT || current.type == TokenType.NUMBER
                || current.type == TokenType.STRING || current.type == TokenType.FALSE
                || current.type == TokenType.TRUE || current.type == TokenType.NIL
                || current.type == TokenType.READ || current.type == TokenType.TONUMBER
                || current.type == TokenType.TOSTRING || current.type == TokenType.OPEN_CUR
                || current.type == TokenType.OPEN_PAR || current.type == TokenType.ID) {
            procExpr();
        }
        eat(TokenType.CLOSE_PAR);
    }

    // <table> ::= '{' [ <elem> { ',' <elem> } ] '}'
    private void procTable() {
        eat(TokenType.OPEN_CUR);
        if (current.type == TokenType.OPEN_PAR || current.type == TokenType.SUB
                || current.type == TokenType.HASH || current.type == TokenType.NOT || current.type == TokenType.NUMBER
                || current.type == TokenType.STRING || current.type == TokenType.FALSE
                || current.type == TokenType.TRUE || current.type == TokenType.NIL
                || current.type == TokenType.READ || current.type == TokenType.TONUMBER
                || current.type == TokenType.TOSTRING || current.type == TokenType.OPEN_CUR
                || current.type == TokenType.OPEN_PAR || current.type == TokenType.ID) {
            procElem();
            while (current.type == TokenType.COLON) {
                advance();
                procElem();
            }
        }
        eat(TokenType.CLOSE_CUR);

    }

    // <elem> ::= [ '[' <expr> ']' '=' ] <expr>
    private void procElem() {
        if (current.type == TokenType.OPEN_BRA) {
            procExpr();
            eat(TokenType.CLOSE_BRA);
            eat(TokenType.EQUAL);
        }
        procExpr();
    }

    private void procName() {
        eat(TokenType.ID);
    }

    private void procNumber() {
        eat(TokenType.NUMBER);
    }

    private void procString() {
        eat(TokenType.STRING);
    }

}
