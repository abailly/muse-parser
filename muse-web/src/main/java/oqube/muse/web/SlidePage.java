package oqube.muse.web;

import java.io.File;
import java.io.InputStream;

import oqube.muse.MuseSink;
import oqube.muse.html.HTMLPublisher;
import oqube.muse.html.HTMLSlidesSink;

public class SlidePage extends MusePage {

  private String date;

  private String title;

  public SlidePage(File f, HTMLPublisher publisher) {
    super(f, publisher);
    this.date = date;
    this.title = title;
  }

  @Override
  public InputStream content(String sessionId) {
    MuseSink oldSink = getPub().getSink(sessionId);
    HTMLSlidesSink newSink = new HTMLSlidesSink();
    newSink.setSink(oldSink);
    getPub().setSink(sessionId, newSink);
    InputStream content = super.content(sessionId);
    getPub().setSink(sessionId, oldSink);
    return content;
  }

}
