package oqube.muse.events;

import oqube.muse.MuseSink;

public class MetadataEvent implements SinkEvent {

  private String name;

  private String value;

  public MetadataEvent(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public void passTo(MuseSink sink) {
    sink.addMetadata(name, value);
  }

  @Override
  public String toString() {
    return "#" + name + " " + value;
  }

}
