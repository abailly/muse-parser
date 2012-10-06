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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import oqube.muse.html.HTMLPublisher;

/**
 * @author nono
 * 
 */
public class MuseIndexPage extends MusePage {

  private final String EOL = System.getProperty("line.separator");

  public MuseIndexPage(File f) {
    super(f);
  }

  public MuseIndexPage(File file, HTMLPublisher publisher) {
    super(file, publisher);
  }

  @Override
  public InputStream content(String sessionId) {
    // construct content for file
    StringBuffer sb = new StringBuffer();
    final File f = getFile();
    assert f.isDirectory();
    File[] files = f.listFiles(new FileFilter() {

      public boolean accept(File arg0) {
        return arg0.getName().endsWith(".muse");
      }

    });
    for (File file : files)
      sb.append(" - [[./").append(file.getName()).append("][").append(
          file.getName()).append("]]").append(EOL);
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    try {
      // make muse style index
      Reader br = new StringReader(sb.toString());
      getPub().process(sessionId, br, pw);
      br.close();
      pw.close();
    } catch (IOException e) {
    }
    byte[] bytes = sw.toString().getBytes();
    this.length = bytes.length;
    // Extract stream
    return new ByteArrayInputStream(bytes);
  }
}
