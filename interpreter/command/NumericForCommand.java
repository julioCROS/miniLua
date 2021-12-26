package interpreter.command;

import interpreter.expr.Expr;
import interpreter.expr.Variable;
import interpreter.value.NumberValue;

public class NumericForCommand extends Command {
    private Variable var;
    private Expr expr1;
    private Expr expr2;
    private Expr expr3;
    private Command cmds;

    public NumericForCommand(int line, Variable var, Expr expr1, Expr expr2, Expr expr3, Command cmds) {
        super(line);
        this.var = var;
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.expr3 = expr3;
        this.cmds = cmds;
    }

    @Override
    public void execute() {
        /*
        for	 var=expr1,expr2,expr3	 do	 ...	 end (itera	 de	 expr1	 ate	
        expr2	usando	exp3	como	passo	para	incrementar	var;	expr3	e	opcional,	
        assume-se	o	valor	1	se	nao	estiver	presente).
        */
        int incrementar;
        int expressao1;
        int expressao2;

        if(expr3 != null && expr3.expr() != null && expr3.expr().value() instanceof NumberValue) {
            incrementar = Integer.parseInt(expr3.expr().toString());
        } else {
            incrementar = 1;
        }

        if(expr1.expr() != null && expr1.expr().value() instanceof NumberValue && expr2.expr() != null && expr2.expr().value() instanceof NumberValue)
        {
            expressao1 = Integer.parseInt(expr1.expr().toString());;
            expressao2 = Integer.parseInt(expr2.expr().toString());;
            while(expressao2 > expressao1)
            {
                cmds.execute();
                expressao1 = expressao1 + incrementar;
                var.setValue(new NumberValue((double) expressao1));
            } 
        }
    }
}