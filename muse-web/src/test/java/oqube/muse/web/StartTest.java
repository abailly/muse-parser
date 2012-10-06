package oqube.muse.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StartTest {

  private TemporaryFS fs;

  private static final String EOL = System.getProperty("line.separator");

  @Before
  public void setUp() throws Exception {
    fs = new TemporaryFS(new File(
        new File(System.getProperty("java.io.tmpdir")), "tests"));
    fs.copy("test1/toto/tutu/tata.muse");
    fs.copy("test1/toto/tutu/index.muse");
    fs.copy("test1/toto/titi/titi.muse");
    fs.copy("test1/toto/titi/toto.muse");
  }

  @Test
  public void startServerWithSimpleconfigFromStdin() throws IOException {
    String config = "new config 'tutu map to '/titi/*" + EOL
        + "set root directory '" + new File(fs.root(), "test1/toto/tutu/")
        + EOL + "set header \"one header\"" + EOL
        + "add mappers 'muse 'implicit" + EOL + EOL
        + "new config 'titi map to '/tutu/*" + EOL + "set root directory '"
        + new File(fs.root(), "test1/toto/titi/") + EOL
        + "set header \"two header\"" + EOL + "add mappers 'muse 'html";
    System.err.println(config);
    InputStream oldstdin = System.in;
    System.setIn(new ByteArrayInputStream(config.getBytes()));
    Start.main(new String[] {});
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.setIn(oldstdin);
    // do some requests
    // try connecting to server
    URL url = new URL("http://localhost:4444/titi/tata");
    URLConnection cnx = url.openConnection();
    cnx.connect();
    String res = answer(cnx);
    org.junit.Assert.assertEquals("bad response code :", 200,
        ((HttpURLConnection) cnx).getResponseCode());
    Assert.assertTrue("unexpected content", res
        .contains("<strong>technologies ouvertes</strong>"));
    url = new URL("http://localhost:4444/tutu/titi.muse");
    cnx = url.openConnection();
    cnx.connect();
    res = answer(cnx);
    org.junit.Assert.assertEquals("bad response code :", 200,
        ((HttpURLConnection) cnx).getResponseCode());
    Assert.assertTrue("unexpected content: "+res, res
        .contains("<strong>technologies ouvertes</strong>"));
  }

  private String answer(URLConnection cnx) throws IOException {
    InputStream is = cnx.getInputStream();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    byte[] buf = new byte[1024];
    int ln = 0;
    while ((ln = is.read(buf, 0, 1024)) >= 0) {
      bos.write(buf, 0, ln);
    }
    String res = bos.toString();
    return res;
  }

}