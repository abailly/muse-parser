package oqube.muse.html;

import junit.framework.TestCase;
import oqube.muse.StubSink;

public class DefaultHTMLLinkerTest extends TestCase {

  public void testLinkToPngProducesImgTag() {
    String link = "toto.png";
    String text = "toto";
    DefaultHTMLLinker linker = new DefaultHTMLLinker();
    StubSink ss = new StubSink("<img src=\"toto.png\" alt=\"toto\" />");
    linker.link(ss, link, text);
    ss.check();
  }

  public void testLinkToRelativePngProducesImgTag() {
    String link = "../../toto.png";
    String text = "toto";
    DefaultHTMLLinker linker = new DefaultHTMLLinker();
    final StubSink sk = new StubSink(
        "<img src=\"../../toto.png\" alt=\"toto\" />");
    linker.link(sk, link, text);
    sk.check();
  }

  public void testLinkPngURIProducesImgTag() {
    String link = "http://somehost/toto.png";
    String text = "toto";
    DefaultHTMLLinker linker = new DefaultHTMLLinker();
    final StubSink sk = new StubSink(
        "<img src=\"http://somehost/toto.png\" alt=\"toto\" />");
    linker.link(sk, link, text);
    sk.check();
  }

  public void testRelativeLinkPngURIProducesImgTag() {
    String link = "somehost/toto.png";
    String text = "toto";
    DefaultHTMLLinker linker = new DefaultHTMLLinker();
    final StubSink sk = new StubSink(
        "<img src=\"somehost/toto.png\" alt=\"toto\" />");
    linker.link(sk, link, text);
    sk.check();
  }

  public void testLinkSvgURIProducesImgTag() {
    String link = "somehost/toto.svg";
    String text = "toto";
    DefaultHTMLLinker linker = new DefaultHTMLLinker();
    final StubSink sk = new StubSink(
        "<img src=\"somehost/toto.svg\" alt=\"toto\" />");
    linker.link(sk, link, text);
    sk.check();
  }

  public void testLinkToMuseFileProducesAnchorTagToHtmlFile() {
    String link = "toto.muse";
    String text = "toto";
    DefaultHTMLLinker linker = new DefaultHTMLLinker();
    final StubSink sk = new StubSink("<a href=\"toto.html\" >toto</a>");
    linker.link(sk, link, text);
    sk.check();
  }

  public void testLinkToRelativeMuseFileProducesAnchorTagToHtmlFile() {
    String link = "./toto.muse";
    String text = "toto";
    DefaultHTMLLinker linker = new DefaultHTMLLinker();
    final StubSink sk = new StubSink("<a href=\"./toto.html\" >toto</a>");
    linker.link(sk, link, text);
    sk.check();
  }

  public void testLinkToMuseURIProducesAnchorTagToURI() {
    String link = "http://localhost/toto.muse";
    String text = "toto";
    DefaultHTMLLinker linker = new DefaultHTMLLinker();
    final StubSink sk = new StubSink(
        "<a href=\"http://localhost/toto.muse\" >toto</a>");
    linker.link(sk, link, text);
    sk.check();
  }

  public void testLinkToFragmentInMuseFile() {
    String link = "toto.muse#titi";
    String text = "toto";
    DefaultHTMLLinker linker = new DefaultHTMLLinker();
    final StubSink sk = new StubSink(
        "<a href=\"toto.html#titi\">toto</a>");
    linker.link(sk, link, text);
    sk.check();    
  }

  public void testLinkToLonelyFragmentInHTMLFile() {
    String link = "#titi";
    String text = "toto";
    DefaultHTMLLinker linker = new DefaultHTMLLinker();
    final StubSink sk = new StubSink(
        "<a href=\"#titi\">toto</a>");
    linker.link(sk, link, text);
    sk.check();    
  }
}
