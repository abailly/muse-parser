package oqube.muse.muse;

import java.io.ByteArrayOutputStream;

import junit.framework.TestCase;
import oqube.muse.html.MuseAnchorLinker;
import oqube.muse.parser.MuseParser;

public class MuseMuseSinkTest extends TestCase {

  private MuseMuseSink sink;

  private MuseParser parser;

  private ByteArrayOutputStream bos;

  protected void setUp() throws Exception {
    super.setUp();
    sink = new MuseMuseSink();
    sink.setLinker(new MuseAnchorLinker());
    sink.setTagHandler(new MuseSourceTag());
    sink.setOut(bos = new ByteArrayOutputStream());
    parser = new MuseParser();
    parser.setSink(sink);
  }

  public void test01Copy() {
    parser.setStream(getClass().getResourceAsStream("/testSourceTag1.muse"));
    sink.start("document", null);
    parser.start();
    sink.end("document");
    sink.getOut().flush();
    // check content of output stream
    String out = new String(bos.toByteArray());
    System.err.println(out);
    assertTrue(out.contains("#author abailly@oqube.com"));
    assertTrue(out.contains("** Methods"));
    assertTrue(out.contains(" for (int m = 0; m < l; m++) {"));
  }

}
