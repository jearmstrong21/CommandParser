package p0nki.commandparser.node;

import p0nki.commandparser.command.CommandContext;
import p0nki.commandparser.command.CommandReader;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LiteralCommandNode<S, R> extends CommandNode<S, R> {

    private final Set<String> literals;

    public LiteralCommandNode(String... values) {
        this(new HashSet<>(Arrays.asList(values)));
    }

    public LiteralCommandNode(Set<String> alias) {
        super(String.join(", ", alias));
        literals = alias;
    }

    @Override
    public boolean parse(CommandContext<S> context, CommandReader reader) {
        return literals.contains(reader.readWhile(CommandReader.isNotSpace));
    }
}
