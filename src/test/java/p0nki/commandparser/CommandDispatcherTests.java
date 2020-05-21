package p0nki.commandparser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import p0nki.commandparser.argument.IntegerArgumentType;
import p0nki.commandparser.node.ArgumentCommandNode;
import p0nki.commandparser.node.LiteralCommandNode;

@SuppressWarnings("unused")
public class CommandDispatcherTests {

    public static CommandDispatcher<String, Integer> dispatcher;

    @BeforeClass
    public static void registerCommands() {
        dispatcher = new CommandDispatcher<>();
        dispatcher.register(new LiteralCommandNode<String, Integer>("test", "test2")
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
                        .then(new ArgumentCommandNode<String, Integer, Integer>("argname", new IntegerArgumentType<String>().minimum(5))
                                .executes(context -> context.get("argname", Integer.class))
                        )
                )
                .then(new LiteralCommandNode<String, Integer>("alias1", "alias2", "alias3")
                        .requires(new CommandRequirement<>() {
                            @Override
                            public boolean isAvailableTo(String s) {
                                return s.equals("allowed");
                            }

                            @Override
                            public String documentation() {
                                return "source must be `allowed`";
                            }
                        })
                        .then(new ArgumentCommandNode<String, Integer, Integer>("argname2", new IntegerArgumentType<String>().maximum(43).minimum(40))
                                .executes(context -> context.get("argname2", Integer.class))
                        )
                )
                .executes(context -> -3)
        );
        System.out.println(dispatcher.generateHelp());
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

}
