package interpreter.expr;

import interpreter.util.Utils;
import interpreter.value.BooleanValue;
import interpreter.value.NumberValue;
import interpreter.value.StringValue;
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
        Value<?> value = null;
        Value<?> left = this.left.expr();
        Value<?> right = this.right.expr();

        switch (op) {
            case AndOp:
                // return left && right;
                if(left instanceof BooleanValue && right instanceof BooleanValue)
                {
                    boolean bool = (left.eval() && right.eval());
                    BooleanValue booleanValue = new BooleanValue(bool);
                    value = booleanValue;
                } else {
                    Utils.abort(super.getLine());
                }
                break;
            case OrOp:
               // return left || right;
               if(left instanceof BooleanValue && right instanceof BooleanValue)
                {
                    boolean bool = (left.eval() || right.eval());
                    BooleanValue booleanValue = new BooleanValue(bool);
                    value = booleanValue;
                } else {
                    Utils.abort(super.getLine());
                }
                break;
            case EqualOp:
                // return left == right;
                if(left instanceof BooleanValue && right instanceof BooleanValue)
                {
                    boolean bool = (left.eval() == right.eval());
                    BooleanValue booleanValue = new BooleanValue(bool);
                    value = booleanValue;
                } else if(left instanceof StringValue && right instanceof StringValue)
                {
                    String lv = left.toString();
                    String rv = right.toString();
                    int equal = lv.compareTo(rv);
                    Boolean areEqual = false;
                    if(equal == 0) {
                        areEqual = true;
                    }
                    BooleanValue booleanValue = new BooleanValue(areEqual);
                    value = booleanValue;
                } else if(left instanceof NumberValue && right instanceof NumberValue)
                {
                    double lv = Double.parseDouble(left.toString());
                    double rv = Double.parseDouble(right.toString());
                    boolean equal = false;
                    if(lv == rv) {
                        equal = true;
                    }
                    BooleanValue booleanValue = new BooleanValue (equal);
                    value = booleanValue;
                } else {
                    Utils.abort(super.getLine());
                }
                break;
            case NotEqualOp:
                // return left != right;
                if(left instanceof BooleanValue && right instanceof BooleanValue)
                {
                    boolean bool = (left.eval() != right.eval());
                    BooleanValue booleanValue = new BooleanValue(bool);
                    value = booleanValue;
                } else if(left instanceof StringValue && right instanceof StringValue)
                {
                    String lv = left.toString();
                    String rv = right.toString();
                    int equal = lv.compareTo(rv);
                    Boolean notEqual = true;
                    if(equal == 0) {
                        notEqual = false;
                    }
                    BooleanValue booleanValue = new BooleanValue(notEqual);
                    value = booleanValue;
                } else if(left instanceof NumberValue && right instanceof NumberValue)
                {
                    double lv = Double.parseDouble(left.toString());
                    double rv = Double.parseDouble(right.toString());
                    boolean notEqual = true;
                    if(lv == rv) {
                        notEqual = false;
                    }
                    BooleanValue booleanValue = new BooleanValue (notEqual);
                    value = booleanValue;
                } else {
                    Utils.abort(super.getLine());
                }
                break;
            case LowerThanOp:
                // return left < right;
                if (left instanceof NumberValue && right instanceof NumberValue) {
                    double lv = Double.parseDouble(left.toString());
                    double rv = Double.parseDouble(right.toString());
                    boolean lower = lv < rv;
                    BooleanValue booleanValue = new BooleanValue (lower);
                    value = booleanValue;
                } else {
                    Utils.abort(super.getLine());
                }
                break;
            case LowerEqualOp:
                // return left <= right;
                if (left instanceof NumberValue && right instanceof NumberValue) {
                    double lv = Double.parseDouble(left.toString());
                    double rv = Double.parseDouble(right.toString());
                    boolean lowerEqual = lv <= rv;
                    BooleanValue booleanValue = new BooleanValue(lowerEqual);
                    value = booleanValue;
                } else {
                    Utils.abort(super.getLine());
                }
                break;
            case GreaterThanOp:
                // return left > right;
                if (left instanceof NumberValue && right instanceof NumberValue) {
                    double lv = Double.parseDouble(left.toString());
                    double rv = Double.parseDouble(right.toString());
                    boolean greater = lv > rv;
                    BooleanValue booleanValue = new BooleanValue(greater);
                    value = booleanValue;
                } else {
                    Utils.abort(super.getLine());
                }
                break;
            case GreaterEqualOp:
                // return left >= right;
                if (left instanceof NumberValue && right instanceof NumberValue) {
                    double lv = Double.parseDouble(left.toString());
                    double rv = Double.parseDouble(right.toString());
                    boolean greaterEqual = lv >= rv;
                    BooleanValue booleanValue = new BooleanValue(greaterEqual);
                    value = booleanValue;
                } else {
                    Utils.abort(super.getLine());
                }
                break;
            case ConcatOp:
                // return left + right;
                if (left instanceof NumberValue && right instanceof NumberValue) {
                    String lv = left.toString();
                    String rv = right.toString();
                    String concat = lv + rv;
                    StringValue stringValue = new StringValue(concat);
                    value = stringValue;
                } else {
                    Utils.abort(super.getLine());
                }
                break;
            case AddOp:
                // return left + right;
                if (left instanceof NumberValue && right instanceof NumberValue) {
                    double lv = Double.parseDouble(left.toString());
                    double rv = Double.parseDouble(right.toString());
                    double add = lv + rv;
                    NumberValue numberValue = new NumberValue (add);
                    value = numberValue;
                } else {
                    Utils.abort(super.getLine());
                }
                break;
            case SubOp:
                //return left - right;
                if (left instanceof NumberValue && right instanceof NumberValue) {
                    double lv = Double.parseDouble(left.toString());
                    double rv = Double.parseDouble(right.toString());
                    double sub = lv - rv;
                    NumberValue numberValue = new NumberValue (sub);
                    value = numberValue;
                } else {
                    Utils.abort(super.getLine());
                }
                break;
            case MulOp:
                //return left * right;
                if (left instanceof NumberValue && right instanceof NumberValue) {
                    double lv = Double.parseDouble(left.toString());
                    double rv = Double.parseDouble(right.toString());
                    double mul = lv*rv;
                    NumberValue numberValue = new NumberValue (mul);
                    value = numberValue;
                } else {
                    Utils.abort(super.getLine());
                }
                break;
            case DivOp:
                //return left / right;
                if (left instanceof NumberValue && right instanceof NumberValue) {
                    double lv = Double.parseDouble(left.toString());
                    double rv = Double.parseDouble(right.toString());
                    double div = lv/rv;
                    NumberValue numberValue = new NumberValue (div);
                    value = numberValue;
                } else {
                    Utils.abort(super.getLine());
                }
                break;
            case ModOp:
                //return left % right;
                if (left instanceof NumberValue && right instanceof NumberValue) {
                    double lv = Double.parseDouble(left.toString());
                    double rv = Double.parseDouble(right.toString());
                    double mod = lv%rv;
                    NumberValue numberValue = new NumberValue (mod);
                    value = numberValue;
                } else {
                    Utils.abort(super.getLine());
                }
                break;
            default:
                Utils.abort(super.getLine());
        }
        return value;
    }
}