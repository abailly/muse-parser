/**
 *  Copyright (C) 2006 - OQube / Arnaud Bailly
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Created on Wed Sep 20 2006
 */
package oqube.muse;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.Stack;

import oqube.muse.literate.LiterateMuse;
import oqube.muse.literate.LiteratePublisher;

import junit.framework.TestCase;

/**
 * 
 * @author abailly@oqube.com
 * @version $Id$
 */
public class MusePublishTest extends TestCase {

  private File temp;

  protected void setUp() throws Exception {
    super.setUp();
    // create temporary directory
    temp = new File("temp");
  }

  protected void tearDown() throws Exception {
    super.tearDown();
    // cleanup temp
    deleteDir(temp);
  }

  private void deleteDir(File temp2) {
    File[] fs = temp2.listFiles();
    if (fs == null)
      return;
    for (int i = 0; i < fs.length; i++) {
      if (fs[i].isDirectory())
        deleteDir(fs[i]);
      else
        fs[i].delete();
    }
    temp2.delete();
  }

  /*
   * flat directory testing.
   */
  public void test01() {
    Publish pub = new Publish();
    pub.setOutputDirectory(temp);
    pub.setFiles(Arrays.asList(new File[] { new File(
        "src/test/resources/test01") }));
    pub.setPublisher(PublisherFactory.instance("xhtml"));
    pub.run();
    // check published files exists and are not empty
    File wit = new File(temp, "index.html");
    assertTrue(wit.exists() && wit.length() > 0);
  }

  /*
   * nested directory testing.
   */
  public void test02() {
    Publish pub = new Publish();
    pub.setOutputDirectory(temp);
    pub.setFiles(Arrays.asList(new File[] { new File(
        "src/test/resources/test02") }));
    pub.setPublisher(PublisherFactory.instance("xhtml"));
    pub.run();
    // check published files exists and are not empty
    File wit = new File(temp, "subdir/subsubdir/muse-standalone.html");
    assertTrue(wit.exists() && wit.length() > 0);
  }

  /*
   * exclusion testing.
   */
  public void test03() {
    Publish pub = new Publish();
    pub.setOutputDirectory(temp);
    pub.setFiles(Arrays.asList(new File[] { new File(
        "src/test/resources/test03") }));
    pub.setPublisher(PublisherFactory.instance("xhtml"));
    pub.excludePatterns(Arrays.asList(new String[] { ".*index.*",
        ".*subsubdir/.*", ".*.svn.*" }));
    pub.run();
    // check published files exists and are not empty
    File wit = new File(temp, "subdir/subsubdir/muse-standalone.html");
    assertTrue(!wit.exists());
    assertTrue(!new File(temp, "subdir/subsubdir/").exists());
    assertTrue(!new File(temp, "index.html").exists());
  }

  /*
   * tests muse publishing.
   * 
   */
  public void test04() {
    Publish pub = new Publish();
    pub.setOutputDirectory(temp);
    pub.setFiles(Arrays.asList(new File[] { new File(
        "src/test/resources/test02") }));
    pub.setPublisher(PublisherFactory.instance("muse"));
    pub.run();
    // check published files exists and are not empty
    File wit = new File(temp, "subdir/subsubdir/muse-standalone.muse");
    assertTrue(wit.exists() && wit.length() > 0);
  }

  /*
   * tests non publishing if not modified.
   */
  public void test05() throws InterruptedException {
    Publish pub = new Publish();
    pub.setOutputDirectory(temp);
    pub.setFiles(Arrays.asList(new File[] { new File(
        "src/test/resources/test02") }));
    pub.setPublisher(PublisherFactory.instance("xhtml"));
    pub.run();
    // touch one file
    File f = new File("src/test/resources/test02/index.muse");
    File mod = new File(temp, "index.html");
    long timeMod = mod.lastModified();
    boolean modflag = false;
    Thread.currentThread().sleep(1000);
    modflag = f.setLastModified(new Date().getTime());
    File witNoMod = new File(temp, "subdir/subsubdir/muse-standalone.html");
    long timeNotMod = witNoMod.lastModified();
    // republish
    pub.run();
    if (modflag) {
      mod = new File(temp, "index.html");
      assertFalse(timeMod == mod.lastModified());
    }
    assertEquals(timeNotMod, witNoMod.lastModified());
  }
  
  /*
   * test literate publisher.
   */
  public void test06() {
    Publish pub = new Publish();
    pub.setOutputDirectory(temp);
    pub.setFiles(Arrays.asList(new File[] { new File(
        "src/test/resources/test06") }));
    LiteratePublisher lp = new LiteratePublisher();
    lp.setHeader("// generated by muse-parser java v1.0");
    lp.setFooter("// generated: " + new Date());
    lp.setMultifile(false);
    File srcout = new File("target");
    lp.setOutputDirectory(srcout);
    lp.setPublisher(PublisherFactory.instance("xhtml"));
    pub.setPublisher(lp);
    pub.run();
    // check headers and footers
    File doc = new File(temp,"literate.html");
    assertTrue(doc.exists() && doc.length() > 0);
    // check files are generated
    File src = new File(srcout,"toto/Toto.java");
    assertTrue(src.exists() && src.length() > 0);
    
  }
  
  /*
   * test cli for literatemuse
   */
  public void test07() {
    String[] args = new String[]{"-d",temp.getPath(),"-s","target","-f","muse","src/test/resources/test06"};
    LiterateMuse.main(args);
    // check headers and footers
    File doc = new File(temp,"literate.muse");
    assertTrue(doc.exists() && doc.length() > 0);
    // check files are generated
    File src = new File("target/toto/Toto.java");
    assertTrue(src.exists() && src.length() > 0);    
  }
}
