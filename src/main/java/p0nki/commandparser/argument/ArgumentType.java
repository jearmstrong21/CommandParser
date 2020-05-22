package p0nki.commandparser.argument;

import p0nki.commandparser.command.CommandContext;
import p0nki.commandparser.command.CommandReader;

import java.util.Optional;

public interface ArgumentType<S, T> {

    Optional<T> parse(CommandContext<S> context, CommandReader reader);

    String getName();

}
