package oqube.muse.events;

import static oqube.muse.events.EventSequenceMatcher.endWithElement;
import static oqube.muse.events.EventSequenceMatcher.startWithElement;
import static oqube.muse.events.EventSequenceMatcher.textEquals;

import java.io.StringReader;

import junit.framework.TestCase;
import oqube.muse.MuseSink;
import oqube.muse.filter.FilterSink;
import oqube.muse.parser.MuseParser;

import org.jmock.Expectations;
import org.jmock.Mockery;

public class FilterSinkTest extends TestCase {

  private static final String EOL = System.getProperty("line.separator");

  private Mockery mockery = new Mockery();

  private MuseSink mock;

  private FilterSink configureFilter(MuseParser parser, String s) {
    StringReader rd = new StringReader(s);
    parser.setReader(rd);
    FilterSink filter = new FilterSink();
    filter.setSink(mock);
    parser.setSink(filter);
    return filter;
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    mock = mockery.mock(MuseSink.class);
  }

  public void testSinkFilterEventsByName() {
    String s = "This is a paragraph blokr *with* two **lines**";
    MuseParser parser = new MuseParser();
    FilterSink filter = configureFilter(parser, s);
    filter.filterElement("emph");
    mockery.checking(new Expectations() {
      {
        one(mock).start("emph", null);
        one(mock).end("emph");
        one(mock).text("with");
        exactly(0).of(mock).start("strong", null);
      }
    });
    parser.start();
    mockery.assertIsSatisfied();
  }

  public void testSinkFilterSectionEventsByTitle() {
    String s = "This is a text" + EOL + EOL + "* Some title" + EOL + EOL
        + "some text" + EOL + EOL + "* Other title" + EOL + EOL + "some text";
    MuseParser parser = new MuseParser();
    FilterSink filter = configureFilter(parser, s);
    filter.filterSection(1, "Some title");
    mockery.checking(new Expectations() {
      {
        one(mock).start("title1", null);
        one(mock).end("title1");
        one(mock).start("para", null);
        one(mock).end("para");
        one(mock).text("Some title");
        one(mock).text("some text ");
        exactly(0).of(mock).text("This is a text");
      }
    });
    parser.start();
    mockery.assertIsSatisfied();
  }

  public void testSinkFilterSectionEventsIncludingLowerLevels() {
    String s = "This is a text" + EOL + EOL + "* Some title" + EOL + EOL
        + "some text" + EOL + EOL + "** Other title" + EOL + EOL + "some text";
    MuseParser parser = new MuseParser();
    FilterSink filter = configureFilter(parser, s);
    filter.filterSection(1, "Some title");
    mockery.checking(new Expectations() {
      {
        one(mock).start("title1", null);
        one(mock).end("title1");
        one(mock).start("title2", null);
        one(mock).end("title2");
        one(mock).text("Some title");
        exactly(2).of(mock).text("some text ");
        one(mock).text("Other title");
        atLeast(1).of(mock).start("para", null);
        atLeast(1).of(mock).end("para");
      }
    });
    parser.start();
    mockery.assertIsSatisfied();
  }

  public void testSinkFilterSectionEventsExcludingHigherLevels() {
    String s = "This is a text" + EOL + EOL + "** Some title" + EOL + EOL
        + "some text" + EOL + EOL + "* Other title" + EOL + EOL + "some text";
    MuseParser parser = new MuseParser();
    FilterSink filter = configureFilter(parser, s);
    filter.filterSection(2, "Some title");
    mockery.checking(new Expectations() {
      {
        one(mock).start("title2", null);
        one(mock).end("title2");
        one(mock).text("Some title");
        one(mock).text("some text ");
        atLeast(1).of(mock).start("para", null);
        atLeast(1).of(mock).end("para");
      }
    });
    parser.start();
    mockery.assertIsSatisfied();
  }

  @SuppressWarnings("unchecked")
  public void testTwoEventsInSequenceMatched() {
    EventSequenceMatcher matcher = buildMatcher();
    matcher.receive(new StartEvent("toto", null, 1));
    assertTrue("should not match", !matcher.matches(false));
    assertTrue("should partial match", matcher.matches(true));
    matcher.receive(new TextEvent("some text"));
    assertTrue("should match", matcher.matches(false));
  }

