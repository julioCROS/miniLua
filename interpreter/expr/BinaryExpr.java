package interpreter.expr;

import interpreter.value.Value;

public class BinaryExpr extends Expr{
    private Expr left;
    private BinaryOp op;
    private Expr right;

    public BinaryExpr(int line, Expr left, BinaryOp op, Expr right) {
        super(line);
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public Value<?> expr() {
        switch (op) {
            case AndOp:
                return left && right;
            OrOp:
                return left || right;
            EqualOp:
                return left == right;
            NotEqualOp:
                return left != right;
            LowerThanOp:
                return left < right;
            LowerEqualOp:
                return left <= right;
            GreaterThanOp:
                return left > right;
            GreaterEqualOp:
                return left >= right;
            ConcatOp:
                return left + right;
            AddOp:
                return left + right;
            SubOp:
                return left - right;
            MulOp:
                return left * right;
            DivOp:
                return left / right;
            ModOp:
                return left % right;
        }
    }
}