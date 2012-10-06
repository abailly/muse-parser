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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;

import fr.lifl.utils.Pipe;

/**
 * A page for outputting an image in Base64 encoding.
 * 
 * @author nono
 * 
 */
public class ImagePage implements Page {

  private File file;

  private String type;

  private int length;

  public ImagePage(String type, File file) {
    // check argument
    if(!file.exists())
      throw new IllegalArgumentException("File "+ file+" does not exist in this mapper's namespace");
    this.type = type;
    this.file = file;
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.web.Page#content(java.lang.String)
   */
  public InputStream content(String sessionId) {
    try {
      FileInputStream fis = new FileInputStream(file);
      this.length = (int) file.length();
      return fis;
    } catch (IOException e) {
      return new ByteArrayInputStream(e.getMessage().getBytes());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.web.Page#contentType()
   */
  public String contentType() {
    return type;
  }

  public int length() {
    return length;
  }

}
