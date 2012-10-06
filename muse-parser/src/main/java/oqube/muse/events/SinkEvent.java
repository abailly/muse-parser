package oqube.muse.events;

import oqube.muse.MuseSink;

public interface SinkEvent {

  SinkEvent NULL_EVENT = new SinkEvent() {

    public void passTo(MuseSink sink) {
    }

    @Override
    public String toString() {
      return "NULL EVENT";
    }

  };

  void passTo(MuseSink sink);

}
