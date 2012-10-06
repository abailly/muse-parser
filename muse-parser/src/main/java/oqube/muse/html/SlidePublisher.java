package oqube.muse.html;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import oqube.muse.DefaultSink;
import oqube.muse.MuseSink;
import oqube.muse.MuseTagHandler;
import oqube.muse.template.TemplateFactory;

import org.apache.commons.logging.Log;

public class SlidePublisher extends HTMLPublisher {

  private HTMLSlidesSink slides;

  private HTMLPublisher htmlPublisher;

  public SlidePublisher(HTMLPublisher publisher) {
    this.htmlPublisher = publisher;
  }

  public SlidePublisher() {
    this.htmlPublisher = new HTMLPublisher();
  }

  @Override
  public void startSession(String id) {
    htmlPublisher.startSession(id);
    this.slides = new HTMLSlidesSink();
    final DefaultSink htmlSink = (DefaultSink) htmlPublisher.getSink(id);
    MuseTagHandler tagHandler = htmlSink.getTagHandler();
    htmlSink.setTagHandler(new SlideSourceTagHandler(tagHandler));
    this.slides.setSink(htmlSink);
    htmlPublisher.setSink(id, this.slides);
  }

  public void endSession(String id) {
    htmlPublisher.endSession(id);
  }

  public boolean equals(Object obj) {
    return htmlPublisher.equals(obj);
  }

  public String getExtension() {
    return htmlPublisher.getExtension();
  }

  public String getInputEncoding() {
    return htmlPublisher.getInputEncoding();
  }

  public String getOutputEncoding() {
    return htmlPublisher.getOutputEncoding();
  }

  public MuseSink getSink(String id) {
    return htmlPublisher.getSink(id);
  }

  public String getTargetName(File f) {
    return htmlPublisher.getTargetName(f);
  }

  public TemplateFactory getTemplates() {
    return htmlPublisher.getTemplates();
  }

  public int hashCode() {
    return htmlPublisher.hashCode();
  }

  public void process(String session, Reader br, PrintWriter pw)
      throws IOException {
    htmlPublisher.process(session, br, pw);
  }

  public void setFooter(String string) {
    htmlPublisher.setFooter(string);
  }

  public void setHeader(String string) {
    htmlPublisher.setHeader(string);
  }

  public void setInputEncoding(String inputEncoding) {
    htmlPublisher.setInputEncoding(inputEncoding);
  }

  public void setLog(Log log) {
    htmlPublisher.setLog(log);
  }

  public void setOutputEncoding(String outputEncoding) {
    htmlPublisher.setOutputEncoding(outputEncoding);
  }

  public void setSink(String id, MuseSink sink) {
    htmlPublisher.setSink(id, sink);
  }

  public void setTemplates(TemplateFactory templates) {
    htmlPublisher.setTemplates(templates);
  }

  public String toString() {
    return htmlPublisher.toString();
  }

}
