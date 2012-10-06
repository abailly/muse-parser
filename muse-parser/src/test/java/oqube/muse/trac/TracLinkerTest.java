package oqube.muse.trac;

import junit.framework.TestCase;
import oqube.muse.StubSink;

public class TracLinkerTest extends TestCase {

  public void testMuseLinksGenerateWikiLinks() {
    TracLinker lk = new TracLinker();
    String s = "toto.muse";
    String t = "toto";
    StubSink sink = new StubSink("[wiki:toto toto]");
    lk.link(sink, s, t);
    sink.check();
  }

  public void testMuseRelativeLinksGenerateFlatWikiLinksWithCamelCase() {
    TracLinker lk = new TracLinker();
    String s = "../titi/toto.muse";
    String t = "toto";
    StubSink sink = new StubSink("[wiki:Tititoto toto]");
    lk.link(sink, s, t);
    sink.check();
  }

  public void testImageLinksGenerateImageMacros() {
    TracLinker lk = new TracLinker();
    String s = "toto.png";
    String t = "toto";
    StubSink sink = new StubSink("[[Image(toto.png, alt=\"toto\")]]");
    lk.link(sink, s, t);
    sink.check();
  }
  
  public void testRelativeImageLinksGenerateImageMacros() {
    TracLinker lk = new TracLinker();
    String s = "images/toto.png";
    String t = "toto";
    StubSink sink = new StubSink("[[Image(images/toto.png, alt=\"toto\")]]");
    lk.link(sink, s, t);
    sink.check();
  }
  
  public void testExternalLinkGenerateRawLink() {
    TracLinker lk = new TracLinker();
    String s = "toto.html";
    String t = "toto";
    StubSink sink = new StubSink("[toto.html toto]");
    lk.link(sink, s, t);
    sink.check();    
  }
}
