package oqube.muse.filter;

import oqube.muse.events.SinkEvent;


public interface SinkFilter {

  SinkEvent filter(SinkEvent e);

}
