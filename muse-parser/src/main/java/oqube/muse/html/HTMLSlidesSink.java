package oqube.muse.html;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import oqube.muse.events.CompoundEvent;
import oqube.muse.events.EndEvent;
import oqube.muse.events.EventSequenceMatcher;
import static oqube.muse.events.EventSequenceMatcher.*;
import oqube.muse.events.SinkEvent;
import oqube.muse.events.StartEvent;
import oqube.muse.events.TextEvent;
import oqube.muse.feed.FeedUtils;
import oqube.muse.filter.FilterSink;
import oqube.muse.filter.SinkFilter;

import org.hamcrest.Matcher;
import org.hamcrest.beans.HasPropertyWithValue;
import org.hamcrest.core.Is;

@SuppressWarnings("unchecked")
public class HTMLSlidesSink extends FilterSink implements SinkFilter {

  private int level = 0;

  private Stack<Integer> sections = new Stack<Integer>();

  private EventSequenceMatcher titleMatcher = new EventSequenceMatcher(
      anyTitleStart(), anyText(), anyTitleEnd());

  private EventSequenceMatcher endMatcher = new EventSequenceMatcher(
      anyBelowLevel(0));

  public SinkEvent filter(SinkEvent e) {
    titleMatcher.receive(e);
    endMatcher.receive(e);
    if (titleMatcher.fullMatch()) {
      return handleNewTitle();
    } else if (titleMatcher.partialMatch()) {
      return SinkEvent.NULL_EVENT;
    } else {
      final List<SinkEvent> events = titleMatcher.getEvents();
      if (endMatcher.fullMatch())
        closeSectionToLevel(events, 0);
      CompoundEvent ret = new CompoundEvent(events);
      flushMatcher();
      return ret;
    }
  }

  private void flushMatcher() {
    titleMatcher.getEvents()
                .clear();
  }

  private SinkEvent handleNewTitle() {
    final List<SinkEvent> events = titleMatcher.getEvents();
    Map<String, String> parameters = new HashMap<String, String>() {
      {
        put("id", FeedUtils.escapeTitle(((TextEvent) events.get(1)).getText()));
        put("class", "slide");
      }
    };
    int level = ((StartEvent) events.get(0)).getLevel();
    events.add(0, new StartEvent("section", parameters, level));
    closeSectionToLevel(events, level);
    openSection(level);
    CompoundEvent ret = new CompoundEvent(events);
    flushMatcher();
    return ret;
  }

  private void closeSectionToLevel(final List<SinkEvent> events, int level) {
    while (!this.sections.isEmpty() && this.level >= level) {
      this.level = sections.pop();
      events.add(0, new EndEvent("section", this.level));
    }
  }

  private void openSection(int level) {
    this.sections.push(this.level);
    this.level = level;
  }

  private Matcher<? extends SinkEvent> anyTitleStart() {
    Matcher<? extends SinkEvent>[] matchers = new Matcher[] {
        is(StartEvent.class),
        new HasPropertyWithValue<StartEvent>("element", startsWith("title")) };
    return allOf(matchers);
  }

  private Matcher<? extends SinkEvent> anyTitleEnd() {
    Matcher<? extends SinkEvent>[] matchers = new Matcher[] {
        is(EndEvent.class),
        new HasPropertyWithValue<StartEvent>("element", startsWith("title")) };
    return allOf(matchers);
  }

  public HTMLSlidesSink() {
    setFilter(this);
  }

  public void setOut(PrintWriter pw) {
    super.setOut(pw);
  }

  public void flush() {
    while (!this.sections.isEmpty()) {
      end("section");
      this.sections.pop();
    }
    super.flush();
  }
}
