package oqube.muse.events;

import oqube.muse.MuseSink;

public class AnchorEvent implements SinkEvent {

  private String anchor;

  public AnchorEvent(String anchor) {
    this.anchor = anchor;
  }

  public void passTo(MuseSink sink) {
    sink.anchor(anchor);
  }

}
