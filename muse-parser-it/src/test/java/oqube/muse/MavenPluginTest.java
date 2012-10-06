package oqube.muse;

import java.io.File;
import java.util.Collections;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import oqube.muse.literate.LiterateMuse;

import org.apache.maven.BuildFailureException;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.cli.ConsoleDownloadMonitor;
import org.apache.maven.embedder.MavenEmbedder;
import org.apache.maven.embedder.MavenEmbedderConsoleLogger;
import org.apache.maven.embedder.PlexusLoggerAdapter;
import org.apache.maven.lifecycle.LifecycleExecutionException;
import org.apache.maven.monitor.event.DefaultEventMonitor;
import org.apache.maven.monitor.event.EventMonitor;
import org.apache.maven.project.DuplicateProjectException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingException;
import org.codehaus.plexus.util.dag.CycleDetectedException;

/**
 * Integration testing class for literate muse plugin. This class run plugin
 * with projects using maven embedder and chekcs generation is ok.
 * 
 * @author nono
 * 
 */
public class MavenPluginTest extends TestCase {

  private MavenEmbedder maven;

  protected void setUp() throws Exception {
    super.setUp();
    maven = new MavenEmbedder();
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    maven.setClassLoader(classLoader);
    maven.setLogger(new MavenEmbedderConsoleLogger());
    maven.start();
  }

  public void test01() throws CycleDetectedException,
      LifecycleExecutionException, BuildFailureException,
      DuplicateProjectException, ArtifactResolutionException,
      ArtifactNotFoundException, ProjectBuildingException {
    File targetDirectory = new File("target/test-classes/testPlugin01");
    File pomFile = new File(targetDirectory, "pom.xml");
    MavenProject pom = maven.readProjectWithDependencies(pomFile);
    EventMonitor eventMonitor = new DefaultEventMonitor(
        new PlexusLoggerAdapter(new MavenEmbedderConsoleLogger()));
    maven.execute(pom, Collections.singletonList("muse:publish"), eventMonitor,
        new ConsoleDownloadMonitor(), null, targetDirectory);
    // check generated data
    File gen = new File(targetDirectory,
        "target/generated-sources/literate/toto/Toto.java");
    assertTrue(gen.exists() && gen.length() > 0);
  }

  public void testLiterateWithTestsAndSubdir() throws CycleDetectedException,
      LifecycleExecutionException, BuildFailureException,
      DuplicateProjectException, ArtifactResolutionException,
      ArtifactNotFoundException, ProjectBuildingException {
    File targetDirectory = new File("target/test-classes/testPlugin02");
    File pomFile = new File(targetDirectory, "pom.xml");
    MavenProject pom = maven.readProjectWithDependencies(pomFile);
    EventMonitor eventMonitor = new DefaultEventMonitor(
        new PlexusLoggerAdapter(new MavenEmbedderConsoleLogger()));
    maven.execute(pom, Collections.singletonList("muse:publish"), eventMonitor,
        new ConsoleDownloadMonitor(), null, targetDirectory);
    // check generated data
    File gen = new File(targetDirectory,
        "target/generated-sources/literate/oqube/tdd/Triangle1.java");
    assertTrue(gen.exists() && gen.length() > 0);
  }
}
