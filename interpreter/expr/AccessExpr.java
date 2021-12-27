package interpreter.expr;

import java.util.Map;

import interpreter.util.Utils;
import interpreter.value.NumberValue;
import interpreter.value.StringValue;
import interpreter.value.TableValue;
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
        Value<?> v = base.expr();
        
        if (this.index != null) {            
            if (!(v instanceof TableValue)) {
                Utils.abort(super.getLine());
            } else {
                Value<?> ind = this.index.expr();
                Double key = (double) 0;
                TableValue table = (TableValue) v;
                Map<Value<?>, Value<?>> map = table.value();
                
                if(ind instanceof NumberValue) {
                    key = Double.parseDouble(ind.toString());
                } else if (ind instanceof StringValue) {
                    key = Double.parseDouble(ind.toString());
                } else {
                    Utils.abort(super.getLine());
                }

                return map.get(key);
            }         
        }  
        return v;
    }

    public void setValue(Value<?> value) {
        Value<?> v = base.expr();      
        SetExpr setExpr = (SetExpr) base;     
        
        if (this.index != null) {            
            if (!(v instanceof TableValue)) {
                Utils.abort(super.getLine());
            } else {
                Value<?> ind = this.index.expr();
                NumberValue key = new NumberValue((double) 0);
                TableValue table = (TableValue) v;
                Map<Value<?>, Value<?>> map = table.value();
                
                if(ind instanceof NumberValue) {
                    key = new NumberValue(Double.parseDouble(ind.toString()));
                } else if (ind instanceof StringValue) {
                    key = new NumberValue(Double.parseDouble(ind.toString()));
                } else {
                    Utils.abort(super.getLine());
                }

                if (!map.containsKey(key)) {
                    map.put(key, value);                
                }

                TableValue tableValue = new TableValue(map);
                setExpr.setValue(tableValue);   
                             
            }            
        } else {
            if (value instanceof NumberValue) {
                NumberValue numberValue = (NumberValue) value;
                Double number = numberValue.value();
                NumberValue nv = new NumberValue(number);
                setExpr.setValue(nv);
                             
            } else if (value instanceof StringValue) {
                StringValue stringValue = (StringValue) value;
                String string = stringValue.value();
                StringValue sv = new StringValue(string);
                setExpr.setValue(sv);                
                
            } else {
                setExpr.setValue(value);                
            }
        } 
    }
}