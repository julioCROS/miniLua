package interpreter.command;

import interpreter.expr.Expr;
import interpreter.value.Value;

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
        onde	 cada	 iteração	 var1	 recebe	 um	 índice	 e	 var2	 seu	 valor	
        correspondente;	var2	é	opcional,	assim	apenas	a	chave	var1	é	usada).
        */
    }
}