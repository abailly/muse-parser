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
 * Created on Thu Jul 13 2006
 * 
 */
package oqube.muse;

import java.io.FileOutputStream;
import java.io.InputStream;

import junit.framework.TestCase;
import oqube.muse.html.LinkingHTMLSourceTag;
import oqube.muse.html.MuseAnchorLinker;
import oqube.muse.html.MuseHTMLSink;
import oqube.muse.html.MuseHTMLTagHandler;
import oqube.muse.parser.MuseParser;

/**
 * 
 * @author abailly@oqube.com
 * @version $Id:
 *          /local/muse-parser/oqube/muse-parser/src/test/java/oqube/muse/MuseSinkTest.java
 *          1276 2007-11-01T19:57:16.936009Z nono $
 */
public class MuseSinkTest extends TestCase {

  MuseParser parser;

  DefaultSink sink;

  protected void setUp() throws Exception {
    super.setUp();
    sink = new MuseHTMLSink();
    sink.setLinker(new MuseAnchorLinker());
    sink.setTagHandler(new MuseHTMLTagHandler());
    sink.setOut(new FileOutputStream("/tmp/sinktest.html"));
    parser = new MuseParser();
    parser.setSink(sink);
  }

  public void test01Index() {
    InputStream is = getClass().getResourceAsStream("/index.muse");
    sink.start("document", null);
    parser.setStream(is);
    parser.start();
    sink.end("document");
    sink.getOut().flush();
  }

  public void test02Nesting() {
    InputStream is = getClass().getResourceAsStream("/index-nested.muse");
    sink.start("document", null);
    parser.setStream(is);
    parser.start();
    sink.end("document");
    sink.getOut().flush();
  }

  public void test03Tables() {
    InputStream is = getClass().getResourceAsStream("/test19TableInItem.muse");
    sink.start("document", null);
    parser.setStream(is);
    parser.start();
    sink.end("document");
    sink.getOut().flush();
  }

  public void test04Sources() {
    InputStream is = getClass().getResourceAsStream("/testSourceTag1.muse");
    LinkingHTMLSourceTag tg = new LinkingHTMLSourceTag();
    tg.setNext(new MuseHTMLTagHandler());
    sink.setTagHandler(tg);
    sink.start("document", null);
    parser.setStream(is);
    parser.start();
    sink.end("document");
    sink.getOut().flush();

  }

}
