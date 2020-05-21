package p0nki.commandparser;

public interface Command<S, R> {

    R run(CommandContext<S> context);

}
