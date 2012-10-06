package oqube.muse.html;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.xpath.XPathExpressionException;

import junit.framework.TestCase;

public class SlidePublisherTest extends TestCase {

  public void testSlideIsOutputAsHTMLWithSections() throws IOException,
      XPathExpressionException {
    StringReader input = new StringReader(
        "* Some title\n\nsome content for the slide");
    StringWriter back = new StringWriter();
    PrintWriter output = new PrintWriter(back);
    SlidePublisher pub = new SlidePublisher();
    pub.startSession("1");
    pub.process("1", input, output);
    output.flush();
    final String content = back.toString();
    assertTrue("expected slide div not found: " + content, content
        .contains("class=\"slide\""));
  }

  public void testSourceTagsArePublishedUnadorned() throws Exception {
    StringReader input = new StringReader(
        "*** Some title\n\n"
            + "<src name=\"toto\" language=\"js\">\n" + "some code\n"
            + "</src>");
    StringWriter back = new StringWriter();
    PrintWriter output = new PrintWriter(back);
    SlidePublisher pub = new SlidePublisher();
    pub.startSession("1");
    pub.process("1", input, output);
    output.flush();
    final String content = back.toString();
    assertTrue("expected source content has no links: " + content, !content
        .contains("<a name=\"id0\""));
  }
}
