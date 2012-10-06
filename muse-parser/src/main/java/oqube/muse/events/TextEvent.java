package oqube.muse.events;

import oqube.muse.MuseSink;

public class TextEvent implements SinkEvent {

  private String text;

  public TextEvent(String txt) {
    this.text = txt;
  }

  public void passTo(MuseSink sink) {
    sink.text(this.text);
  }

  @Override
  public String toString() {
    return text;
  }

  public String getText() {
    return text;
  }

  @Override
  public boolean equals(Object arg0) {
    try {
      TextEvent e = (TextEvent) arg0;
      return e != null && e.text.equals(text);
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return text.hashCode();
  }

}
