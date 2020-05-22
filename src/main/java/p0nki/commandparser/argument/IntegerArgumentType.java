package p0nki.commandparser.argument;

import p0nki.commandparser.command.CommandContext;
import p0nki.commandparser.command.CommandReader;

import java.util.Optional;

public class IntegerArgumentType<S> implements ArgumentType<S, Integer> {

    private final int minimum;
    private final int maximum;

    public IntegerArgumentType() {
        this(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntegerArgumentType(int minimum) {
        this(minimum, Integer.MAX_VALUE);
    }

    public IntegerArgumentType(int minimum, int maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public Optional<Integer> parse(CommandContext<S> context, CommandReader reader) {
        try {
            int value = Integer.parseInt(reader.readWhile(CommandReader.isNotSpace));
            if (value < minimum) return Optional.empty();
            if (value > maximum) return Optional.empty();
            return Optional.of(value);
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static int get(CommandContext<?> context, String name){
        return context.get(name, Integer.class);
    }

    @Override
    public String getName() {
        if (minimum == Integer.MIN_VALUE && maximum == Integer.MAX_VALUE) {
            return "integer";
        }
        if (maximum == Integer.MAX_VALUE) {
            return "integer[" + minimum + ",-]";
        }
        return "integer[" + minimum + "," + maximum + "]";
    }

}
