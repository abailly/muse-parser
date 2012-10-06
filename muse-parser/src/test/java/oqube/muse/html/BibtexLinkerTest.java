package oqube.muse.html;

import oqube.muse.MuseSink;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public class BibtexLinkerTest extends MockObjectTestCase {

  private BibtexLinker linker;
  private Mock mock;

  /* (non-Javadoc)
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
    this.linker = new BibtexLinker();
    this.mock = mock(MuseSink.class);
  }

  public void test01() {
    String lnk = "bibtex:src/test/resources/test.bib:heerink-miots-test";
    // configure mock
    mock.expects(once()).method("link").with(eq("#heerink-miots-test"),eq("[1]"));
    linker.link((MuseSink)mock.proxy(),lnk,"");
  }

  public void testLinkerDelegatesNullLinks() {
    String lnk = null;
    // configure mock
    linker.link((MuseSink)mock.proxy(),lnk,"");
  }
  
}
