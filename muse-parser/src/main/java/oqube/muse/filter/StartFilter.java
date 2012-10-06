package oqube.muse.filter;

import java.util.List;

import oqube.muse.events.CompoundEvent;
import oqube.muse.events.EventSequenceMatcher;
import oqube.muse.events.SinkEvent;

public class StartFilter implements SinkFilter {

  private EventSequenceMatcher matcher;

  private boolean started;

  public StartFilter(EventSequenceMatcher m) {
    this.matcher = m;
    this.started = false;
  }

  public SinkEvent filter(SinkEvent event) {
    this.matcher.receive(event);
    if (this.started)
      return event;
    else if (this.matcher.matches(false)) {
      this.started = true;
      return extractBufferedEvents(event);
    } else
      return SinkEvent.NULL_EVENT;
  }

  private SinkEvent extractBufferedEvents(SinkEvent event) {
    final List<SinkEvent> bufferedEvents = this.matcher.getEvents();
    if (bufferedEvents.size() > 1)
      return new CompoundEvent(bufferedEvents);
    else if (bufferedEvents.size() == 1)
      return bufferedEvents.get(0);
    else
      return event;
  }

}
