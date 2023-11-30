package JSONParser.Parser.Tokens;

import java.util.List;

enum STATE {
    IDENTIFIER,
    SEPARATOR, QUOTE
}

public class Tokeniser {
    private static String separators = "[]{},:";
    public List<Token> tokens;

    public List<Token> getTokens(String json) {
        boolean quoted = false;
        StringBuilder buffer=new StringBuilder();
        for (char x : json.toCharArray()) {
            if (quoted) {
                if (x == '\"') {
                    //push buffer
                    quoted = false;
                }
            } else {

            }
        }
        throw new UnsupportedOperationException();
    }

    private STATE getSTATE(char c) {
        if (matchCharacter(c, separators)) {
            return STATE.SEPARATOR;
        } else if (c == '\"') {
            return STATE.QUOTE;
        }
        return STATE.IDENTIFIER;

    }

    public static boolean matchCharacter(char c, String s) {
        for (char x : s.toCharArray()) {
            if (x == c) return true;
        }
        return false;
    }

    public TOKEN getTOKEN(char c) {
        return switch (c) {
            case '[' -> TOKEN.L_SQUARE;
            case ']' -> TOKEN.R_SQUARE;
            case '{' -> TOKEN.L_BRACE;
            case '}' -> TOKEN.R_BRACE;
            case ':' -> TOKEN.COLON;
            case ',' -> TOKEN.COMMA;
            case '\"' -> TOKEN.QUOTE;
            default -> TOKEN.VALUE;
        };
    }
}
