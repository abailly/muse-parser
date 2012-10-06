package oqube.muse.web;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.naming.ConfigurationException;

import org.jmock.Expectations;
import org.jmock.Mockery;

import junit.framework.TestCase;

abstract class DummyMapper extends PathMapper {

  public DummyMapper(PathMapper next) {
    super(next);
  }

  public DummyMapper() {
    super(null);
  }

}

class Dummy2Mapper extends PathMapper {

  public Dummy2Mapper(PathMapper next) {
    super(next);
  }

}

public class ServerConfigTest extends TestCase {

  Mockery context = new Mockery();

  public void testConfigureMappersChainFromListOfNames()
      throws ServerConfigurationException {
    ServerConfig config = new ServerConfig();
    PathMapper pm = config.addMappers("muse", "plannerPage", "implicit",
                              "html", "image", "text")
                          .getMapper();
    assertEquals("bad root class", MuseMapper.class, pm.getClass());
    assertEquals("bad nested class", PlannerPageMapper.class, pm.getNext()
                                                                .getClass());
    assertEquals("bad nested class", ImplicitMapper.class, pm.getNext()
                                                             .getNext()
                                                             .getClass());
    assertEquals("bad nested class", HtmlMapper.class, pm.getNext()
                                                         .getNext()
                                                         .getNext()
                                                         .getClass());
    assertEquals("bad nested class", ImageMapper.class, pm.getNext()
                                                          .getNext()
                                                          .getNext()
                                                          .getNext()
                                                          .getClass());
    assertEquals("bad nested class", TextMapper.class, pm.getNext()
                                                         .getNext()
                                                         .getNext()
                                                         .getNext()
                                                         .getNext()
                                                         .getClass());
  }

  public void testConfigureMappersChainFromSuccessiveCalls()
      throws ServerConfigurationException {
    ServerConfig config = new ServerConfig();
    PathMapper pm = config.addMappers("muse", "plannerPage")
                          .addMappers("implicit", "html", "image", "text")
                          .getMapper();
    assertEquals("bad root class", MuseMapper.class, pm.getClass());
    assertEquals("bad nested class", PlannerPageMapper.class, pm.getNext()
                                                                .getClass());
    assertEquals("bad nested class", ImplicitMapper.class, pm.getNext()
                                                             .getNext()
                                                             .getClass());
    assertEquals("bad nested class", HtmlMapper.class, pm.getNext()
                                                         .getNext()
                                                         .getNext()
                                                         .getClass());
    assertEquals("bad nested class", ImageMapper.class, pm.getNext()
                                                          .getNext()
                                                          .getNext()
                                                          .getNext()
                                                          .getClass());
    assertEquals("bad nested class", TextMapper.class, pm.getNext()
                                                         .getNext()
                                                         .getNext()
                                                         .getNext()
                                                         .getNext()
                                                         .getClass());
  }

  public void testThrowsExceptionIfUnknownClassInListOfNames() {
    ServerConfig config = new ServerConfig();
    try {
      PathMapper pm = config.addMappers("muse", "plannerPage", "toto", "html",
                                "image", "text")
                            .getMapper();
      fail("Expected exception from unknown class");
    } catch (ServerConfigurationException e) {
      assertEquals("Nested exception is incorrect",
          ClassNotFoundException.class, e.getCause()
                                         .getClass());
    }
  }

  public void testThrowsExceptionIfCannotInstantiateMapper() {
    ServerConfig config = new ServerConfig();
    try {
      PathMapper pm = config.addMappers("dummy")
                            .getMapper();
      fail("Expected ServerConfigurationException from uninstantiable class");
    } catch (ServerConfigurationException e) {
      assertEquals("Nested exception is incorrect",
          InstantiationException.class, e.getCause()
                                         .getClass());
    }
  }

  public void testThrowsExceptionIfCannotFindEmptyCtor() {
    ServerConfig config = new ServerConfig();
    try {
      PathMapper pm = config.addMappers("dummy2")
                            .getMapper();
      fail("Expected ServerConfigurationException from uninstantiable class");
    } catch (ServerConfigurationException e) {
      assertEquals("Nested exception is incorrect",
          NoSuchMethodException.class, e.getCause()
                                        .getClass());
    }
  }

