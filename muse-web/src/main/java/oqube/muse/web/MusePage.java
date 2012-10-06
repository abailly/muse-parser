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

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.File;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Date;

import oqube.muse.html.HTMLPublisher;

/**
 * Produces an xhtml page from a muse page.
 * 
 * @author abailly@oqube.com
 * @version $Id$
 */
public class MusePage implements Page {

  private HTMLPublisher pub;

  private File file;

  protected int length;

  public MusePage(File f) {
    this.file = f;
  }

  public MusePage(File f, HTMLPublisher publisher) {
    // check argument
    if (!f.exists())
      throw new IllegalArgumentException("File " + f
          + " does not exist in this mapper's namespace");
    this.file = f;
    this.pub = publisher;
  }

  public HTMLPublisher getPub() {
    return pub;
  }

  public void setPub(HTMLPublisher pub) {
    this.pub = pub;
  }

  /**
   * Transforms the muse content into xhtml using muse publisher.
   * 
   * @return a valid xhtml document. It may be an error page if content cannot
   *         be properly produced.
   */
  public InputStream content(String sessionId) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    try {
      Reader br = new InputStreamReader(new FileInputStream(file),
          pub.getInputEncoding());
      assert br != null;
      assert pw != null;
      assert pub != null;
      pub.process(sessionId, br, pw);
      br.close();
      pw.close();
      byte[] bytes = sw.toString()
                       .getBytes(pub.getOutputEncoding());
      this.length = bytes.length;
      // Extract stream
      return new ByteArrayInputStream(bytes);
    } catch (IOException e) {
      return new ByteArrayInputStream(("Error reading page " + file.getPath()
          + ": " + e.getMessage()).getBytes());
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || !(o instanceof MusePage))
      return false;
    MusePage p = (MusePage) o;
    return p.file.equals(file);
  }

  public File getFile() {
    return file;
  }

  public String contentType() {
    return "text/html";
  }

  public int length() {
    return length;
  }
}
