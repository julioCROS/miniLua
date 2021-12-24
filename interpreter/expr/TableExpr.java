package interpreter.expr;

import interpreter.value.Value;

public class TableExpr extends Expr{
    private List<TableEntry> table;

    public TableExpr(int line) {
        super(line);
    }

    public addEntry(Expr key, Expr value) {
    }

    @Override
    public Value<?> expr() {
        return this.value;
    }
}