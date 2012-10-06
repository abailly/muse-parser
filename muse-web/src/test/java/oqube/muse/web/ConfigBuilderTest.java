package oqube.muse.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class ConfigBuilderTest {

  private final String EOL = System.getProperty("line.separator");

  @Test
  public void parserSplitInputIntoMethodAndArguments() throws IOException {
    String input = "call 'arg with \"otherArg\"";
    Parser parser = new Parser(new BufferedReader(new StringReader(input)));
    Assert.assertEquals("callWith", parser.content());
    Assert.assertEquals("arg", parser.next.content());
    Assert.assertEquals("otherArg", parser.next.next.content());
    Assert.assertNull(parser.more);
  }

  @Test
  public void parserHandleOnlyArguments() throws IOException {
    String input = " 'arg ";
    Parser parser = new Parser(new BufferedReader(new StringReader(input)));
    Assert.assertEquals("arg", parser.content());
    Assert.assertNull(parser.next);
  }

  @Test
  public void parserHandleArgumentsWithSpecialCharacters() throws IOException {
    String input = " 'arg.-( ";
    Parser parser = new Parser(new BufferedReader(new StringReader(input)));
    Assert.assertEquals("arg.-(", parser.content());
    Assert.assertNull(parser.next);
  }

  @Test
  public void parserHandleArgumentsWithWhiteSpace() throws IOException {
    String input = " \"arg  arg\"";
    Parser parser = new Parser(new BufferedReader(new StringReader(input)));
    Assert.assertEquals("arg  arg", parser.content());
    Assert.assertNull(parser.next);
  }

  @Test
  public void parserHandleOnlyMethod() throws IOException {
    String input = " a complex Method call";
    Parser parser = new Parser(new BufferedReader(new StringReader(input)));
    Assert.assertEquals("aComplexMethodCall", parser.content());
    Assert.assertNull(parser.next);
  }

  @Test
  public void parserHandleSeveralArgs() throws IOException {
    String input = " a complex Method call 'one \"two\" '12";
    Parser parser = new Parser(new BufferedReader(new StringReader(input)));
    Assert.assertEquals("aComplexMethodCall", parser.content());
    Assert.assertEquals("one", parser.next.content());
    Assert.assertEquals("two", parser.next.next.content());
    Assert.assertEquals("12", parser.next.next.next.content());
  }

  @Test
  public void parserOnEmptyStringThrowsException() {
    String input = "";
    Parser parser;
    try {
      parser = new Parser(new BufferedReader(new StringReader(input)));
      Assert.fail("Expected exception");
    } catch (IOException e) {
      // OK
    }
  }

  @Test
  public void parserIterateOverMultipleLines() throws IOException {
    String input = "call \"arg\" with 'otherArg" + EOL
        + " another Call '12 with 'bingo";
    Parser p = new Parser(new BufferedReader(new StringReader(input)));
    Assert.assertEquals("callWith", p.content());
    Assert.assertEquals("arg", p.next.content());
    Assert.assertEquals("otherArg", p.next.next.content());
    Assert.assertEquals("anotherCallWith", p.more.content());
    Assert.assertEquals("12", p.more.next.content());
    Assert.assertNull(p.more.more);
  }

  @Test
  public void parserHandleEmptyLines() throws IOException {
    String input = "  call 'arg with 'otherArg" + EOL + EOL
        + " another Call \"12\" with 'bingo";
    Parser p = new Parser(new BufferedReader(new StringReader(input)));
    Assert.assertEquals("callWith", p.content());
    Assert.assertEquals("", p.more.content());
    Assert.assertEquals("12", p.more.more.next.content());
  }


  @Test
  public void parserCanBeConstructedFromReader() throws IOException {
    String input = "  call 'arg with 'otherArg" + EOL + EOL
        + " another Call \"12\" with 'bingo";
    Parser p = new Parser(new BufferedReader(new StringReader(input)));
    Assert.assertEquals("callWith", p.content());
    Assert.assertEquals("", p.more.content());
    Assert.assertEquals("12", p.more.more.next.content());

  }


  @Test
  public void configurationBuilderCreateServerConfigFromReader()
      throws IOException {
    String tmpdir = System.getProperty("java.io.tmpdir");
    String config = "new config 'toto" + EOL + "add mappers 'muse 'html 'text"
        + EOL + "set root directory \"" + tmpdir + "\"" + EOL
        + "set inputEncoding \"UTF-8\"" + EOL;
    ServerConfigBuilder b = new ServerConfigBuilder();
    b.buildFromReader(new StringReader(config));
    Map<String, ServerConfig> confs = b.getConfigurations();
    Assert.assertEquals(1, confs.size());
    Assert.assertEquals(new File(tmpdir), confs.entrySet().iterator().next()
        .getValue().getRootDirectory());
  }

  @Test
  public void configurationBuilderCreateTwoServerConfigsFromReader()
      throws IOException {
    String tmpdir = System.getProperty("java.io.tmpdir");
    String config = "new config 'toto" + EOL + "add mappers 'muse 'html 'text"
        + EOL + "set root directory \"" + tmpdir + "\"" + EOL
        + "set inputEncoding \"UTF-8\"" + EOL + EOL
        + "new config 'tutu map to \"/plan\"" + EOL
        + "add mappers 'muse 'plannerPage 'text" + EOL
        + "set root directory \"" + tmpdir + "\"" + EOL
        + "set inputEncoding \"UTF-8\"" + EOL;
    ServerConfigBuilder b = new ServerConfigBuilder();
    b.buildFromReader(new StringReader(config));
    Map<String, ServerConfig> confs = b.getConfigurations();
    Assert.assertEquals(2, confs.size());
  }

}
