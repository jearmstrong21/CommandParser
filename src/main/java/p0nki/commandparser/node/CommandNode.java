package p0nki.commandparser.node;

import p0nki.commandparser.command.Command;
import p0nki.commandparser.command.CommandContext;
import p0nki.commandparser.command.CommandReader;
import p0nki.commandparser.command.CommandRequirement;

import java.util.*;

public abstract class CommandNode<S, R> {

    private final Set<CommandRequirement<S>> requirements;
    private final String name;
    private final Optional<String> category;
    private final List<CommandNode<S, R>> children;
    private Optional<Command<S, R>> executes;
    private Optional<String> documentation;

    public CommandNode(Optional<String> category, String name) {
        requirements = new HashSet<>();
        children = new ArrayList<>();
        this.category = category;
        this.name = name;
        this.executes = Optional.empty();
        documentation = Optional.empty();
    }

    public Optional<String> getCategory() {
        return category;
    }

    public Optional<Command<S, R>> getExecutes() {
        return executes;
    }

    public Optional<String> getDocumentation() {
        return documentation;
    }

    public CommandNode<S, R> documentation(String documentation) {
        this.documentation = Optional.of(documentation);
        return this;
    }

    public Set<CommandRequirement<S>> getRequirements() {
        return requirements;
    }

    public List<CommandNode<S, R>> getChildren() {
        return children;
    }

    @Override
    public final String toString() {
        return name;
    }

    public final CommandNode<S, R> requires(CommandRequirement<S> requirement) {
        requirements.add(requirement);
        return this;
    }

    public final CommandNode<S, R> then(CommandNode<S, R> child) {
        children.add(child);
        return this;
    }

    public final CommandNode<S, R> executes(Command<S, R> command) {
        executes = Optional.of(command);
        return this;
    }

    public final boolean isIntermediateNode() {
        return !executes.isPresent();
    }

    public final R execute(CommandContext<S> context) {
        if (!isAvailableTo(context.source())) throw new IllegalArgumentException("This shouldn't be happening");
        if (!executes.isPresent()) throw new IllegalArgumentException("This shouldn't be happening");
        return executes.get().run(context);
    }

    public final boolean isAvailableTo(S s) {
        return requirements.stream().allMatch(requirement -> requirement.isAvailableTo(s));
    }

    public abstract boolean parse(CommandContext<S> context, CommandReader reader);

}
