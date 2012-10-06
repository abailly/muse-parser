package oqube.muse.filter;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import oqube.muse.events.EventSequenceMatcher;
import oqube.muse.events.SinkEvent;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.beans.HasPropertyWithValue;

public class TagFilter implements SinkFilter {

  private Matcher<SinkEvent> matcher;

  public TagFilter(String tag) {
    this.matcher = Matchers.anyOf(EventSequenceMatcher.startWithElement(tag),
        EventSequenceMatcher.endWithElement(tag));
  }

  public SinkEvent filter(SinkEvent e) {
    if (matcher.matches(e))
      return e;
    else
      return SinkEvent.NULL_EVENT;
  }

}
