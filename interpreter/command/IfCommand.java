package interpreter.command;

import interpreter.expr.Expr;

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
        // Implementar
    }

    @Override
    public void execute() {
        // Executar comandos se a express�o for verdadeira.
    }
}