package p0nki.commandparser;

import java.util.function.Predicate;

public class CommandReader {

    public static Predicate<Character> isSpace = ch -> ch == ' ';
    public static Predicate<Character> isNotSpace = ch -> ch != ' ';

    private final String buffer;
    private int index;

    public CommandReader(String buffer) {
        this.buffer = buffer;
        index = 0;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getBuffer() {
        return buffer;
    }

    public boolean canRead() {
        return index < buffer.length();
    }

    public String readWhile(Predicate<Character> predicate) {
        StringBuilder str = new StringBuilder();
        char ch;
        while (canRead() && predicate.test(ch = next())) {
            str.append(ch);
        }
        return str.toString();
    }

    public char next() {
        return buffer.charAt(index++);
    }

}
