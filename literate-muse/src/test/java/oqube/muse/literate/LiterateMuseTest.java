package oqube.muse.literate;

import java.io.File;
import java.util.ArrayList;

import fr.lifl.utils.TemporaryFS;
import junit.framework.TestCase;

public class LiterateMuseTest extends TestCase {

  private TemporaryFS tmpfs;

  private LiterateMuse literate;

  private File docdir;

  private File srcdir;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    tmpfs = new TemporaryFS(new File(new File(System
        .getProperty("java.io.tmpdir")), "literate-muse"));
    literate = new LiterateMuse();
    literate.forcePublishing();
    literate.setFormat("xhtml");
    literate.setHeader(getClass().getResource("/header.txt").getFile());
    literate.setFooter(getClass().getResource("/footer.txt").getFile());
    docdir = new File(tmpfs.root(), "doc");
    literate.setDocOutputDirectory(docdir);
    srcdir = new File(tmpfs.root(), "src");
    literate.setSourceOutputDirectory(srcdir);
    literate.setFiles(new ArrayList<File>() {
      {
        add(new File(getClass().getResource("/webdriver.muse").getFile()));
      }
    });
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    tmpfs.clean();
  }

  public void testHeaderAndFooterAreSet() throws Exception {
    literate.run();
    final File generatedFile = new File(docdir, "webdriver.html");
    assertTrue("no doc output", generatedFile.exists());
    final String docContent = literate.read(generatedFile.getAbsolutePath());
    System.err.println(docContent);
    assertTrue("bad header", docContent.contains("this is a header"));
    assertTrue("bad code content", docContent.contains("arbitrary"));
  }

  public void testLiterateMuseInvokesPostProcessorOnSources() throws Exception {
    String command = buildCommand();
    literate.setPostProcessor(command);
    literate.run();
    final File generatedSource = new File(srcdir, "Test.hs");
    assertTrue("no source output", generatedSource.exists());
    final String docContent = literate.read(generatedSource.getAbsolutePath());
    System.err.println(docContent);
 //   assertTrue("bad header", docContent.contains("(2, liftM Sove orbitrory)"));
  }

  private String buildCommand() {
    StringBuilder builder = new StringBuilder();
    builder.append("java"); // hope java is on the path !!
    builder.append(" -cp ").append(
        new File(getBasedir()).getAbsolutePath() + "/target/test-classes/");
    builder.append(" oqube.muse.literate.AChanger");
    return builder.toString();
  }

  private String getBasedir() {
    return System.getProperty("basedir", ".");
  }
}
