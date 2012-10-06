package oqube.muse.literate;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import oqube.muse.DefaultSink;
import oqube.muse.parser.MuseParser;
import oqube.muse.MuseSink;
import oqube.muse.MuseTagHandler;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.Constraint;

import junit.framework.TestCase;

public class LiterateSourceHandlerTest extends MockObjectTestCase {

  private SourceRepository handler;

  private DefaultSink sink;

  private MuseParser parser;

  private Mock mock;

  protected void setUp() throws Exception {
    super.setUp();
    handler = new DefaultSourceRepository();
    mock = mock(MuseTagHandler.class);
    sink = new DefaultSink();
    sink.setTagHandler((MuseTagHandler) mock.proxy());
    sink.start("document", null);
    sink.setOut(new PrintWriter(new StringWriter()));
    parser = new MuseParser();
    parser.setSink(sink);
  }

  public void test01Basic() {
    parser.setStream(getClass().getResourceAsStream("/testSourceTag1.muse"));
    mock.expects(once()).method("block").with(same(sink), eq("source"),
        checkArgs(new String[][] { { "name", "Toto.java" } }), ANYTHING).will(
        returnValue(true));
    mock.expects(exactly(2)).method("block").with(same(sink), eq("source"),
        checkArgs(new String[][] { { "name", "fields" } }), ANYTHING).will(
        returnValue(true));
    mock.expects(exactly(1)).method("block").with(same(sink), eq("source"),
        checkArgs(new String[][] { { "name", "methods" } }), ANYTHING).will(
        returnValue(true));
    mock.expects(exactly(4)).method("block").with(same(sink), eq("source"),
        checkArgs(new String[][] { { "name", "power" } }), ANYTHING).will(
        returnValue(true));
    mock.expects(exactly(1)).method("block").with(same(sink), eq("source"),
        checkArgs(new String[][] { { "name", "compute" } }), ANYTHING).will(
        returnValue(true));
    parser.start();
  }

  public void test02RefTree() {
    parser.setStream(getClass().getResourceAsStream("/testSourceTag1.muse"));
    // handler passes fragments to sources repo
    sink.setTagHandler(new MuseTagHandler() {

      public boolean flow(MuseSink sink, String tag, String[][] at,
          String content) {
        // TODO Auto-generated method stub
        return false;
      }

      public boolean block(MuseSink sink, String tag, String[][] at,
          String content) {
        handler.addFragment(at[0][1], content);
        return true;
      }

    });
    parser.start();
    // check source repo has correct tree
    Map m = handler.collectSources();
    assertEquals(1, m.size());
    assertNotNull(m.get("Toto.java"));
    System.err.println(m.get("Toto.java"));
    assertTrue(((String) m.get("Toto.java"))
        .contains("matrix[i][m]* matrix[m][j];"));
  }

  public void test03LenientRefTree() {
    parser.setStream(getClass().getResourceAsStream(
        "/testSourceTagLenient.muse"));
    // handler passes fragments to sources repo
    sink.setTagHandler(new MuseTagHandler() {

      public boolean flow(MuseSink sink, String tag, String[][] at,
          String content) {
        // TODO Auto-generated method stub
        return false;
      }

      public boolean block(MuseSink sink, String tag, String[][] at,
          String content) {
        handler.addFragment(at[0][1], content);
        return true;
      }

    });
    // check source repo has correct tree
    handler.setLenient(true);
    parser.start();
    Map m = handler.collectSources();
    assertEquals(1, m.size());
    assertNotNull(m.get("Toto.java"));
    assertFalse(((String) m.get("Toto.java")).matches("<<types>>"));
  }

  public void test04NonLenientRefTreeError() {
    parser.setStream(getClass().getResourceAsStream(
        "/testSourceTagLenient.muse"));
    // handler passes fragments to sources repo
    sink.setTagHandler(new MuseTagHandler() {

      public boolean flow(MuseSink sink, String tag, String[][] at,
          String content) {
        // TODO Auto-generated method stub
        return false;
      }

      public boolean block(MuseSink sink, String tag, String[][] at,
          String content) {
        handler.addFragment(at[0][1], content);
        return true;
      }

    });
    // check source repo has correct tree
    parser.start();
    try {
      Map m = handler.collectSources();
      fail("Should have thrown illegalargumentexception");
    } catch (IllegalArgumentException e) {
      // OK
    }
  }

  private Constraint checkArgs(final String[][] strings) {
    return new Constraint() {

      public StringBuffer describeTo(StringBuffer arg0) {
        return arg0.append("check tag attributes");
      }

      public boolean eval(Object arg0) {
        String[][] tab = (String[][]) arg0;
        if (tab.length != strings.length)
          return false;
        for (int i = 0; i < tab.length; i++) {
          if (tab[i].length != strings[i].length)
            return false;
          for (int j = 0; j < tab[i].length; j++)
            if (!tab[i][j].equals(strings[i][j]))
              return false;

        }
        return true;
      }

    };
  }
}
