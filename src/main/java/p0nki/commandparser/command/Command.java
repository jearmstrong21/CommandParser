package p0nki.commandparser.command;

public interface Command<S, R> {

    R run(CommandContext<S> context);

}
