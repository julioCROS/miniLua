package lexical;

import java.io.FileInputStream;
import java.io.PushbackInputStream;

public class LexicalAnalysis implements AutoCloseable {

    private int line;
    private SymbolTable st;
    private PushbackInputStream input;

    public LexicalAnalysis(String filename) {
        try {
            input = new PushbackInputStream(new FileInputStream(filename));
        } catch (Exception e) {
            throw new LexicalException("Unable to open file");
        }

        st = new SymbolTable();
        line = 1;
    }

    public void close() {
        try {
            input.close();
        } catch (Exception e) {
            throw new LexicalException("Unable to close file");
        }
    }

    public int getLine() {
        return this.line;
    }

    public Lexeme nextToken() {
        Lexeme lex = new Lexeme("", TokenType.END_OF_FILE);

        int state = 1;
        while (state != 17 && state != 18) {
            int c = getc();
            // System.out.printf(" [%02d, %03d ('%c')]\n", state, c, (char) c);

            switch (state) {
                case 1:
                    if (c == ' ' || c == '\t' || c == '\r') {
                        state = 1;
                    } else if (c == '\n') {
                        line++;
                        state = 1;
                    } else if (c == '-') {
                        state = 2;
                    } else if (c == '=' || c == '<' || c == '>') {
                        state = 10;
                    } else if (c == '~') {
                        lex.token += (char) c;
                        state = 11;
                    } else if (c == '.') {
                        state = 12;
                    } else if (Character.isLetter(c)) {
                        lex.token += (char) c;
                        state = 13;
                    } else if (c == '"') {
                        state = 14;
                    } else if (Character.isDigit(c)) {
                        lex.token += (char) c;
                        state = 15;
                    } else if (c == ';' || c == ',' || c == '+' || c == '*' ||
                            c == '/' || c == '%' || c == '#' ||
                            c == '(' || c == ')' || c == '[' || c == ']' ||
                            c == '{' || c == '}') {
                        lex.token += (char) c;
                        state = 17;
                    } else if (c == -1) {
                        lex.type = TokenType.END_OF_FILE;
                        state = 18;
                    } else {
                        lex.token += (char) c;
                        lex.type = TokenType.INVALID_TOKEN;
                        state = 18;
                    }

                    // TODO: Implement me!
                    break;
                case 2:
                
                    // TODO: Implement me!
                    break;
                case 3:
                    // TODO: Implement me!
                    break;
                case 4:
                    // TODO: Implement me!
                    break;
                case 5:
                    // TODO: Implement me!
                    break;
                case 6:
                    // TODO: Implement me!
                    break;
                case 7:
                    // TODO: Implement me!
                    break;
                case 8:
                    // TODO: Implement me!
                    break;
                case 9:
                    // TODO: Implement me!
                    break;
                case 10:
                    // TODO: Implement me!
                    break;
                case 11:
                    if (c == '=') {
                        lex.token += (char) c;
                        state = 17;
                    } else {
                        ungetc(c);
                        lex.type = TokenType.INVALID_TOKEN;
                        state = 18;
                    }

                    break;
                case 12:
                    // TODO: Implement me!
                    break;
                case 13:
                    if (c == '_' ||
                            Character.isLetter(c) ||
                            Character.isDigit(c)) {
                        lex.token += (char) c;
                        state = 13;
                    } else {
                        ungetc(c);
                        state = 17;
                    }

                    // TODO: Implement me!
                    break;
                case 14:
                    // TODO: Implement me!
                    break;
                case 15:
                    if (c == '.') {
                        lex.token += (char) c;
                        state = 16;
                    } else if (Character.isDigit(c)) {
                        lex.token += (char) c;
                        state = 15;
                    } else {
                        ungetc(c);
                        lex.type = TokenType.NUMBER;
                        state = 18;
                    }

                    break;
                case 16:
                    // TODO: Implement me!
                    break;
                default:
                    throw new LexicalException("Unreachable");
            }
        }

        if (state == 17)
            lex.type = st.find(lex.token);

        return lex;

    }

    private int getc() {
        try {
            return input.read();
        } catch (Exception e) {
            throw new LexicalException("Unable to read file");
        }
    }

    private void ungetc(int c) {
        if (c != -1) {
            try {
                input.unread(c);
            } catch (Exception e) {
                throw new LexicalException("Unable to ungetc");
            }
        }
    }
}
