/**
 * Copyright (C) 2006 - OQube / Arnaud Bailly This library is free software; you
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
 * Created 19 sept. 2006
 */
package oqube.muse.html;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import oqube.muse.AbstractPublisher;
import oqube.muse.DefaultSink;

/**
 * Publish in html format. THis publisher also handles source code fragments.
 * 
 * @author nono
 * 
 */
public class HTMLPublisher extends AbstractPublisher {

  private BibtexLinker bibtexLinker;

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.Publisher#getExtension()
   */
  public String getExtension() {
    return ".html";
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.Publisher#process(java.lang.String, java.io.Reader,
   *      java.io.PrintWriter)
   */
  public void process(String session, Reader br, PrintWriter pw)
      throws IOException {
    if (!th.get()
           .equals(session))
      return;
    sink.setOut(pw);
    parser.setSink(sink);
    parser.setReader(br);
    sink.start("document", null);
    parser.start();
    // collect links into a div
    new BibtexHTMLFormatter().format(pw, bibtexLinker.getEntries());
    sink.end("document");
    pw.flush();
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.Publisher#startSession(java.lang.String)
   */
  public void startSession(String id) {
    th.set(id);
    // MuseAnchorLinker linker = new MuseAnchorLinker();
    bibtexLinker = new BibtexLinker();
    bibtexLinker.setNext(new DefaultHTMLLinker());
    defineSink();
  }

  protected void defineSink() {
    MuseHTMLTagHandler tagh = new MuseHTMLTagHandler();
    LinkingHTMLSourceTag src = new LinkingHTMLSourceTag();
    tagh.setNext(src);
    DefaultSink sink = new MuseHTMLSink();
    sink.setTagHandler(tagh);
    sink.setLinker(bibtexLinker);
    sink.setHeader(header);
    sink.setFooter(footer);
    this.sink = sink;
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.Publisher#endSession(java.lang.String)
   */
  public void endSession(String id) {
  }

}
