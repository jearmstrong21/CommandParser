package p0nki.commandparser;

import org.junit.Assert;
import org.junit.Test;
import p0nki.commandparser.command.CommandReader;

@SuppressWarnings("unused")
public class CommandReaderTests {

    private CommandReader reader() {
        return new CommandReader("execute as joe ping");
    }

    @Test
    public void testReader() {
        CommandReader reader = reader();
        Assert.assertEquals("execute", reader.readWhile(CommandReader.isNotSpace));
        reader.setIndex(0);
        Assert.assertEquals(0, reader.getIndex());
        Assert.assertEquals("execute", reader.readWhile(CommandReader.isNotSpace));
        Assert.assertEquals("as", reader.readWhile(CommandReader.isNotSpace));
        Assert.assertTrue(reader.canRead());
        Assert.assertEquals("joe", reader.readWhile(CommandReader.isNotSpace));
        Assert.assertEquals("ping", reader.readWhile(CommandReader.isNotSpace));
        Assert.assertFalse(reader.canRead());
    }

}
