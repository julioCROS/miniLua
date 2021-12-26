package interpreter.command;

import interpreter.expr.Expr;
import interpreter.value.BooleanValue;

public class IfCommand extends Command {
    private Expr expr;
    private Command thenCmds;
    private Command elseCmds;

    public IfCommand(int line, Expr expr, Command thenCmds) {
        super(line);
        this.expr = expr;
        this.thenCmds = thenCmds;
    }

    public void setElseCommands(Command elseCmds) {
        this.elseCmds = elseCmds;
    }

    @Override
    public void execute() {
        if(expr.expr() != null && expr.expr().value() instanceof BooleanValue)
        {
            if(expr.expr().value().equals(Boolean.TRUE))
            {
                thenCmds.execute();
            } else {
                if(elseCmds != null)
                {
                    elseCmds.execute();
                }
            }

        }
    }
}