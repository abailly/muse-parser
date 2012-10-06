package oqube.muse.events;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.beans.HasPropertyWithValue;
import org.hamcrest.number.OrderingComparisons;

public class EventSequenceMatcher {

  private Matcher<? extends SinkEvent>[] matchers;

  private LinkedList<SinkEvent> events;

  private int max;

  public EventSequenceMatcher(Matcher<? extends SinkEvent>... matchers) {
    this.matchers = matchers;
    this.events = new LinkedList<SinkEvent>();
    this.max = this.matchers.length;
  }

  public EventSequenceMatcher receive(SinkEvent event) {
    if (!matchAll()) {
      if (isFull()) {
        this.events.removeFirst();
      }
      this.events.addLast(event);
    }
    return this;
  }

  public boolean matchAll() {
    return max == 0;
  }

  private boolean isFull() {
    return this.events.size() == max;
  }

  public boolean matches(boolean partial) {
    int i = 0;
    boolean result = true;
    for (SinkEvent e : events) {
      result &= matchers[i++].matches(e);
    }
    return partial ? result : result && (i == max);
  }

  @SuppressWarnings("unchecked")
  public static Matcher<TextEvent> textEquals(String text) {
    Matcher<TextEvent>[] matchers = new Matcher[] { is(TextEvent.class),
        new HasPropertyWithValue<TextEvent>("text", equalTo(text)) };
    return allOf(matchers);
  }

  @SuppressWarnings("unchecked")
  public static Matcher<StartEvent> startAtLevel(int level) {
    Matcher<StartEvent>[] matchers = new Matcher[] { is(StartEvent.class),
        new HasPropertyWithValue<StartEvent>("level", equalTo(level)) };
    return allOf(matchers);
  }

  @SuppressWarnings("unchecked")
  public static Matcher<? extends SinkEvent> startWithElementAtLevel(
      String element, int level) {
    Matcher<? extends SinkEvent>[] matchers = new Matcher[] {
        is(StartEvent.class),
        new HasPropertyWithValue<StartEvent>("level", equalTo(level)),
        new HasPropertyWithValue<StartEvent>("element", equalTo(element)) };
    return allOf(matchers);
  }

  @SuppressWarnings("unchecked")
  public static Matcher<? extends SinkEvent> startWithElement(String element) {
    Matcher<? extends SinkEvent>[] matchers = new Matcher[] {
        is(StartEvent.class),
        new HasPropertyWithValue<StartEvent>("element", equalTo(element)) };
    return allOf(matchers);
  }

  @SuppressWarnings("unchecked")
  public static Matcher<? extends SinkEvent> endWithElement(String element) {
    Matcher<? extends SinkEvent>[] matchers = new Matcher[] {
        is(EndEvent.class),
        new HasPropertyWithValue<EndEvent>("element", equalTo(element)) };
    return allOf(matchers);
  }

  @SuppressWarnings("unchecked")
  public static Matcher<? extends SinkEvent> startBelowLevel(int level) {
    Matcher<? extends SinkEvent>[] matchers = new Matcher[] {
        is(StartEvent.class),
        new HasPropertyWithValue<StartEvent>("level",
            OrderingComparisons.lessThanOrEqualTo(level)) };
    return allOf(matchers);
  }

  @SuppressWarnings("unchecked")
  public static Matcher<? extends SinkEvent> anyBelowLevel(int level) {
    Matcher<? extends SinkEvent>[] matchers = new Matcher[] {
        anyOf(is(StartEvent.class), is(EndEvent.class)),
        new HasPropertyWithValue<StartEvent>("level",
            OrderingComparisons.lessThanOrEqualTo(level)) };
    return allOf(matchers);
  }

  @SuppressWarnings("unchecked")
  public static Matcher<? extends SinkEvent> anyText() {
    Matcher<? extends SinkEvent>[] matchers = new Matcher[] { is(TextEvent.class) };
    return allOf(matchers);
  }

  public List<SinkEvent> getEvents() {
    return events;
  }

  public boolean fullMatch() {
    return matches(false);
  }

  public boolean partialMatch() {
    return matches(true);
  }

}
