package p0nki.commandparser.argument;

import p0nki.commandparser.CommandContext;
import p0nki.commandparser.CommandReader;

import java.util.Optional;

public class IntegerArgumentType<S> implements ArgumentType<S, Integer> {

    private Integer minimum = null;
    private Integer maximum = null;

    public IntegerArgumentType() {
    }

    public IntegerArgumentType<S> minimum(int minimum) {
        this.minimum = minimum;
        return this;
    }

    public IntegerArgumentType<S> maximum(int maximum) {
        this.maximum = maximum;
        return this;
    }

    @Override
    public Optional<Integer> parse(CommandContext<S> context, CommandReader reader) {
        try {
            int value = Integer.parseInt(reader.readWhile(CommandReader.isNotSpace));
            if (minimum != null && value < minimum) return Optional.empty();
            if (maximum != null && value > maximum) return Optional.empty();
            return Optional.of(value);
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    @Override
    public String getName() {
        if (minimum == null && maximum == null) {
            return "integer";
        }
        if (minimum == null) {
            return "integer[-," + maximum + "]";
        }
        if (maximum == null) {
            return "integer[" + minimum + ",-]";
        }
        return "integer[" + minimum + "," + maximum + "]";
    }

}
