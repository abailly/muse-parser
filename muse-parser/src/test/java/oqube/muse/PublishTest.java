/*
 * ______________________________________________________________________________
 * 
 * Copyright 2006 Arnaud Bailly -
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * (1) Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * (2) Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * (3) The name of the author may not be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Created on Sun Aug 27 2006
 * 
 */
package oqube.muse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Collections;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.apache.commons.logging.Log;

/**
 * 
 * @author abailly@oqube.com
 * @version $Id$
 */
public class PublishTest extends TestCase {

  Publish pub;

  protected void setUp() throws Exception {
    super.setUp();
    this.pub = new Publish();
  }

  public void test01DefaultEnc() {
    String ienc = pub.getInputEncoding();
    String oenc = pub.getOutputEncoding();
    String def = System.getProperty("file.encoding");
    assertEquals("Incorrect input encoding", def, ienc);
    assertEquals("Incorrect output encoding", def, oenc);
  }

  public void test02NonExistentFile() {
    File f = new File("doesnotexist.file");
    try {
      pub.addFile(f);
      fail("Cannot add non existent files");
    } catch (IOException e) {
    }
  }

  public void testConfigurePublishWithNonExistentFormatShouldFail() {
    try {
      Publish.configurePublish(new String[] { "-f", "toto" }, pub);
      fail("should have thrown exception");
    } catch (Exception e) {
      //
    }
  }

  class MockPublisher extends AbstractPublisher {

    public void startSession(String id) {
      // TODO Auto-generated method stub

    }

    public void process(String session, Reader br, PrintWriter pw)
        throws IOException {
    }

    public MuseSink getSink(String id) {
      // TODO Auto-generated method stub
      return super.getSink(id);
    }

    public void setLog(Log log) {
      // TODO Auto-generated method stub
      super.setLog(log);
    }

    public void setOutputEncoding(String outputEncoding) {
      // TODO Auto-generated method stub
    }

    public void setSink(String id, MuseSink sink) {
    }

    public String getExtension() {
      // TODO Auto-generated method stub
      return null;
    }

    public void endSession(String id) {
      // TODO Auto-generated method stub

    }

  };

  private long ts = 0;

  public void testForceFlagShouldPublishNonModifiedFile() throws IOException {
    File tmp = new File(System.getProperty("java.io.tmpdir"));
    final File out = new File(tmp, "test.test");
    Publisher mock = new MockPublisher() {

      public void process(String session, Reader br, PrintWriter pw)
          throws IOException {
        ts = System.currentTimeMillis();
        if (!out.exists())
          out.createNewFile();
      }

      public String getTargetName(File f) {
        return "test.test";
      }

    };
    File f = File.createTempFile("test", ".muse");
    pub.setPublisher(mock);
    pub.publishFile(f, tmp);
    // redo publication
    long oldts = ts;
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    pub.setForce(true);
    pub.publishFile(f, tmp);
    assertTrue("target file not modified", ts > oldts);
    // delete
    assertTrue(out.delete());
  }

  public void testFalseForceFlagShouldNotPublishNonModifiedFile()
      throws IOException {
    File tmp = new File(System.getProperty("java.io.tmpdir"));
    final File out = new File(tmp, "test.test");
    if (out.exists())
      out.delete();
    Publisher mock = new MockPublisher() {
      int count = 0;

      public String getTargetName(File f) {
        return "test.test";
      }

      public void process(String session, Reader br, PrintWriter pw)
          throws IOException {
        if (++count > 1)
          throw new AssertionFailedError("should not have been called");
      }

    };
    File f = File.createTempFile("test", ".muse");
    pub.setPublisher(mock);
    assertTrue(!out.exists());
    pub.publishFile(f, tmp);
    // redo publication
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    pub.setForce(false);
    pub.publishFile(f, tmp);
  }

  public void testNonMuseFilesAreCopiedVerbatim() throws IOException {
    File tmp = new File(System.getProperty("java.io.tmpdir"));
    Publisher mock = new MockPublisher() {
      public void process(String session, Reader br, PrintWriter pw)
          throws IOException {
        throw new AssertionFailedError("should not have been called");
      }

      public String getTargetName(File f) {
        return "test.test";
      }

    };
    File f = File.createTempFile("test", ".data");
    FileWriter fw = new FileWriter(f);
    fw.append("This is some data.");
    fw.close();
    pub.setPublisher(mock);
    final File out = new File(tmp, mock.getTargetName(f));
    if (out.exists())
      out.delete();
    assertTrue(!out.exists());
    pub.setFiles(Collections.singletonList(f));
    pub.setOutputDirectory(tmp);
    pub.run();
    // check file has been copied
    assertTrue(out.exists());
    BufferedReader rd = new BufferedReader(new FileReader(out));
    String s = rd.readLine();
    assertEquals("Data not copied correctly", "This is some data.", s);
  }

}
