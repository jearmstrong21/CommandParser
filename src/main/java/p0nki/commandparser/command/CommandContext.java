package p0nki.commandparser.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandContext<S> {

    private final Map<String, Object> arguments;
    private final S source;

    public CommandContext(S source) {
        arguments = new HashMap<>();
        this.source = source;
    }

    public S source() {
        return source;
    }

    public Set<String> keys() {
        return arguments.keySet();
    }

    public <T> void set(String name, T value) {
        arguments.put(name, value);
    }

    public <T> T get(String name, Class<T> clazz) {
        return clazz.cast(arguments.get(name));
    }

}
