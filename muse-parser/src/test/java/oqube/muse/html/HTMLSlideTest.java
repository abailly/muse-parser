package oqube.muse.html;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import oqube.muse.MuseSink;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;

public class HTMLSlideTest extends TestCase {

  private StringWriter sw;

  private PrintWriter pw;

  private HTMLSlidesSink sink;

  Mockery context = new Mockery();

  private MuseSink backend;

  public void testSlideSinkPassEventsAndOutputToItsBackend() throws Exception {
    context.checking(new Expectations() {
      {
        one(backend).text("some text");
        one(backend).setOut(pw);
        one(backend).addMetadata("toto", "tutu");
        one(backend).anchor("anchor");
      }
    });
    sink.setSink(backend);
    sink.setOut(pw);
    sink.text("some text");
    sink.addMetadata("toto", "tutu");
    sink.anchor("anchor");
    context.assertIsSatisfied();
  }

  public void testSlideOutputStartingDivWhenItEncountersTitle1()
      throws Exception {
    ExpectationsExt exp = expectSectioning();
    exp.start("section", withTitle("some-title"))
       .start("title1")
       .text("some title")
       .end("title1");
    exp.one(backend)
       .setOut(pw);
    context.checking(exp);
    sink.setSink(backend);
    sink.setOut(pw);
    sink.start("title1", null);
    sink.text("some title");
    sink.end("title1");
    context.assertIsSatisfied();
  }

  public void testSlideOutputEndingDivWhenItIsFlushed() throws Exception {
    ExpectationsExt exp = expectSectioning();
    exp.start("section", withTitle("some-title"))
       .start("title1")
       .text("some title")
       .end("title1")
       .end("section");
    exp.one(backend)
       .setOut(pw);
    exp.one(backend)
       .flush();
    context.checking(exp);
    sink.setSink(backend);
    sink.setOut(pw);
    sink.start("title1", null);
    sink.text("some title");
    sink.end("title1");
    sink.flush();
    context.assertIsSatisfied();
  }

  public void testSlideOutputTwoSectionsWithTwoTitles() throws Exception {
    ExpectationsExt exp = expectSectioning();
    exp.start("section", withTitle("some-title"))
       .start("title1")
       .text("some title")
       .end("title1")
       .end("section")
       .start("section", withTitle("other-title"))
       .start("title1")
       .text("other title")
       .end("title1")
       .end("section");
    exp.one(backend)
       .setOut(pw);
    exp.one(backend)
       .flush();
    context.checking(exp);
    sink.setSink(backend);
    sink.setOut(pw);
    sink.start("title1", null);
    sink.text("some title");
    sink.end("title1");
    sink.start("title1", null);
    sink.text("other title");
    sink.end("title1");
    sink.flush();
    context.assertIsSatisfied();
  }

  public void testSlideOutputTwoNestedSectionsWithTwoNestedTitles()
      throws Exception {
    ExpectationsExt exp = expectSectioning();
    exp.start("section", withTitle("some-title"))
       .start("title1")
       .text("some title")
       .end("title1")
       .start("section", withTitle("other-title"))
       .start("title2")
       .text("other title")
       .end("title2")
       .end("section")
       .end("section");
    exp.one(backend)
       .setOut(pw);
    exp.one(backend)
       .flush();
    context.checking(exp);
    sink.setSink(backend);
    sink.setOut(pw);
    sink.start("title1", null);
    sink.text("some title");
    sink.end("title1");
    sink.start("title2", null);
    sink.text("other title");
    sink.end("title2");
    sink.flush();
    context.assertIsSatisfied();
  }

