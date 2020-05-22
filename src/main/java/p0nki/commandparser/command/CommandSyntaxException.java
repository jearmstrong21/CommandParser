package p0nki.commandparser.command;

public class CommandSyntaxException extends Exception {

    public static final CommandSyntaxException NO_COMMAND_FOUND = new CommandSyntaxException("No command found");
    public static final CommandSyntaxException UNCONSUMED_TOKENS = new CommandSyntaxException("Unconsumed tokens");

    private CommandSyntaxException(String message) {
        super(message);
    }

}
