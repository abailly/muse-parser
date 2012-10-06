package oqube.muse.web;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

import org.junit.runner.RunWith;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

@RunWith(JDaveRunner.class)
public class MuseHTMLServerSpec extends Specification<MuseHTMLServer> {

  private TemporaryFS fs;

  public void create() {
    super.create();
    fs = new TemporaryFS(new File(
        new File(System.getProperty("java.io.tmpdir")), "tests"));
    try {
      fs.copy("test1/toto/tutu/tata.muse");
      fs.copy("test1/toto/tutu/index.muse");
      fs.copy("test1/toto/tutu/genuine.html");
      fs.copy("test1/toto/titi/titi.muse");
      fs.copy("test1/toto/titi/toto.muse");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public class CheckServingMultipleDirectories {
    MuseHTMLServer server;

    public MuseHTMLServer create() throws IOException {
      server = new MuseHTMLServer();
      server.setRootDirectory(new File(fs.root(), "test1/toto/tutu"));
      server.init();
      return server;
    }

    public void serverPublishesFilesFromBothDirectories() {
      RequestConfig conf = new RequestConfig();
      conf.path = "/tata.muse";
      specify(server.serve(conf), should.equal(Boolean.TRUE));
      specify(conf.data, should.not().equal(null));
      specify(conf.type, should.equal("text/plain"));
    }

    // public void serverCachesPages() {
    // RequestConfig conf = new RequestConfig();
    // conf.path = "/tata";
    // specify(server.serve(conf), should.equal(Boolean.TRUE));
    // server.serve(conf);
    // specify(conf.status, should.equal(HttpURLConnection.HTTP_NOT_MODIFIED));
    // }
  }

  public void destroy() {
    fs.clean();
  }

}
