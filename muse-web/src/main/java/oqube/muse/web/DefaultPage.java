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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.StringReader;

/**
 * Just returns the content of a file if it exists or an error page.
 * 
 * @author abailly@oqube.com
 * @version $Id$
 */
public class DefaultPage implements Page {

  private File file;

  private String type = "text/plain";

  private long length;

  public DefaultPage(File f) {
    // check argument
    if(!f.exists())
      throw new IllegalArgumentException("File "+ file+" does not exist in this mapper's namespace");
    this.file = f;
  }

  public DefaultPage(String type, File f) {
    this(f);
    this.type = type;
  }

  public InputStream content(String id) {
    try {
      this.length  = file.length();
      return new FileInputStream(file);
    } catch (FileNotFoundException e) {
      return new ByteArrayInputStream(e.getMessage().getBytes());
    }
  }

  public Object getFile() {
    return file;
  }

  public String contentType() {
    return type;
  }

  public int length() {
    return (int) length;
  }

}
