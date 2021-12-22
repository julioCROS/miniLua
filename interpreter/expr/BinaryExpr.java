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
                // return left && right;
            case OrOp:
               // return left || right;
            case EqualOp:
                // return left == right;
            case NotEqualOp:
                // return left != right;
            case LowerThanOp:
                // return left < right;
            case LowerEqualOp:
                // return left <= right;
            case GreaterThanOp:
                // return left > right;
            case GreaterEqualOp:
                // return left >= right;
            case ConcatOp:
                // return left + right;
            case AddOp:
                // return left + right;
            case SubOp:
                //return left - right;
            case MulOp:
                //return left * right;
            case DivOp:
                //return left / right;
            case ModOp:
                //return left % right;
            default:
                return null;
        }
    }
}