  public void testConfigureDirectoryToServe()
      throws ServerConfigurationException {
    ServerConfig config = new ServerConfig();
    final String tmpdir = System.getProperty("java.io.tmpdir");
    File root = new File(tmpdir);
    File f = config.setRootDirectory(tmpdir)
                   .getRootDirectory();
    assertEquals("Incorrect root directory", root, f);
  }

  public void testThrowsExceptionWhenDirectoryToServeDoesNotExist() {
    ServerConfig config = new ServerConfig();
    try {
      File f = config.setRootDirectory("toto_12")
                     .getRootDirectory();
      fail("Expected configuration exception");
    } catch (ServerConfigurationException e) {
      //
    }
  }

  public void testCanEraseConfiguration() throws ServerConfigurationException {
    ServerConfig config = new ServerConfig();
    final String tmpdir = System.getProperty("java.io.tmpdir");
    config.setRootDirectory(tmpdir)
          .addMappers("muse");
    config.erase();
    assertNull("Mapper should be null", config.getMapper());
    assertNull("Directory should be null", config.getRootDirectory());
  }

  public void testConfigureHeaderFromString() {
    ServerConfig config = new ServerConfig();
    config.setHeader("header");
    assertEquals("Bad header", new Header(null, "header"), config.getHeader());
  }

  public void testConfigureFooterFromString() {
    ServerConfig config = new ServerConfig();
    config.setFooter("header");
    assertEquals("Bad footer", new Header(null, "header"), config.getFooter());
  }

  public void testConfigureHeaderFromFile() {
    ServerConfig config = new ServerConfig();
    config.setHeaderFromFile("toto.hdr");
    assertEquals("Bad header", new Header("toto.hdr", ""), config.getHeader());

  }

  public void testConfigureFooterFromFile() {
    ServerConfig config = new ServerConfig();
    config.setFooterFromFile("toto.hdr");
    assertEquals("Bad header", new Header("toto.hdr", ""), config.getFooter());

  }

  public void testConfigureInputEncoding() {
    ServerConfig config = new ServerConfig();
    config.setInputEncoding("ISO-8859-1");
    assertEquals("Bad header", "ISO-8859-1", config.getInputEncoding());
  }

  public void testConfigureOutputEncoding() {
    ServerConfig config = new ServerConfig();
    config.setOutputEncoding("ISO-8859-1");
    assertEquals("Bad header", "ISO-8859-1", config.getOutputEncoding());
  }

  public void testCanConfigureAServer() throws ServerConfigurationException {
    final MuseServer server = context.mock(MuseServer.class);
    final String tmpdir = System.getProperty("java.io.tmpdir");
    final ServerConfig config = new ServerConfig();
    config.addMappers("muse")
          .setFooterFromFile("toto.hdr")
          .setHeaderFromFile("toto.hdr")
          .setInputEncoding("ISO-8859-1")
          .setOutputEncoding("UTF-8")
          .setRootDirectory(tmpdir);
    context.checking(new Expectations() {
      {
        one(server).setInputEncoding("ISO-8859-1");
        one(server).setOutputEncoding("UTF-8");
        one(server).setPath("");
        one(server).setHeader(new Header("toto.hdr", ""));
        one(server).setFooter(new Header("toto.hdr", ""));
        one(server).setRootDirectory(new File(tmpdir));
        one(server).setMapper(config.getMapper());
      }
    });
    config.configure(server);
    context.assertIsSatisfied();
  }

  public void testCanConfigureAServerWithDefaultValues()
      throws ServerConfigurationException {
    final MuseServer server = context.mock(MuseServer.class);
    final String enc = System.getProperty("file.encoding");
    final ServerConfig config = new ServerConfig();
    context.checking(new Expectations() {
      {
        one(server).setInputEncoding(enc);
        one(server).setPath("");
        one(server).setOutputEncoding(enc);
        one(server).setHeader(new Header("", ""));
        one(server).setFooter(new Header("", ""));
        one(server).setRootDirectory(new File("."));
        one(server).setMapper(config.getMapper());
      }
    });
    config.configure(server);
    context.assertIsSatisfied();
  }
}
