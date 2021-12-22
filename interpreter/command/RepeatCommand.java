package interpreter.command;

import interpreter.expr.Expr;

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
        // Executar comandos e repetir se a express�o for falsa.
    }
}