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
 * Created on Thu Jul 5 2007
 */
package oqube.muse.web;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import junit.framework.AssertionFailedError;

/**
 * 
 * @author abailly@oqube.com
 * @version $Id$
 */
public class MapperTest extends TestCase {

  private TemporaryFS fs;

  private File file;

  private File img;

  protected void setUp() throws Exception {
    super.setUp();
    File fsRoot = new File(new File(System.getProperty("java.io.tmpdir")),
        "tests");
    new TemporaryFS(fsRoot).clean();
    fs = new TemporaryFS(fsRoot);
    file = fs.copy("test1/toto/tutu/tata.muse");
    fs.copy("test1/toto/tutu/2007.10.21.muse");
    img = fs.copy("test1/toto/muse-powered-by.png");
  }

  public void testMapRootToImplicitIndex() {
    PathMapper pm = new MuseMapper(new ImplicitMapper());
    final File dir = new File(fs.root(), "test1/toto/tutu/");
    pm.setRoot(dir);
    MusePage p = (MusePage) pm.map("/");
    assertNotNull(p);
    assertEquals(dir, p.getFile());
  }

  public void testMapEmptyStringToImplicitIndex() {
    PathMapper pm = new MuseMapper(new ImplicitMapper());
    final File dir = new File(fs.root(), "test1/toto/tutu/");
    pm.setRoot(dir);
    MusePage p = (MusePage) pm.map("");
    assertNotNull(p);
    assertEquals(dir, p.getFile());
  }

  public void testMapMuseExtendedFile() {
    PathMapper pm = new MuseMapper(new ImplicitMapper());
    final File dir = new File(fs.root(), "test1/");
    pm.setRoot(dir);
    MusePage p = (MusePage) pm.map("/toto/tutu/tata.muse");
    assertNotNull(p);
    assertEquals(new File(dir, "toto/tutu/tata.muse"), p.getFile());
  }

  public void testMapImplicitMuseFile() {
    PathMapper pm = new MuseMapper(new ImplicitMapper());
    final File dir = new File(fs.root(), "test1");
    pm.setRoot(dir);
    MusePage p = (MusePage) pm.map("/toto/tutu/tata");
    assertNotNull(p);
    assertEquals(new File(dir, "toto/tutu/tata.muse"), p.getFile());
  }

  public void testMapPlannerPage() {
    PathMapper pm = new MuseMapper(new PlannerPageMapper());
    final File dir = new File(fs.root(), "test1");
    pm.setRoot(dir);
    MusePage p = (MusePage) pm.map("/toto/tutu/2007.10.21");
    assertNotNull(p);
    assertEquals(new File(dir, "toto/tutu/2007.10.21.muse"), p.getFile());
  }

  public void testMapNonMuseResource() {
    PathMapper pm = new MuseMapper(new ImplicitMapper());
    final File dir = new File(fs.root(), "test1/");
    pm.setRoot(dir);
    DefaultPage p = (DefaultPage) pm.map("/toto/muse-powered-by.png");
    assertNotNull(p);
    assertEquals(new File(dir, "toto/muse-powered-by.png"), p.getFile());
  }

  public void testMapToGeneratedIndexFile() {
    PathMapper pm = new MuseMapper(new ImplicitMapper());
    final File dir = new File(fs.root(), "test1/");
    pm.setRoot(dir);
    MuseIndexPage p = (MuseIndexPage) pm.map("/toto/tutu/");
    assertNotNull(p);
    assertEquals(new File(dir, "toto/tutu/"), p.getFile());
  }

  public void testMapToIndexFileIfItExists() throws IOException {
    File index = fs.copy("test1/toto/tutu/index.muse");
    PathMapper pm = new MuseMapper(new ImplicitMapper());
    final File dir = new File(fs.root(), "test1/");
    pm.setRoot(dir);
    MusePage p = (MusePage) pm.map("/toto/tutu/");
    assertNotNull(p);
    assertEquals(index, p.getFile());
  }

  public void testMapToNonExistentFileReturnsNull() {
    PathMapper pm = new MuseMapper();
    final File dir = new File(fs.root(), "test1/");
    pm.setRoot(dir);
    MuseIndexPage p = (MuseIndexPage) pm.map("/should-not-exist");
    assertNull(p);

  }

  protected void tearDown() throws Exception {
    super.tearDown();
    fs.clean();
  }

}
