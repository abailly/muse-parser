/**
 * Copyright (C) 2007 - OQube / Arnaud Bailly This library is free software; you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * Created on Fri Jul 6 2007
 */
package oqube.muse.web;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Reader;
import java.io.File;
import java.io.IOException;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

import fr.lifl.utils.Pipe;

import oqube.muse.html.HTMLPublisher;

/**
 * 
 * @author abailly@oqube.com
 * @version $Id$
 */
public class MusePageTest extends TestCase {

  private TemporaryFS fs;

  protected File file;

  protected HTMLPublisher pub;

  private String sessionId;

  protected void setUp() throws Exception {
    super.setUp();
    fs = new TemporaryFS(new File(
        new File(System.getProperty("java.io.tmpdir")), "tests"));
    file = fs.copy("test1/toto/tutu/tata.muse");
    this.pub = new HTMLPublisher();
    this.sessionId = "id";
    this.pub.startSession(this.sessionId);
  }

  public void testMusePageFromFileProduceXhtmlContent() throws IOException {
    MusePage page = new MusePage(file);
    page.setPub(pub);
    StringWriter bos = new StringWriter();
    Reader r = new InputStreamReader(page.content("id"));
    char[] buf = new char[1024];
    int ln = 0;
    while ((ln = r.read(buf)) != -1)
      bos.write(buf, 0, ln);
    // find xhtml
    String s = bos.toString();
    System.err.println(s);
    assertTrue("header not found", s.matches("(?s:.*<\\?xml.*\\?>.*)"));
    assertTrue("incorrect content", s
        .matches("(?s:.*<li>une sous liste </li>.*)"));
    // should check xhtml well-formednesse ?
  }

  public void testMusePageReadsRightEncoding() throws IOException {
    // an iso-8859-1 encoded file
    File iso = fs.copy("test1/toto/tutu/index.muse");
    this.pub = new HTMLPublisher();
    this.sessionId = "id";
    this.pub.startSession(this.sessionId);
    pub.setInputEncoding("ISO-8859-1");
    pub.setOutputEncoding("UTF-8");
    // expect platform dependent encoding
    MusePage page = new MusePage(iso);
    page.setPub(pub);
    StringWriter bos = new StringWriter();
    Reader r = new InputStreamReader(page.content("id"));
    char[] buf = new char[1024];
    int ln = 0;
    while ((ln = r.read(buf)) != -1)
      bos.write(buf, 0, ln);
    // find xhtml
    String s = bos.toString();
    System.err.println(s);
    assertTrue("unexpected characters in output", s
        .matches("(?s:.*soci\u00e9t\u00e9.*)"));
    assertTrue("unexpected character encoding in XML declaration, expected UTF-8"
        + ", got "
        + s.split("\n")[0], s.matches("(?s:.*<\\?xml.*UTF-8.*\\?>.*)"));
    // should check xhtml well-formednesse ?
  }

  protected void tearDown() throws Exception {
    super.tearDown();
    fs.clean();
  }

}
