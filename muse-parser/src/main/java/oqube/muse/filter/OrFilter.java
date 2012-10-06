package oqube.muse.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import oqube.muse.events.SinkEvent;

public class OrFilter implements SinkFilter {

  private List<SinkFilter> filters;

  public OrFilter(SinkFilter... filters) {
    this.filters = Arrays.asList(filters);
  }

  public SinkEvent filter(SinkEvent e) {
    for (SinkFilter filter : filters) {
      SinkEvent ret = filter.filter(e);
      if (ret != SinkEvent.NULL_EVENT)
        return ret;
    }
    return SinkEvent.NULL_EVENT;
  }

}
