package oqube.muse;

import java.io.StringReader;

import oqube.muse.parser.MuseParser;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public class MuseParserFragmentTest extends MockObjectTestCase {
  private static final String EOL = System.getProperty("line.separator");

  private Mock mock;

  private MuseParser parser;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    // create dummy sink
    mock = mock(MuseSink.class);
    parser = new MuseParser();
    parser.setSink((MuseSink) mock.proxy());
    parser.setFragment(true);
  }

  public void testSettingFragmentModeInMuseParser() {
    String s = "This is a paragraph blokr" + EOL
        + "anethro line in the paragraphh" + EOL + EOL + "another para" + EOL
        + "with two lines";
    StringReader rd = new StringReader(s);
    parser.setReader(rd);
    // MuseHTMLSink sk = new MuseHTMLSink();
    // sk.setOut(new PrintWriter(System.out));
    // parser.setSink(sk);
    mock.expects(exactly(2)).method("text");
    mock.expects(exactly(2)).method("start").with(eq("para"),ANYTHING);
    mock.expects(exactly(2)).method("end").with(eq("para"));
    parser.start();
  }

  public void testUnsettingFragmentModeInMuseParser() {
    String s = "This is a paragraph blokr" + EOL
        + "anethro line in the paragraphh" + EOL + EOL + "another para" + EOL
        + "with two lines";
    StringReader rd = new StringReader(s);
    parser.setReader(rd);
    parser.setFragment(false);
    // MuseHTMLSink sk = new MuseHTMLSink();
    // sk.setOut(new PrintWriter(System.out));
    // parser.setSink(sk);
    mock.expects(once()).method("start").with(eq("header"),ANYTHING);
    mock.expects(once()).method("end").with(eq("header"));
    mock.expects(once()).method("start").with(eq("footer"),ANYTHING);
    mock.expects(once()).method("end").with(eq("footer"));
    mock.expects(once()).method("start").with(eq("body"),ANYTHING);
    mock.expects(once()).method("end").with(eq("body"));
    mock.expects(exactly(2)).method("text");
    mock.expects(exactly(2)).method("start").with(eq("para"),ANYTHING);
    mock.expects(exactly(2)).method("end").with(eq("para"));
    parser.start();
  }

  @Override
  protected void tearDown() throws Exception {
    // TODO Auto-generated method stub
    super.tearDown();
  }

}
