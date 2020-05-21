package p0nki.commandparser;

public interface CommandRequirement<S> {

    boolean isAvailableTo(S s);

    String documentation();

}
