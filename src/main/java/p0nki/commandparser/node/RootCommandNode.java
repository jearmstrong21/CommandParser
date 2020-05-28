package p0nki.commandparser.node;

import p0nki.commandparser.command.CommandContext;
import p0nki.commandparser.command.CommandReader;

import java.util.Optional;

public class RootCommandNode<S, R> extends CommandNode<S, R> {

    public RootCommandNode() {
        super(Optional.empty(), "<root>");
    }

    @Override
    public boolean parse(CommandContext<S> context, CommandReader reader) {
        return true;
    }
}
