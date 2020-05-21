package p0nki.commandparser.node;

import p0nki.commandparser.CommandContext;
import p0nki.commandparser.CommandReader;
import p0nki.commandparser.node.CommandNode;

public class RootCommandNode<S, R> extends CommandNode<S, R> {

    public RootCommandNode() {
        super("<root>");
    }

    @Override
    public boolean parse(CommandContext<S> context, CommandReader reader) {
        return true;
    }
}
