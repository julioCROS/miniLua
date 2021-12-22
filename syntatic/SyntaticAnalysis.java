package syntatic;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import interpreter.command.Command;
import interpreter.command.AssignCommand;
import interpreter.command.BlocksCommand;
import interpreter.command.PrintCommand;
import interpreter.command.WhileCommand;
import interpreter.expr.ConstExpr;
import interpreter.expr.Expr;
import interpreter.expr.UnaryExpr;
import interpreter.expr.SetExpr;
import interpreter.expr.TableEntry;
import interpreter.expr.TableExpr;
import interpreter.expr.UnaryExpr;
import interpreter.expr.UnaryOp;
import interpreter.expr.Variable;
import interpreter.expr.Variable;
import interpreter.value.BooleanValue;
import interpreter.value.NumberValue;
import interpreter.value.StringValue;
import interpreter.value.Value;
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
        BlocksCommand cmds = procCode();
        eat(TokenType.END_OF_FILE);
        return cmds;
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
                System.out.printf("Fim de arquivo inesperado\n");
                break;
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
    private BlocksCommand procCode() {
        int line = lex.getLine();
        List<Command> cmds = new ArrayList<Command>();

        while (current.type == TokenType.IF ||
                current.type == TokenType.WHILE ||
                current.type == TokenType.REPEAT ||
                current.type == TokenType.FOR ||
                current.type == TokenType.PRINT ||
                current.type == TokenType.ID) {
            Command cmd = procCmd();
            cmds.add(cmd);
        }

        BlockCommands bc = new BlockCommands(line, cmds);
        return bc;
    }

    // <cmd> ::= (<if> | <while> | <repeat> | <for> | <print> | <assign>) [';']
    private Command procCmd() {
        Command cmd = null;
        if (current.type == TokenType.IF) {
            procIf();
        } else if (current.type == TokenType.WHILE) {
            cmd = procWhile();
        } else if (current.type == TokenType.REPEAT) {
            procRepeat();
        } else if (current.type == TokenType.FOR) {
            procFor();
        } else if (current.type == TokenType.PRINT) {
            cmd = procPrint();
        } else if (current.type == TokenType.ID) {
            cmd = procAssign();
        } else {
            showError();
        }

        if (current.type == TokenType.SEMI_COLON) {
            advance();
        }

        return cmd;
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
    private WhileCommand procWhile() {
        eat(TokenType.WHILE);

        int line = lex.getLine();
        Expr expr = procExpr();

        eat(TokenType.DO);

        Command cmd = procCode();

        eat(TokenType.END);

        WhileCommand wc = new WhileCommand(line, expr, cmd);
        return wc;
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
    private PrintCommand procPrint() {
        eat(TokenType.PRINT);
        int line = lex.getLine();
        eat(TokenType.OPEN_PAR);
        Expr expr = null;
        if (current.type == TokenType.OPEN_PAR || current.type == TokenType.SUB
                || current.type == TokenType.HASH || current.type == TokenType.NOT
                || current.type == TokenType.NUMBER || current.type == TokenType.STRING
                || current.type == TokenType.FALSE || current.type == TokenType.TRUE
                || current.type == TokenType.NIL || current.type == TokenType.READ
                || current.type == TokenType.TONUMBER || current.type == TokenType.TOSTRING
                || current.type == TokenType.OPEN_CUR || current.type == TokenType.ID) {
            expr = procExpr();
        }
        eat(TokenType.CLOSE_PAR);

        PrintCommand pc = new PrintCommand(line, expr);
        return pc;
    }

    // <assign> ::= <lvalue> { ',' <lvalue> } '=' <expr> { ',' <expr> }
    private AssignCommand procAssign() {
        int line = lex.getLine();
        Vector<SetExpr> lhs = new Vector<SetExpr>();
        Vector<Expr> rhs = new Vector<Expr>();

        lhs.add(procLValue());
        while (current.type == TokenType.COLON) {
            advance();
            lhs.add(procLValue());
        }
        eat(TokenType.ASSIGN);
        procExpr();
        while (current.type == TokenType.COLON) {
            advance();
            rhs.add(procExpr());
        }

        AssignCommand ac = new AssignCommand(line, lhs, rhs);
        return ac;
    }

    // <expr> ::= <rel> { (and | or) <rel> }
    private Expr procExpr() {
        Expr expr = procRel();
        while (current.type == TokenType.AND || current.type == TokenType.OR
                || current.type == TokenType.LOWER_THAN || current.type == TokenType.GREATER_THAN
                || current.type == TokenType.LOWER_EQUAL || current.type == TokenType.GREATER_EQUAL
                || current.type == TokenType.NOT_EQUAL || current.type == TokenType.EQUAL
                || current.type == TokenType.ADD || current.type == TokenType.SUB
                || current.type == TokenType.MUL || current.type == TokenType.DIV
                || current.type == TokenType.MOD || current.type == TokenType.HASH) {
            advance();
            // FIXME: Implement me!
            procRel();
        }

        return expr;
    }

    // <rel> ::= <concat> [ ('<' | '>' | '<=' | '>=' | '~=' | '==') <concat> ]
    private Expr procRel() {
        Expr expr = procConcat();
        // FIXME: Implement me!
        if (current.type == TokenType.LOWER_THAN || current.type == TokenType.GREATER_THAN
                || current.type == TokenType.LOWER_EQUAL || current.type == TokenType.GREATER_EQUAL
                || current.type == TokenType.NOT_EQUAL || current.type == TokenType.EQUAL) {
            advance();
            procConcat();
        }
        return expr;
    }

    // <concat> ::= <arith> { '..' <arith> }
    private Expr procConcat() {
        Expr expr = procArith();
        // FIXME: Implement me!
        while (current.type == TokenType.CONCAT) {
            advance();
            procArith();
        }

        return expr;
    }

    // <arith> ::= <term> { ('+' | '-') <term> }
    private Expr procArith() {
        Expr expr = procTerm();
        // FIXME: Implement me!
        while (current.type == TokenType.ADD || current.type == TokenType.SUB) {
            advance();
            procTerm();
        }

        return expr;
    }

    // <term> ::= <factor> { ('*' | '/' | '%') <factor> }
    private Expr procTerm() {
        Expr expr = procFactor();
        // FIXME: Implement me!
        while (current.type == TokenType.MUL || current.type == TokenType.DIV
                || current.type == TokenType.MOD) {
            advance();
            procFactor();
        }

        return expr;
    }

    // <factor> ::= '(' <expr> ')' | [ '-' | '#' | not] <rvalue>
    private Expr procFactor() {
        Expr expr = null;
        if (current.type == TokenType.OPEN_PAR) {
            advance();
            procExpr();
            eat(TokenType.CLOSE_PAR);
            // FIXME: Implement me!
        } else {
            UnaryOp op = null;
            if (current.type == TokenType.HASH) {
                advance();
                op = UnaryOp.Size;
            } else if (current.type == TokenType.SUB) {
                advance();
                op = UnaryOp.Neg;
            } else if (current.type == TokenType.NOT) {
                advance();
                op = UnaryOp.Not;
            }
            int line = lex.getLine();
            expr = procRValue();

            if (op != null) {
                expr = new UnaryExpr(line, expr, op);
            }

        }

        return expr;
    }

    // <lvalue> ::= <name> { '.' <name> | '[' <expr> ']' }
    private SetExpr procLValue() {
        Variable var = procName();
        while (current.type == TokenType.DOT ||
                current.type == TokenType.OPEN_BRA) {
            if (current.type == TokenType.DOT) {
                advance();
                String name = procName().getName();
                int line = lex.getLine();

                StringValue sv = new StringValue(name);
                ConstExpr index = new ConstExpr(line, sv);

                // FIXME: Implement me!
            } else if (current.type == TokenType.OPEN_BRA) {
                advance();
                procExpr();
                eat(TokenType.CLOSE_BRA);
            }
        }

        return var;
    }

    // <rvalue> ::= <const> | <function> | <table> | <lvalue>
    private Expr procRValue() {
        Expr expr = null;
        if (current.type == TokenType.NUMBER ||
                current.type == TokenType.STRING ||
                current.type == TokenType.FALSE ||
                current.type == TokenType.TRUE ||
                current.type == TokenType.NIL) {
            Value<?> v = procConst();
            int line = lex.getLine();
            expr = new ConstExpr(line, v);

        } else if (current.type == TokenType.READ ||
                current.type == TokenType.TONUMBER ||
                current.type == TokenType.TOSTRING) {
            expr = procFunction();

        } else if (current.type == TokenType.OPEN_CUR) {
            expr = procTable();

        } else if (current.type == TokenType.ID) {
            expr = procLValue();
        } else {
            showError();
        }

        return expr;
    }

    // <const> ::= <number> | <string> | false | true | nil
    private Value<?> procConst() {
        Value<?> v = null;
        if (current.type == TokenType.NUMBER) {
            v = procNumber();

        } else if (current.type == TokenType.STRING) {
            v = procString();

        } else if (current.type == TokenType.FALSE) {
            advance();
            v = new BooleanValue(false);

        } else if (current.type == TokenType.TRUE) {
            advance();
            v = new BooleanValue(true);

        } else if (current.type == TokenType.NIL) {
            advance();
            v = null;

        } else {
            showError();
        }

        return v;
    }

    // <function> ::= (read | tonumber | tostring) '(' [ <expr> ] ')'
    private UnaryExpr procFunction() {
        UnaryOp op = null;
        if (current.type == TokenType.READ) {
            op = UnaryOp.Read;
            advance();
        } else if (current.type == TokenType.TONUMBER) {
            op = UnaryOp.ToNumber;
            advance();
        } else if (current.type == TokenType.TOSTRING) {
            op = UnaryOp.ToString;
            advance();
        } else {
            showError();
        }
        int line = lex.getLine();
        eat(TokenType.OPEN_PAR);
        Expr expr = null;
        if (current.type == TokenType.OPEN_PAR || current.type == TokenType.SUB
                || current.type == TokenType.HASH || current.type == TokenType.NOT
                || current.type == TokenType.NUMBER || current.type == TokenType.STRING
                || current.type == TokenType.FALSE || current.type == TokenType.TRUE
                || current.type == TokenType.NIL || current.type == TokenType.READ
                || current.type == TokenType.TONUMBER || current.type == TokenType.TOSTRING
                || current.type == TokenType.OPEN_CUR || current.type == TokenType.ID) {
            expr = procExpr();
        }
        eat(TokenType.CLOSE_PAR);
        UnaryExpr uexpr = new UnaryExpr(line, expr, op);
        return uexpr;
    }

    // <table> ::= '{' [ <elem> { ',' <elem> } ] '}'
    private TableExpr procTable() {
        eat(TokenType.OPEN_CUR);

        int line = lex.getLine();
        TableExpr texpr = new TableExpr(line);

        // FIXME: Implement me!

        if (current.type == TokenType.OPEN_BRA) {
            procElem();
            while (current.type == TokenType.COLON) {
                advance();
                procElem();
            }
        }
        eat(TokenType.CLOSE_CUR);

        return texpr;

    }

    // <elem> ::= [ '[' <expr> ']' '=' ] <expr>
    private TableEntry procElem() {
        // FIXME: implement me!
        if (current.type == TokenType.OPEN_BRA) {
            advance();
            procExpr();
            eat(TokenType.CLOSE_BRA);
            eat(TokenType.ASSIGN);
        }
        procExpr();
        return null;
    }

    private Variable procName() {
        String name = current.token;
        eat(TokenType.ID);
        int line = lex.getLine();
        Variable var = new Variable(line, name);
        return var;
    }

    private NumberValue procNumber() {
        String tmp = current.token;
        eat(TokenType.NUMBER);
        Double d = Double.valueOf(tmp);
        NumberValue nv = new NumberValue(d);
        return nv;
    }

    private StringValue procString() {
        String tmp = current.token;
        eat(TokenType.STRING);
        StringValue sv = new StringValue(tmp);
        return sv;
    }

}
