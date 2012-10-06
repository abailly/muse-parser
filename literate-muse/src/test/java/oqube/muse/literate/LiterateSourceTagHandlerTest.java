package oqube.muse.literate;

import oqube.muse.MuseTagHandler;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import junit.framework.TestCase;

public class LiterateSourceTagHandlerTest extends MockObjectTestCase {

  private Mock mock;

  private LiterateSourceTagHandler th;

  private Mock mocknext;

  protected void setUp() throws Exception {
    super.setUp();
    mock = mock(SourceRepository.class);
    mocknext = mock(MuseTagHandler.class);
    th = new LiterateSourceTagHandler();
    th.setRepository((SourceRepository) mock.proxy());
    th.setNext((MuseTagHandler) mocknext.proxy());
  }

  /*
   * test name extraction and repo invocation.
   */
  public void test01Source() {
    String tag = "source";
    String[][] attrs = { { "bar", "baz" }, { "name", "toto" },
        { "dummy", "foo" } };
    String content = "content";
    mock.expects(once()).method("addFragment").with(eq("toto"), eq("content"));
    mocknext.expects(once()).method("block").with(ANYTHING, eq("source"),
        same(attrs), eq("content")).will(returnValue(true));
    th.block(null, tag, attrs, content);
  }

  /*
   * test not good tag
   */
  public void test02NotSource() {
    String tag = "notsource";
    String[][] attrs = { { "bar", "baz" }, { "name", "toto" },
        { "dummy", "foo" } };
    String content = "content";
    mocknext.expects(once()).method("block").with(ANYTHING, eq("notsource"),
        same(attrs), eq("content")).will(returnValue(true));
    th.block(null, tag, attrs, content);
  }

  /*
   * test unnamed source
   */
  public void test03UnnamedSource() {
    String tag = "source";
    String[][] attrs = { { "bar", "baz" },
        { "dummy", "foo" } };
    String content = "content";
    mocknext.expects(once()).method("block").with(ANYTHING, eq("source"),
        same(attrs), eq("content")).will(returnValue(true));
    th.block(null, tag, attrs, content);
  }
 
  /*
   * test no netx handler
   */
  public void test04NoNext() {
    String tag = "source";
    String[][] attrs = { { "bar", "baz" },{ "name", "toto" },
        { "dummy", "foo" } };
    String content = "content";
    th.setNext(null);
    mock.expects(once()).method("addFragment").with(eq("toto"), eq("content"));
    th.block(null, tag, attrs, content);
  }
 
}
