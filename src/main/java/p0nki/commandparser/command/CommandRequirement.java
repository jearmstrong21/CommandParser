package p0nki.commandparser.command;

public interface CommandRequirement<S> {

    boolean isAvailableTo(S s);

    String documentation();

}
