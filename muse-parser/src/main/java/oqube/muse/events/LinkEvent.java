package oqube.muse.events;

import oqube.muse.MuseSink;

public class LinkEvent implements SinkEvent {

  private String link;
  private String text;

  public LinkEvent(String link, String text) {
    this.link = link;
    this.text = text;
  }

  public void passTo(MuseSink sink) {
    sink.link(link, text);
  }

}
