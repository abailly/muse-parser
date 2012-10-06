package oqube.muse.filter;

import oqube.muse.events.EventSequenceMatcher;
import oqube.muse.events.SinkEvent;


public class StartStopFilter implements SinkFilter {

  private StartFilter startFilter;

  private StopFilter stopFilter;

  public StartStopFilter(EventSequenceMatcher startMatcher,
      EventSequenceMatcher stopMatcher) {
    startFilter = new StartFilter(startMatcher);
    stopFilter = new StopFilter(stopMatcher);
  }

  public SinkEvent filter(SinkEvent event) {
    return stopFilter.filter(startFilter.filter(event));
  }

}