  public void testSlideOutputTwoNonNestedSectionsWithTwoTitles()
      throws Exception {
    ExpectationsExt exp = expectSectioning();
    exp.start("section", withTitle("some-title"))
       .start("title2")
       .text("some title")
       .end("title2")
       .end("section")
       .start("section", withTitle("other-title"))
       .start("title1")
       .text("other title")
       .end("title1")
       .end("section");
    exp.one(backend)
       .setOut(pw);
    exp.one(backend)
       .flush();
    context.checking(exp);
    sink.setSink(backend);
    sink.setOut(pw);
    sink.start("title2", null);
    sink.text("some title");
    sink.end("title2");
    sink.start("title1", null);
    sink.text("other title");
    sink.end("title1");
    sink.flush();
    context.assertIsSatisfied();
  }

  public void testSlideOutputCorrectSectionningAccordingToTitles()
      throws Exception {
    ExpectationsExt exp = expectSectioning();
    exp.start("section", withTitle("some-title"))
       .start("title1")
       .text("some title")
       .end("title1")
       .start("section", withTitle("other-title"))
       .start("title2")
       .text("other title")
       .end("title2")
       .start("section", withTitle("other-title"))
       .start("title3")
       .text("other title")
       .end("title3")
       .end("section")
       .end("section")
       .start("section", withTitle("other-title"))
       .start("title2")
       .text("other title")
       .end("title2")
       .end("section")
       .end("section")
       .start("section", withTitle("other-title"))
       .start("title1")
       .text("other title")
       .end("title1")
       .end("section");
    exp.one(backend)
       .setOut(pw);
    exp.one(backend)
       .flush();
    context.checking(exp);
    sink.setSink(backend);
    sink.setOut(pw);
    sink.start("title1", null);
    sink.text("some title");
    sink.end("title1");
    sink.start("title2", null);
    sink.text("other title");
    sink.end("title2");
    sink.start("title3", null);
    sink.text("other title");
    sink.end("title3");
    sink.start("title2", null);
    sink.text("other title");
    sink.end("title2");
    sink.start("title1", null);
    sink.text("other title");
    sink.end("title1");
    sink.flush();
    context.assertIsSatisfied();
  }

  public void testSlideOutputSectionningAtBodyEnd() throws Exception {
    ExpectationsExt exp = expectSectioning();
    exp.start("body")
       .start("section", withTitle("some-title"))
       .start("title1")
       .text("some title")
       .end("title1")
       .start("section", withTitle("other-title"))
       .start("title2")
       .text("other title")
       .end("title2")
       .end("section")
       .end("section")
       .end("body");
    exp.one(backend)
       .setOut(pw);
    context.checking(exp);
    sink.setSink(backend);
    sink.setOut(pw);
    sink.start("body", null);
    sink.start("title1", null);
    sink.text("some title");
    sink.end("title1");
    sink.start("title2", null);
    sink.text("other title");
    sink.end("title2");
    sink.end("body");
    context.assertIsSatisfied();
  }

  private ExpectationsExt expectSectioning() {
    final Sequence sections = context.sequence("sections");
    ExpectationsExt exp = new ExpectationsExt(sections);
    return exp;
  }

  class ExpectationsExt extends Expectations {
    private Sequence sections;

    public ExpectationsExt(Sequence sections) {
      this.sections = sections;
    }

    public ExpectationsExt end(String tag) {
      one(backend).end(tag);
      inSequence(this.sections);
      return this;
    }

    public ExpectationsExt text(String string) {
      one(backend).text(string);
      inSequence(this.sections);
      return this;
    }

    ExpectationsExt start(String tag) {
      one(backend).start(tag, null);
      inSequence(this.sections);
      return this;
    }

    ExpectationsExt start(String tag, Map<String, String> parameterMap) {
      one(backend).start(tag, parameterMap);
      inSequence(this.sections);
      return this;
    }

  }

  private HashMap<String, String> withTitle(final String title) {
    return new HashMap<String, String>() {
      {
        put("id", title);
        put("class", "slide");
      }
    };
  }

  public void setUp() throws Exception {
    super.setUp();
    sw = new StringWriter();
    pw = new PrintWriter(sw);
    sink = new HTMLSlidesSink();
    backend = context.mock(MuseSink.class);
  }

}