  private EventSequenceMatcher buildMatcher() {
    EventSequenceMatcher matcher = new EventSequenceMatcher(
        EventSequenceMatcher.startAtLevel(1), EventSequenceMatcher
            .textEquals("some text"));
    return matcher;
  }

  @SuppressWarnings("unchecked")
  public void testTwoEventsInSequenceMatchedWithinAFlow() {
    EventSequenceMatcher matcher = buildMatcher();
    matcher.receive(new StartEvent("tutu", null, 1)).receive(
        new TextEvent("some text")).receive(new EndEvent("tutu")).receive(
        new StartEvent("toto", null, 1)).receive(new TextEvent("some text"));
    assertTrue("should match", matcher.matches(false));
  }

  public void testSinkFilterSectionEventsWithTitle() {
    String s = "This is a text" + EOL + EOL + "** Some title" + EOL + EOL
        + "some text" + EOL + EOL + "** Some other title" + EOL + EOL
        + "some text" + EOL + EOL + "* Higher level title" + EOL + EOL
        + "some text";
    MuseParser parser = new MuseParser();
    FilterSink filter = configureFilter(parser, s);
    filter.filterSection(2, "Some other title");
    mockery.checking(new Expectations() {
      {
        one(mock).start("title2", null);
        one(mock).end("title2");
        one(mock).text("Some other title");
        one(mock).text("some text ");
        atLeast(1).of(mock).start("para", null);
        atLeast(1).of(mock).end("para");
      }
    });
    parser.start();
    mockery.assertIsSatisfied();
  }

  public void testFilterTextWithRegex() {
    String s = "This is a text with some title" + EOL + EOL
        + "another text that should be filtered out";
    MuseParser parser = new MuseParser();
    FilterSink filter = configureFilter(parser, s);
    filter.filterText(".*that should.*");
    mockery.checking(new Expectations() {
      {
        one(mock).text("another text that should be filtered out ");
      }
    });
    parser.start();
    mockery.assertIsSatisfied();
  }

  @SuppressWarnings("unchecked")
  public void testStartStopFilter() {
    String s = "This is a *text* with some *title* another text that *should* be filtered out";
    MuseParser parser = new MuseParser();
    FilterSink filter = configureFilter(parser, s);
    filter.filterStartStop(new EventSequenceMatcher(startWithElement("emph"),
        textEquals("text")), new EventSequenceMatcher(textEquals("title"),
        endWithElement("emph")));
    mockery.checking(new Expectations() {
      {
        exactly(2).of(mock).start("emph", null);
        exactly(2).of(mock).end("emph");
        one(mock).text(" with some ");
        one(mock).text("text");
        one(mock).text("title");
      }
    });
    parser.start();
    mockery.assertIsSatisfied();
  }

  // public void testInverseStartStopFilter() {
  // String s = "* Some title" + EOL + EOL + "some text" + EOL + EOL
  // + "** Some other title" + EOL + EOL + "some text" + EOL + EOL
  // + "* Higher level title" + EOL + EOL + "some other text";
  // MuseParser parser = new MuseParser();
  // FilterSink sink = configureFilter(parser, s);
  // sink.notFilter().filterSection(1, "Higher level title ");
  // mockery.checking(new Expectations() {
  // {
  // one(mock).start("header", null);
  // one(mock).end("header");
  // one(mock).start("footer", null);
  // one(mock).end("footer");
  // one(mock).start("title2", null);
  // one(mock).end("title2");
  // one(mock).start("body", null);
  // one(mock).end("body");
  // one(mock).start("title1", null);
  // one(mock).end("title1");
  // one(mock).text("Some other title ");
  // one(mock).text("Some title ");
  // atLeast(1).of(mock).text("some text ");
  // atLeast(1).of(mock).start("para", null);
  // atLeast(1).of(mock).end("para");
  // }
  // });
  // parser.start();
  // mockery.assertIsSatisfied();
  // }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }

}
