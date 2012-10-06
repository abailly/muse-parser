package oqube.muse.events;

import oqube.muse.MuseSink;

public class FlowEvent implements SinkEvent {

  private String tag;

  private String[][] currentArgs;

  private String content;

  public FlowEvent(String tag, String[][] currentArgs, String content) {
    this.tag = tag;
    this.currentArgs = currentArgs;
    this.content = content;
  }

  public void passTo(MuseSink sink) {
    sink.flow(tag, currentArgs, content);
  }

}
