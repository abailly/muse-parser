package oqube.muse.html;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import oqube.muse.DefaultSink;
import oqube.muse.TemplateSinkTest;
import oqube.muse.template.Template;

public class MuseHTMLSinkTest extends TemplateSinkTest {

  private StringWriter sw;

  private PrintWriter pw;

  private MuseHTMLSink sink;

  public void testTextShouldEscapeXMLSpecialCharacters() {
    sink.text("<some>data & sp\"cial chars");
    sink.flush();
    pw.flush();
    assertEquals("Characters are not escaped",
        "&lt;some&gt;data &amp; sp&quot;cial chars", sw.toString());
  }

  public void testRawTextShouldNotEscapeCharacters() {
    sink.rawText("<some>data & sp\"cial chars");
    sink.flush();
    pw.flush();
    assertEquals("Characters are escaped ?!", "<some>data & sp\"cial chars", sw
        .toString());
  }

  public void testTextShouldNotEscapeNonAsciiCharacters() {
    sink.text("soci\u00e9t\u00e9");
    sink.flush();
    pw.flush();
    assertEquals("Characters are escaped ?!", "soci\u00e9t\u00e9", sw
        .toString());
  }

  public void testNoHeadTagIsOutputIfHeaderIsSet() {
    sink.setHeader(new Template() {

      public String content(Map<Object, Object> environment) {
        return "dummy";
      }

    });
    sink.start("header", null);
    sink.end("header");
    pw.flush();
    assertEquals("Incorrect output", "dummy", sw.toString());
  }

  public void testHeadTagIsOutputIfHeaderIsNotSet() {
    sink.start("header", null);
    sink.end("header");
    pw.flush();
    assertTrue("Incorrect output", sw.toString().matches(
        "<head>\\s*</head>\\s*"));
  }

  public void testBodyStartTagIsOutputIfHeaderIsNotSet() {
    sink.start("body", null);
    pw.flush();
    assertTrue("Incorrect output", sw.toString().matches("<body>\\s*"));
  }

  public void testBodyEndTagIsOutputIfFooterIsNotSet() {
    sink.end("body");
    pw.flush();
    assertTrue("Incorrect output", sw.toString().matches("\\s*</body>\\s*"));
  }

  public void testNoStartBodyTagOutputIfHeaderIsSet() {
    sink.setHeader(new Template() {

      public String content(Map<Object, Object> environment) {
        return "dummy";
      }

    });
    sink.start("header", null);
    sink.end("header");
    sink.start("body", null);
    pw.flush();
    assertEquals("Incorrect output", "dummy", sw.toString());
  }

  public void testNoEndBodyTagOutputIfFooterIsSet() {
    sink.setFooter(new Template() {

      public String content(Map<Object, Object> environment) {
        return "dummy";
      }

    });
    sink.end("body");
    sink.start("footer", null);
    sink.end("footer");
    pw.flush();
    assertEquals("Incorrect output", "dummy", sw.toString());
  }

  public void testFormatPlannerTasksAsDivs() {
    sink.setLinker(new DefaultHTMLLinker());
    sink.start("tasks", null);
    sink.start("task", new HashMap<String, String>() {
      {
        put("priority", "A");
        put("level", "0");
        put("status", "_");
        put("description", "this a *text* with a link to [[Link#12][(12)]]");
        put("link", "2007.11.01");
      }
    });
    sink.end("task");
    sink.end("tasks");
    sink.flush();
    pw.flush();
    assertTrue(
        "bad task format",
        sw
            .toString()
            .contains(
                "<span class=\"priorityA\"><span class=\"level0\"><span class=\"status_\">")
            && sw
                .toString()
                .contains(
                    "this a <em>text</em> with a link to <a href=\"Link#12\">(12)</a>"));
  }

  public void testTitle1SectionProducesAnAnchor() {
    sink.setLinker(new DefaultHTMLLinker());
    sink.start("title1", null);
    sink.text("some title%with ' various4$ strange##characterès");
    sink.end("title1");
    pw.flush();
    assertTrue("no anchor generated from level 1 title", sw.toString()
        .contains(
            "<a name=\"some-title-with---various4--strange--character-s\"></a>"));

  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    sw = new StringWriter();
    pw = new PrintWriter(sw);
    sink = new MuseHTMLSink();
    sink.setOut(pw);
  }

  @Override
  protected DefaultSink makeSink() {
    return new MuseHTMLSink();
  }

}
