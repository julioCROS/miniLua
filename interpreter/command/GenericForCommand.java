package interpreter.command;

import interpreter.expr.Expr;
import interpreter.expr.Variable;

public class GenericForCommand extends Command {
    private Variable var1;
    private Variable var2;
    private Expr expr;
    private Command cmds;

    public GenericForCommand(int line, Variable var1, Variable var2, Expr expr, Command cmds) {
        super(line);
        this.var1 = var1;
        this.var2 = var2;
        this.expr = expr;
        this.cmds = cmds;
    }

    @Override
    public void execute() {
        /*
        for	var1,	var2	in	expr	do	...	end (expr	deve	ser	uma	tabela,	
        onde	 cada	 iteracao	 var1	 recebe	 um	 indice	 e	 var2	 seu	 valor	
        correspondente;	var2	e	opcional,	assim	apenas	a	chave	var1	e	usada).
        */
    }
}