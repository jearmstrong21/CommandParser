package p0nki.commandparser.argument;

import p0nki.commandparser.command.CommandContext;
import p0nki.commandparser.command.CommandReader;

import java.util.Optional;

public class GreedyStringArgumentType<S> implements ArgumentType<S, String> {

    @Override
    public Optional<String> parse(CommandContext<S> context, CommandReader reader) {
        String str = reader.readWhile(ch -> true);
        if (str.trim().equals("")) return Optional.empty();
        return Optional.of(str);
    }

    public static String get(CommandContext<?> context, String name){
        return context.get(name, String.class);
    }

    @Override
    public String getName() {
        return "greedyString";
    }
}
