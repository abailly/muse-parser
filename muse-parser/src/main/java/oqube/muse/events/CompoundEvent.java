package oqube.muse.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import oqube.muse.MuseSink;

public class CompoundEvent implements SinkEvent {

  private List<SinkEvent> events;

  public CompoundEvent(SinkEvent... events) {
    this.events = Arrays.asList(events);
  }

  public CompoundEvent(List<SinkEvent> events2) {
    this.events = new ArrayList<SinkEvent>(events2);
  }

  public void passTo(MuseSink sink) {
    for (SinkEvent e : events)
      e.passTo(sink);
  }

  @Override
  public boolean equals(Object arg0) {
    try {
      CompoundEvent e = (CompoundEvent) arg0;
      return e.events.equals(events);
    } catch (ClassCastException ex) {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return events.hashCode();
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    for (SinkEvent e : events)
      sb.append(e.toString()).append(" => ");
    return sb.toString();
  }

}
