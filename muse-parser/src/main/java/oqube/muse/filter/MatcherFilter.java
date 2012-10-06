package oqube.muse.filter;

import oqube.muse.events.SinkEvent;

import org.hamcrest.Matcher;

public class MatcherFilter implements SinkFilter {

  private Matcher<? extends SinkEvent> matcher;

  public MatcherFilter(Matcher<? extends SinkEvent> matcher) {
    this.matcher = matcher;
  }

  public SinkEvent filter(SinkEvent e) {
    if (matcher.matches(e))
      return e;
    else
      return SinkEvent.NULL_EVENT;
  }

}
