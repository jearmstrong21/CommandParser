package p0nki.commandparser.node;

import p0nki.commandparser.argument.ArgumentType;
import p0nki.commandparser.command.CommandContext;
import p0nki.commandparser.command.CommandReader;

import java.util.Optional;

public class ArgumentCommandNode<S, R, T> extends CommandNode<S, R> {

    private final String name;
    private final ArgumentType<S, T> argumentType;

    public ArgumentCommandNode(String name, ArgumentType<S, T> argumentType) {
        super(Optional.empty(), name + ":" + argumentType.getName());
        this.name = name;
        this.argumentType = argumentType;
    }

    @Override
    public boolean parse(CommandContext<S> context, CommandReader reader) {
        Optional<T> result = argumentType.parse(context, reader);
        result.ifPresent(t -> context.set(name, t));
        return result.isPresent();
    }
}
