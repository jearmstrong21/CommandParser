package p0nki.commandparser;

import p0nki.commandparser.node.CommandNode;
import p0nki.commandparser.node.RootCommandNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CommandDispatcher<S, R> {

    private final RootCommandNode<S, R> root;

    public CommandDispatcher() {
        root = new RootCommandNode<>();
    }

    public void register(CommandNode<S, R> node) {
        root.then(node);
    }

    public R run(S source, String str) throws CommandSyntaxException {
        CommandReader reader = new CommandReader(str);
        List<CommandNode<S, R>> nodes = root.getChildren();
        CommandContext<S> context = new CommandContext<>(source);
        CommandNode<S, R> parent = root;
        while (true) {
            int index = reader.getIndex();
            CommandNode<S, R> bestNode = null;
            nodes = nodes.stream().filter(node -> node.isAvailableTo(source)).collect(Collectors.toList());
            for (CommandNode<S, R> node : nodes) {
                reader.setIndex(index);
                if (node.parse(context, reader)) {
                    bestNode = node;
                    break;
                }
            }
            if (bestNode == null) {
                if (!parent.isExecutable()) throw new CommandSyntaxException("No command found");
                if (!parent.isAvailableTo(source)) throw new CommandSyntaxException("No command found");
                if (reader.canRead()) throw new CommandSyntaxException("Unconsumed tokens");
                return parent.execute(context);
            }
            nodes = bestNode.getChildren();
            parent = bestNode;
            if (nodes.size() == 0) {
                if (!bestNode.isExecutable()) throw new CommandSyntaxException("No command found");
                if (!bestNode.isAvailableTo(source)) throw new CommandSyntaxException("No command found");
                if (reader.canRead()) throw new CommandSyntaxException("Unconsumed tokens");
                return bestNode.execute(context);
            }
        }
    }

    public String generateHelp() {
        StringBuilder sb = new StringBuilder();
        List<CommandNode<S, R>> nodes = new ArrayList<>(root.getChildren());
        List<Integer> depths = nodes.stream().map(o -> 0).collect(Collectors.toList());
        while (nodes.size() > 0) {
            CommandNode<S, R> node = nodes.get(0);
            nodes.remove(0);
            int depth = depths.get(0);
            depths.remove(0);
            sb.append("\t".repeat(depth)).append(node.toString());
            node.getDocumentation().ifPresent(docs -> sb.append("\n").append("\t".repeat(depth)).append("\tDocumentation: ").append(docs));
            if (node.getRequirements().size() > 0) {
                sb.append("\n").append("\t".repeat(depth)).append("\tRequirements: ").append(node.getRequirements().stream().map(CommandRequirement::documentation).collect(Collectors.joining(", ")));
            }
            sb.append("\n");
            nodes.addAll(0, node.getChildren());
            depths.addAll(0, IntStream.range(0, node.getChildren().size()).mapToObj(x -> depth + 1).collect(Collectors.toList()));
        }
        return sb.toString();
    }

}