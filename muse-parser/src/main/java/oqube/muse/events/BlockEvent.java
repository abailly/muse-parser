package oqube.muse.events;

import oqube.muse.MuseSink;

public class BlockEvent implements SinkEvent {

  private String tag;
  private String[][] currentArgs;
  private String content;

  public BlockEvent(String tag, String[][] currentArgs, String content) {
    this.tag = tag;
    this.currentArgs = currentArgs;
    this.content = content;
  }

  public void passTo(MuseSink sink) {
    sink.block(tag, currentArgs, content);
  }

}
