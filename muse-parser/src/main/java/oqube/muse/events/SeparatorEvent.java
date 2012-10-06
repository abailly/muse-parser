package oqube.muse.events;

import oqube.muse.MuseSink;

public class SeparatorEvent implements SinkEvent {

  public void passTo(MuseSink sink) {
    sink.separator();
  }

}
