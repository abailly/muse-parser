package oqube.muse.html;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import junit.framework.TestCase;
import oqube.muse.DefaultSink;
import oqube.muse.MuseSink;

public class BibtexHTMLFormatterTest extends TestCase {

  private ByteArrayOutputStream bos;

  private PrintWriter pw;

  private BibtexLinker linker;

  private BibtexHTMLFormatter frm;

  protected void setUp() throws Exception {
    super.setUp();
    frm = new BibtexHTMLFormatter();
    bos = new ByteArrayOutputStream();
    pw = new PrintWriter(new OutputStreamWriter(bos));
    // load linker and some entries
    MuseSink sk = new DefaultSink();
    linker = new BibtexLinker();
    String lnk = "bibtex:src/test/resources/test.bib:heerink-miots-test";
    linker.link(sk, lnk, "");
    lnk = "bibtex:src/test/resources/test.bib:gaudel";
    linker.link(sk, lnk, "");
    lnk = "bibtex:src/test/resources/test.bib:gupta-gentestdata";
    linker.link(sk, lnk, "");
    lnk = "bibtex:src/test/resources/test.bib:hierons-check-synch";
    linker.link(sk, lnk, "");
  }

  public void test01() {
    frm.format(pw,linker.getEntries());
//  check output
    pw.flush();
    pw.close();
    String s = new String(bos.toByteArray());
    System.err.println(s);
    assertTrue(s.contains("<span class=\"title\">Software Testing based on Formal Specifications : a theory and a tool</span>"));
    assertTrue(s.contains("<span class=\"label\">[2]</span>"));
    // check list formats
    assertTrue(s.contains("Gilles Bernot, Marie-Claude Gaudel et Bruno Marre"));
    assertTrue(s.contains("Robert M. Hierons et H. Ural"));
  }

}
