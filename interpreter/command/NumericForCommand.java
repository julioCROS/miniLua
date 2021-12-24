package interpreter.command;

import interpreter.expr.Expr;
import interpreter.value.Value;

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
        for	 var=expr1,expr2,expr3	 do	 ...	 end (itera	 de	 expr1	 até	
        expr2	usando	exp3	como	passo	para	incrementar	var;	expr3	é	opcional,	
        assume-se	o	valor	1	se	não	estiver	presente).
        */
    }
}