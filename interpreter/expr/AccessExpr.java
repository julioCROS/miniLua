package interpreter.expr;

import interpreter.value.Value;

public class AccessExpr extends Expr{
    private Expr base;
    private Expr index;

    public AccessExpr(int line, Expr base, Expr index) {
        super(line);
        this.base = base;
        this.index = index;
    }

    @Override
    public Value<?> expr() {
        return this.value;
    }

    public void setValue(Value<?> value) {
        this.value = value;
    }
}