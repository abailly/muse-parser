package oqube.muse.web;

import static oqube.muse.events.EventSequenceMatcher.startBelowLevel;
import static oqube.muse.events.EventSequenceMatcher.startWithElementAtLevel;
import static oqube.muse.events.EventSequenceMatcher.textEquals;

import java.io.InputStream;
import java.io.InputStreamReader;

import oqube.muse.MuseSink;
import oqube.muse.events.EventSequenceMatcher;
import oqube.muse.filter.FilterSink;
import oqube.muse.filter.OrFilter;
import oqube.muse.filter.SectionFilter;
import oqube.muse.filter.TagFilter;
import oqube.muse.parser.MuseParser;

public class FeedEntrySelector {

  private FilterSink sink;

  private InputStream input;

  public FeedEntrySelector(String date, String title) {
    this.sink = new FilterSink();
    sink.filterWith(new OrFilter(new TagFilter("document"), new TagFilter(
        "header"), new TagFilter("footer"), new TagFilter("body"),
        new SectionFilter(1, startMatcher(date, title))));
  }

  private EventSequenceMatcher startMatcher(String date, String title) {
    return new EventSequenceMatcher(startWithElementAtLevel("title1", 1),
        new TextLinkMatcher(date, title));
  }

  public void setSink(MuseSink targetSink) {
    this.sink.setSink(targetSink);
  }

  public void setInput(InputStream resourceAsStream) {
    this.input = resourceAsStream;
  }

  public void select() {
    MuseParser parser = new MuseParser();
    parser.setReader(new InputStreamReader(this.input));
    parser.setSink(sink);
    parser.start();
  }

  public FilterSink getSink() {
    return sink;
  }

  public void setSink(FilterSink sink) {
    this.sink = sink;
  }

}
