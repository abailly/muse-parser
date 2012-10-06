package oqube.muse.filter;

import oqube.muse.events.EventSequenceMatcher;
import oqube.muse.events.SinkEvent;

public class StopFilter implements SinkFilter {

  private EventSequenceMatcher matcher;

  private boolean stopped;

  public StopFilter(EventSequenceMatcher m) {
    this.matcher = m;
    this.stopped = this.matcher.matchAll();
  }

  public SinkEvent filter(SinkEvent event) {
    this.matcher.receive(event);
    if (this.stopped)
      return SinkEvent.NULL_EVENT;
    else if (this.matcher.matches(false)) {
      this.stopped = true;
      return event;
    } else
      return event;
  }

}
