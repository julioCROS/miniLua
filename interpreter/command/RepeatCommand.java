package interpreter.command;

import interpreter.expr.Expr;
import interpreter.value.BooleanValue;

public class RepeatCommand extends Command {
    private Command cmds;
    private Expr expr;

    public RepeatCommand(int line, Command cmds, Expr expr) {
        super(line);
        this.cmds = cmds;
        this.expr = expr;
    }

    @Override
    public void execute() {
        /*
        Executar comandos e repetir se a expressao for falsa.
        */
        cmds.execute();
        if(expr.expr() != null && expr.expr().value() instanceof BooleanValue)
        {
            if(expr.expr().value().equals(Boolean.FALSE))
            {
                cmds.execute();
            } 
        }
    }
}