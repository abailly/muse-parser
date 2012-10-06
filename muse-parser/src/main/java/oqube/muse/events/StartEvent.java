package oqube.muse.events;

import java.util.Map;

import oqube.muse.MuseSink;

public class StartEvent implements SinkEvent {

  private String element;

  private Map<String, String> parameters;

  private int level = Integer.MAX_VALUE;

  public StartEvent(String element, Map<String, String> parameters, int level) {
    this.setElement(element);
    this.parameters = parameters;
    this.level = level;
  }

  public void passTo(MuseSink sink) {
    sink.start(this.getElement(), this.parameters);
  }

  public void setElement(String element) {
    this.element = element;
  }

  public String getElement() {
    return element;
  }

  @Override
  public String toString() {
    return "<" + element + ">";
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

}
