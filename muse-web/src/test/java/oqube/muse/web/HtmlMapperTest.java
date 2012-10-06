package oqube.muse.web;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

public class HtmlMapperTest extends TestCase {

  private TemporaryFS fs;

  protected void setUp() throws Exception {
    super.setUp();
    fs = new TemporaryFS(new File(
        new File(System.getProperty("java.io.tmpdir")), "tests"));
    fs.copy("test1/toto/tutu/tata.muse");
    fs.copy("test1/toto/tutu/genuine.html");
  }

  public void testLocalHtmlReferenceReturnsMuseFileIfItExists() {
    HtmlMapper h = new HtmlMapper();
    final File dir = new File(fs.root(), "test1/");
    h.setRoot(dir);
    MusePage p = (MusePage) h.map("/toto/tutu/tata.html");
    assertNotNull(p);
    assertEquals(new File(dir, "toto/tutu/tata.muse"), p.getFile());
  }

  public void testLocalHtmlReferenceReturnsHtmlFileIfMuseFileDoesNotExist() {
    HtmlMapper h = new HtmlMapper();
    final File dir = new File(fs.root(), "test1/");
    h.setRoot(dir);
    DefaultPage p = (DefaultPage) h.map("/toto/tutu/genuine.html");
    assertNotNull(p);
    assertEquals("text/html", p.contentType());
    assertEquals(new File(dir, "toto/tutu/genuine.html"), p.getFile());
  }

  public void testLocalRelativeHtmlReferenceReturnsMuseFile()
      throws IOException {
    HtmlMapper h = new HtmlMapper();
    final File dir = new File(fs.root(), "test1/toto/tutu");
    h.setRoot(dir);
    MusePage p = (MusePage) h.map("../tutu/tata.html");
    assertNotNull(p);
    assertEquals(new File(dir, "tata.muse").getCanonicalFile(), p.getFile()
        .getCanonicalFile());
  }

}
