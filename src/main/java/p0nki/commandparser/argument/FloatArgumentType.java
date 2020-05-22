package p0nki.commandparser.argument;

import p0nki.commandparser.command.CommandContext;
import p0nki.commandparser.command.CommandReader;

import java.util.Optional;

public class FloatArgumentType<S> implements ArgumentType<S, Float> {

    private final float minimum;
    private final float maximum;

    public FloatArgumentType() {
        this(Float.MIN_VALUE, Float.MAX_VALUE);
    }

    public FloatArgumentType(float minimum) {
        this(minimum, Float.MAX_VALUE);
    }

    public FloatArgumentType(float minimum, float maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public Optional<Float> parse(CommandContext<S> context, CommandReader reader) {
        return Optional.empty();
    }

    @Override
    public String getName() {
        if (minimum == Float.MIN_VALUE && maximum == Float.MAX_VALUE) {
            return "float";
        }
        if (maximum == Float.MAX_VALUE) {
            return "float[" + minimum + ",-]";
        }
        return "float[" + minimum + "," + maximum + "]";
    }

}
