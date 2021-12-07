import interpreter.command.Command;
import lexical.LexicalAnalysis;
import syntatic.SyntaticAnalysis;
import lexical.TokenType;
import lexical.Lexeme;

public class mli {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java mli [miniLua file]");
            return;
        }

        try (LexicalAnalysis l = new LexicalAnalysis(args[0])) {
            // O código a seguir é dado para testar o interpretador.
            // TODO: descomentar depois que o analisador léxico estiver OK.
            // SyntaticAnalysis s = new SyntaticAnalysis(l);
            // Command c = s.start();
            // c.execute();

            // // O código a seguir é usado apenas para testar o analisador léxico.
            // // TODO: depois de pronto, comentar o código abaixo.
            Lexeme lex;
            do {
                lex = l.nextToken();
                //System.out.printf("%02d: (\"%s\", %s)\n", l.getLine(),
                //        lex.token, lex.type);
            } while (lex.type != TokenType.END_OF_FILE &&
                    lex.type != TokenType.INVALID_TOKEN &&
                    lex.type != TokenType.UNEXPECTED_EOF);

            switch (lex.type) {
                case INVALID_TOKEN:
                    System.out.printf("%02d: Lexema invalido [%s]\n", l.getLine(), lex.token);
                    break;
                case UNEXPECTED_EOF:
                    System.out.printf("%02d: Fim de arquivo inesperado\n", l.getLine());
                    break;
                default:
                    System.out.printf("(\"%s\", %s)\n", lex.token, lex.type);
                    break;

            }
        } catch (Exception e) {
            System.err.println("Internal error: " + e.getMessage());
        }
    }

}
