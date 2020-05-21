package p0nki.commandparser.node;

import p0nki.commandparser.CommandContext;
import p0nki.commandparser.CommandReader;
import p0nki.commandparser.node.CommandNode;

import java.util.HashSet;
import java.util.Set;

public class LiteralCommandNode<S, R> extends CommandNode<S, R> {

    private final Set<String> literals;

    public LiteralCommandNode(String main) {
        this(main, new HashSet<>());
    }

    public LiteralCommandNode(String main, Set<String> alias) {
        super(main);
        literals = new HashSet<>(alias);
        literals.add(main);
    }

    @Override
    public boolean parse(CommandContext<S> context, CommandReader reader) {
        return literals.contains(reader.readWhile(CommandReader.isNotSpace));
    }
}
