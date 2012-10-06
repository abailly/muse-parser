package oqube.muse.events;

import oqube.muse.MuseSink;

public class RawTextEvent extends TextEvent{

  public RawTextEvent(String txt) {
    super(txt);
  }

  @Override
  public void passTo(MuseSink sink) {
    sink.rawText(getText());
  }

}
