package oqube.muse.filter;

import static oqube.muse.events.EventSequenceMatcher.endWithElement;
import static oqube.muse.events.EventSequenceMatcher.startWithElement;
import oqube.muse.events.EventSequenceMatcher;

public class ElementFilter extends StartStopFilter {

  private String element;

  private boolean filtering;

  public ElementFilter(String element) {
    super(new EventSequenceMatcher(startWithElement(element)),
        new EventSequenceMatcher(endWithElement(element)));
  }

}
