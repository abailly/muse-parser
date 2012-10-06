/**
 * Copyright (C) 2008 - OQube / Arnaud Bailly Licensed under MIT open-source
 * license: see http://www.opensource.org/licenses/mit-license.php Created 13
 * oct. 08
 */
package oqube.muse.filter;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import oqube.muse.MuseSink;
import oqube.muse.events.AnchorEvent;
import oqube.muse.events.BlockEvent;
import oqube.muse.events.EndEvent;
import oqube.muse.events.EventSequenceMatcher;
import oqube.muse.events.FlowEvent;
import oqube.muse.events.LinkEvent;
import oqube.muse.events.MetadataEvent;
import oqube.muse.events.RawTextEvent;
import oqube.muse.events.SeparatorEvent;
import oqube.muse.events.SinkEvent;
import oqube.muse.events.StartEvent;
import oqube.muse.events.TextEvent;
import oqube.muse.template.Template;

/**
 * A wrapper class for filtering some events before submitting to another sink.
 * 
 * @author nono
 * 
 */
public class FilterSink implements MuseSink {

  private MuseSink sink;

  private SinkFilter filter;

  private static final Map<String, Integer> levels = new HashMap<String, Integer>() {
    {
      put("document", Integer.MIN_VALUE);
      put("header", -100);
      put("body", -50);
      put("footer", -100);
      put("title1", 1);
      put("title2", 2);
      put("title3", 3);
      put("title4", 4);
      put("para", 10);
      put("list", 20);
      put("enum", 20);
      put("item", 30);
    }
  };

  public void setSink(MuseSink sink) {
    this.sink = sink;
  }

  public void addMetadata(String name, String value) {
    doFilter(new MetadataEvent(name, value));
  }

  private void doFilter(SinkEvent event) {
    this.getFilter()
        .filter(event)
        .passTo(sink);
  }

  public void anchor(String anchor) {
    doFilter(new AnchorEvent(anchor));
  }

  public void link(String link, String text) {
    doFilter(new LinkEvent(link, text));
  }

  public void rawText(String text) {
    doFilter(new RawTextEvent(text));
  }

  public void separator() {
    doFilter(new SeparatorEvent());
  }

  public void block(String tag, String[][] currentArgs, String content) {
    doFilter(new BlockEvent(tag, currentArgs, content));
  }

  public void flow(String tag, String[][] currentArgs, String content) {
    doFilter(new FlowEvent(tag, currentArgs, content));
  }

  public void end(String element) {
    doFilter(new EndEvent(element, levelOf(element)));
  }

  public void start(String element, Map<String, String> parameters) {
    doFilter(new StartEvent(element, parameters, levelOf(element)));
  }

  public String getEncoding() {
    return sink.getEncoding();
  }

  public void setEncoding(String outputEncoding) {
    sink.setEncoding(outputEncoding);
  }

  public void setFooter(Template footer) {
    sink.setFooter(footer);

  }

  public void setHeader(Template header) {
    sink.setHeader(header);
  }

  public void setLineWidth(int lw) {
    sink.setLineWidth(lw);
  }

  public void setOut(PrintWriter pw) {
    sink.setOut(pw);
  }

  private int levelOf(String element) {
    Integer i = levels.get(element);
    return (i == null) ? Integer.MAX_VALUE : i;
  }

  public void text(String txt) {
    doFilter(new TextEvent(txt));
  }

  public void filterStartStop(EventSequenceMatcher startMatcher,
      EventSequenceMatcher stopMatcher) {
    this.setFilter(new StartStopFilter(startMatcher, stopMatcher));
  }

  public void filterSection(int i, String string) {
    this.setFilter(new SectionFilter(i, string));
  }

  public void filterElement(String tag) {
    this.setFilter(new ElementFilter(tag));
  }

  public void filterText(String regex) {
    this.setFilter(new RegexFilter(regex));
  }

  public FilterBuilder notFilter() {
    final NotFilter notFilter = new NotFilter();
    this.setFilter(notFilter);
    return notFilter;
  }

  public void filterWith(SinkFilter filter) {
    this.setFilter(filter);
  }

  public void setFilter(SinkFilter filter) {
    this.filter = filter;
  }

  public SinkFilter getFilter() {
    return filter;
  }

  public void flush() {
    sink.flush();
  }

}
