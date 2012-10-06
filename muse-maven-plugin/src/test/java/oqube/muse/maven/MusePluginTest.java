package oqube.muse.maven;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;

public class MusePluginTest extends AbstractMojoTestCase {

  private File f;

  protected void setUp() throws Exception {
    super.setUp();
  }

  /*
   * (non-Javadoc)
   * 
   * @see junit.framework.TestCase#tearDown()
   */
  protected void tearDown() throws Exception {
    /* delete f */
    super.tearDown();
  }

  /*
   */
  public void test01Base() throws Exception {
    File testPom = new File(getBasedir(),
        "/target/test-classes/testpoms/test01.xml");
    LiterateMuseMojo mojo = (LiterateMuseMojo) lookupMojo("publish",
        testPom);
    // check config
    MavenProjectStub proj = new MavenProjectStub();
    mojo.setProject(proj);
    mojo.execute();
    assertTrue(new File(getBasedir(),
    "/target/generated-sources/lit/toto/Toto.java").exists());
    assertTrue(new File(getBasedir(),
    "/target/site/literate.html").exists());
  }

}
