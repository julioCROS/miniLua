package interpreter.command;

import interpreter.expr.Expr;
import interpreter.value.BooleanValue;

public class WhileCommand extends Command {
    private Expr expr;
    private Command cmds;

    public WhileCommand(int line, Expr expr, Command cmds) {
        super(line);
        this.expr = expr;
        this.cmds = cmds;
    }

    @Override
    public void execute() {
        if(expr.expr() != null && expr.expr().value() instanceof BooleanValue)
        {
            while(expr.expr().value().equals(Boolean.TRUE))
            {
                cmds.execute();
            } 
        }
    }
}
