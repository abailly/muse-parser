package oqube.muse.feed;

public class TextAccumulator {

  private StringBuffer accumulator = new StringBuffer();

  public void accumulate(String txt) {
    accumulator.append(txt);
  }

  public String content() {
    return accumulator.toString();
  }

  public void reset() {
    this.accumulator = new StringBuffer();
  }

}
