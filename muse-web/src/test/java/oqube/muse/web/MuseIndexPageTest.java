/*
 * Copyright (c) 2007 - OQube / Arnaud Bailly This library is free software; you
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
 * Created 7 juil. 07
 */
package oqube.muse.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

import oqube.muse.html.HTMLPublisher;
import junit.framework.TestCase;

/**
 * @author nono
 * 
 */
public class MuseIndexPageTest extends MusePageTest {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  public void testIndexPagePointsToMuseFiles() throws IOException {
    MuseIndexPage page = new MuseIndexPage(file.getParentFile());
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
    assertTrue("incorrect content: cannot find anchor", s
        .matches("(?s:.*<a href=\"./tata.html\" >tata.muse</a>.*)"));
    // should check xhtml well-formednesse ?
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }

}
