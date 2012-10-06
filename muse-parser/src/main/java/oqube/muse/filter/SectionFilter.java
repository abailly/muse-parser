package oqube.muse.filter;

import static oqube.muse.events.EventSequenceMatcher.startBelowLevel;
import static oqube.muse.events.EventSequenceMatcher.startWithElementAtLevel;
import static oqube.muse.events.EventSequenceMatcher.textEquals;
import oqube.muse.events.EventSequenceMatcher;
import oqube.muse.events.SinkEvent;

public class SectionFilter implements SinkFilter {

  private EventSequenceMatcher end;

  private StartFilter startFilter;

  private boolean stopped;

  public SectionFilter(int level, String titleText) {
    this.startFilter = new StartFilter(new EventSequenceMatcher(
        startWithElementAtLevel("title" + level, level), textEquals(titleText)));
    this.end = new EventSequenceMatcher(startBelowLevel(level));
  }

  public SectionFilter(int level, EventSequenceMatcher startMatcher) {
    this.startFilter = new StartFilter(startMatcher);
    this.end = new EventSequenceMatcher(startBelowLevel(level));
  }

  public SinkEvent filter(SinkEvent e) {
    end.receive(e);
    if (stopped)
      return SinkEvent.NULL_EVENT;
    SinkEvent event = startFilter.filter(e);
    if (event != SinkEvent.NULL_EVENT) {
      if (end.matches(false)) {
        stopped = true;
        return SinkEvent.NULL_EVENT;
      }
    }
    return event;
  }

}
