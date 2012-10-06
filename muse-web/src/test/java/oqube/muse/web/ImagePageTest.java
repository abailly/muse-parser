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

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.swing.ImageIcon;

import oqube.muse.html.HTMLPublisher;
import junit.framework.TestCase;

/**
 * @author nono
 * 
 */
public class ImagePageTest extends TestCase {

  private TemporaryFS fs;

  protected File file;

  /*
   * (non-Javadoc)
   * 
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
    fs = new TemporaryFS(new File(
        new File(System.getProperty("java.io.tmpdir")), "tests"));
    file = fs.copy("test1/toto/muse-powered-by.png");
  }

  public void testImageIsConvertedToBase64() throws IOException {
    ImagePage p = new ImagePage("image/png", file);
    InputStream is = p.content("one");
    assertNotNull(is);
  }

  /*
   * (non-Javadoc)
   * 
   * @see junit.framework.TestCase#tearDown()
   */
  protected void tearDown() throws Exception {
    super.tearDown();
    fs.clean();
  }

}
