package p0nki.commandparser.argument;

import p0nki.commandparser.command.CommandContext;
import p0nki.commandparser.command.CommandReader;

import java.util.Optional;

public class QuotedStringArgumentType<S> implements ArgumentType<S, String> {

    private boolean isQuote(char ch) {
        return ch == '\'' || ch == '\"';
    }

    private boolean isEscape(char ch) {
        return ch == '/';
    }

    @Override
    public Optional<String> parse(CommandContext<S> context, CommandReader reader) {
        if (!reader.canRead()) return Optional.empty();
        char first = reader.next();
        StringBuilder str = new StringBuilder();
        if (isQuote(first)) {
            char ch;
            while (reader.canRead() && !isQuote(ch = reader.next())) {
                str.append(ch);
            }
            if (reader.canRead()) reader.next();
        } else {
            str.append(first).append(reader.readWhile(CommandReader.isNotSpace));
        }
        if (str.toString().trim().equals("")) return Optional.empty();
        return Optional.of(str.toString());
    }


    @Override
    public String getName() {
        return "quotedString";
    }
}
