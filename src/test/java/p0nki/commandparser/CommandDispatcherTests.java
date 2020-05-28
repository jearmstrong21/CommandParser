package p0nki.commandparser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import p0nki.commandparser.argument.GreedyStringArgumentType;
import p0nki.commandparser.argument.IntegerArgumentType;
import p0nki.commandparser.argument.QuotedStringArgumentType;
import p0nki.commandparser.command.CommandDispatcher;
import p0nki.commandparser.command.CommandRequirement;
import p0nki.commandparser.command.CommandSyntaxException;
import p0nki.commandparser.node.ArgumentCommandNode;
import p0nki.commandparser.node.CommandNode;
import p0nki.commandparser.node.LiteralCommandNode;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class CommandDispatcherTests {

    public static CommandDispatcher<String, Integer> dispatcher;

    @BeforeClass
    public static void registerCommands() {
        dispatcher = new CommandDispatcher<>();
        dispatcher.register(new LiteralCommandNode<String, Integer>("test", "test2")
                .documentation("[PLACEHOLDER]")
                .then(new LiteralCommandNode<String, Integer>("a")
                        .documentation("Returns 1")
                        .executes(context -> 1)
                )
                .then(new LiteralCommandNode<String, Integer>("b")
                        .documentation("Returns 0")
                        .executes(context -> 0)
                )
                .then(new LiteralCommandNode<String, Integer>("c")
                        .documentation("Returns the value of the `argname` parameter")
                        .then(new ArgumentCommandNode<String, Integer, Integer>("argname", new IntegerArgumentType<>(5))
                                .documentation("[PLACEHOLDER]")
                                .executes(context -> context.get("argname", Integer.class))
                        )
                )
                .then(new LiteralCommandNode<String, Integer>("alias1", "alias2", "alias3")
                        .documentation("[PLACEHOLDER]")
                        .requires(new CommandRequirement<String>() {
                            @Override
                            public boolean isAvailableTo(String s) {
                                return s.equals("allowed");
                            }

                            @Override
                            public String documentation() {
                                return "source must be `allowed`";
                            }
                        })
                        .then(new ArgumentCommandNode<String, Integer, Integer>("argname2", new IntegerArgumentType<>(40, 43))
                                .documentation("[PLACEHOLDER]")
                                .executes(context -> context.get("argname2", Integer.class))
                        )
                )
                .executes(context -> -3)
        );
        dispatcher.register(new LiteralCommandNode<String, Integer>("abc", "def")
                .documentation("[PLACEHOLDER]")
                .then(new ArgumentCommandNode<String, Integer, String>("str", new GreedyStringArgumentType<>())
                        .documentation("[PLACEHOLDER]")
                        .executes(context -> context.get("str", String.class).length())
                )
        );
        dispatcher.register(new LiteralCommandNode<String, Integer>("ghi")
                .documentation("[PLACEHOLDER]")
                .then(new ArgumentCommandNode<String, Integer, String>("str", new GreedyStringArgumentType<>())
                        .documentation("[PLACEHOLDER]")
                        .then(new ArgumentCommandNode<String, Integer, Integer>("int", new IntegerArgumentType<>())
                                .documentation("[PLACEHOLDER]")
                                .executes(context -> context.get("int", Integer.class) + context.get("str", String.class).length())
                        )
                )
        );
        dispatcher.register(new LiteralCommandNode<String, Integer>("bruh")
                .documentation("[PLACEHOLDER]")
                .then(new ArgumentCommandNode<String, Integer, String>("quotedString", new QuotedStringArgumentType<>())
                        .documentation("[PLACEHOLDER]")
                        .then(new ArgumentCommandNode<String, Integer, Integer>("int", new IntegerArgumentType<>(5, 10))
                                .documentation("[PLACEHOLDER]")
                                .executes(context -> context.get("int", Integer.class) + context.get("quotedString", String.class).length())
                        )
                )
        );
        System.out.println(dispatcher.generateHelp(Optional.empty()));
    }

    @Test
    @Before
    public void basic() throws CommandSyntaxException {
        Assert.assertEquals(1, (int) dispatcher.run("source", "test a"));
        Assert.assertEquals(0, (int) dispatcher.run("source", "test b"));
    }

    @Test
    public void validArgument() throws CommandSyntaxException {
        Assert.assertEquals(32, (int) dispatcher.run("source", "test c 32"));
    }

    @Test(expected = CommandSyntaxException.class)
    public void invalidArgument() throws CommandSyntaxException {
        dispatcher.run("source", "test c 2");
    }

    @Test
    public void noExtraLiterals() throws CommandSyntaxException {
        Assert.assertEquals(-3, (int) dispatcher.run("source", "test"));
    }

    @Test
    public void literalAlias() throws CommandSyntaxException {
        Assert.assertEquals(-3, (int) dispatcher.run("source", "test2"));
        Assert.assertEquals(32, (int) dispatcher.run("source", "test2 c 32"));
    }

    @Test(expected = CommandSyntaxException.class)
    public void unsatisfiedRequirement() throws CommandSyntaxException {
        dispatcher.run("not-allowed-source", "test alias2 42");
    }

    @Test
    public void satisfiedRequirement() throws CommandSyntaxException {
        Assert.assertEquals(42, (int) dispatcher.run("allowed", "test alias3 42"));
    }

    @Test(expected = CommandSyntaxException.class)
    public void intermediateNodeEvaluate() throws CommandSyntaxException {
        dispatcher.run("source", "abc");
    }

    @Test
    public void intermediateNodeWithWhitespace() throws CommandSyntaxException {
        Assert.assertEquals(6, (int) dispatcher.run("source", "abc d     "));
    }

    @Test(expected = CommandSyntaxException.class)
    public void greedyTokenOverConsumes() throws CommandSyntaxException {
        dispatcher.run("source", "ghi text 5"); // greedy string consumes "text 5" and the integer argument doesn't exist
    }

    @Test
    public void quotedStringTest() throws CommandSyntaxException {
        Assert.assertEquals(15, (int) dispatcher.run("source", "bruh TESTING 8"));
        Assert.assertEquals(15, (int) dispatcher.run("source", "bruh \"TESTING\" 8"));
        Assert.assertEquals(15, (int) dispatcher.run("source", "bruh \"T S ING\" 8"));
    }

    @Test(expected = CommandSyntaxException.class)
    public void quotedStringStopsAtSpace() throws CommandSyntaxException {
        dispatcher.run("source", "bruh TE STING 8");
    }

    @Test
    public void checkTreeResults() {
        CommandDispatcher.TreeResults<String, Integer> results = dispatcher.descendTree("source", "test");
        Assert.assertNull(results.exception);
        Assert.assertEquals(0, results.context.keys().size());
        Assert.assertEquals(Arrays.asList("a", "b", "c"), results.nodes.stream().map(CommandNode::toString).collect(Collectors.toList()));
    }

    @Test
    public void checkTreeResultsWithCompleteArgument() {
        CommandDispatcher.TreeResults<String, Integer> results = dispatcher.descendTree("source", "bruh \"test\"");
        Assert.assertEquals(CommandSyntaxException.NO_COMMAND_FOUND, results.exception);
        Assert.assertEquals(1, results.context.keys().size());
        Assert.assertEquals(1, results.nodes.size());
    }

    @Test
    public void checkTreeResultsWithIncompleteArgument() {
        CommandDispatcher.TreeResults<String, Integer> results = dispatcher.descendTree("source", "bruh \"tes");
        Assert.assertEquals(CommandSyntaxException.NO_COMMAND_FOUND, results.exception);
        Assert.assertEquals(1, results.nodes.size());
        Assert.assertEquals(1, results.context.keys().size());
    }

    @Test(expected = CommandSyntaxException.class)
    public void incompleteQuotedString() throws CommandSyntaxException {
        dispatcher.run("source", "bruh \"tes");
    }

}
