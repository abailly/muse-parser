package oqube.muse.events;

import oqube.muse.MuseSink;

public class EndEvent implements SinkEvent {

  private String element;

  private int level;

  public EndEvent(String element, int level) {
    this.setElement(element);
    this.level = level;
  }

  public EndEvent(String element) {
    this(element, Integer.MAX_VALUE);
  }

  public void passTo(MuseSink sink) {
    sink.end(this.getElement());
  }

  public void setElement(String element) {
    this.element = element;
  }

  public String getElement() {
    return element;
  }

  @Override
  public String toString() {
    return "</" + element + ">";
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

}
