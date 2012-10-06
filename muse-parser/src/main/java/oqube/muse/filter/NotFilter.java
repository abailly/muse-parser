package oqube.muse.filter;

import oqube.muse.events.SinkEvent;


public class NotFilter implements FilterBuilder, SinkFilter {

  private SinkFilter innerFilter;

  public void filterSection(int i, String string) {
    this.innerFilter = new SectionFilter(i, string);
  }

  public SinkEvent filter(SinkEvent event) {
    SinkEvent e = this.innerFilter.filter(event);
    if (e == SinkEvent.NULL_EVENT)
      return event;
    else
      return SinkEvent.NULL_EVENT;
  }

}
