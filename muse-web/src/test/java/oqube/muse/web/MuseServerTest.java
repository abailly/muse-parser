package oqube.muse.web;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

public class MuseServerTest extends TestCase {
  private TemporaryFS fs;

  private File file;

  protected void setUp() throws Exception {
    super.setUp();
    fs = new TemporaryFS(new File(
        new File(System.getProperty("java.io.tmpdir")), "tests"));
    fs.copy("test1/toto/tutu/tata.muse");
    fs.copy("test1/toto/tutu/index.muse");
    fs.copy("test1/toto/titi/titi.muse");
    fs.copy("test1/toto/titi/toto.muse");
  }

  public void testServerHandlesMultipleDirectories() throws IOException {
    MuseHTMLServer server = new MuseHTMLServer();
    server.setRootDirectory(new File(fs.root(), "test1/toto/tutu"));
    server.init();
    RequestConfig conf = new RequestConfig();
    conf.path = "/tata.muse";
    server.serve(conf);
    assertRequestSucceed("Incorrect reply for request to /tata/tata", conf);
  }

  private void assertRequestSucceed(String string, RequestConfig conf) {
    assertNotNull(conf.data);
    assertEquals("text/plain", conf.type);
  }

  protected void tearDown() throws Exception {
    super.tearDown();
    fs.clean();
  }

}
