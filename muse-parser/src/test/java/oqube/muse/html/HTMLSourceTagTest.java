package oqube.muse.html;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import oqube.muse.DefaultSink;
import oqube.muse.parser.MuseParser;

public class HTMLSourceTagTest extends TestCase {

  private LinkingHTMLSourceTag handler;

  private DefaultSink sink;

  private MuseParser parser;

  private StringBuffer frags = new StringBuffer();

  protected void setUp() throws Exception {
    super.setUp();
    handler = new LinkingHTMLSourceTag();
    sink = new DefaultSink() {

      public void rawText(String text) {
        frags.append(text);
      }

      public void anchor(String a) {
        frags.append("<a name=\"").append(a).append("\"/>");
      }

      public void link(String s, String t) {
        frags.append("<a href=\"").append(s).append("\">").append(t).append(
            "</a>");
      }

    };
    sink.setTagHandler(handler);
    sink.start("document", null);
    sink.setOut(new PrintWriter(new StringWriter()));
    parser = new MuseParser();
    parser.setSink(sink);
  }
  public void test01Basic() {
    parser.setStream(getClass().getResourceAsStream("/testSourceTag1.muse"));
    parser.start();
    System.err.println(frags);
    Pattern pat = Pattern.compile(
        ".*&lt;&lt;fields&gt;&gt;.*<a name=\"id3\"/>.*", Pattern.DOTALL);
    // check generated fragments
    assertTrue(pat.matcher(frags).matches());
    // check <src> tag
    pat = Pattern.compile(".*&lt;&lt;fields&gt;&gt;.*<a name=\"id7\"/>.*",
        Pattern.DOTALL);
    // check generated fragments
    assertTrue(pat.matcher(frags).matches());
    // check previous links
    pat = Pattern.compile(".*<a href=\"id6\">compute.*", Pattern.DOTALL);
    // check generated fragments
    assertTrue(pat.matcher(frags).matches());
  }

  public void test02Haskell() {
    parser.setStream(getClass().getResourceAsStream(
        "/testSourceTagHaskell.muse"));
    parser.start();
    Pattern pat = Pattern.compile(".*<pre class=\"haskell\".*", Pattern.DOTALL);
    // check generated fragments
    assertTrue(pat.matcher(frags).matches());
  }
}
