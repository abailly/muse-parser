/*
 *  Copyright 2006 OQube contact at oqube dot com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package oqube.muse.doxia;

import java.io.PrintWriter;

import oqube.muse.MuseLinker;
import oqube.muse.DefaultSink;
import oqube.muse.MuseTagHandler;
import oqube.muse.ElementHandler;

import org.apache.maven.doxia.sink.Sink;

import java.util.Map;
import java.util.LinkedList;

/**
 * This class wraps a doxia sink into a muse sink.
 * 
 * @author abailly@oqube.muse.com
 * @version $Id$
 * @plexus.component role="oqube.muse.MuseSink" role-hint="muse-doxia"
 */
public class SinkWrapper extends DefaultSink
{

    /**
     * @plexus.requirement role="oqube.muse.MuseLinker" role-hint="muse-linker"
     */
    private MuseLinker linker;

    /**
     * @plexus.requirement role="oqube.muse.MuseTagHandler" role-hint="muse-xhtml"
     */
    private MuseTagHandler tagHandler;

    /*
     * actual wrapped sink.
     */
    private Sink sink;

    public Sink getSink()
    {
        return sink;
    }

    public void setSink( Sink sink )
    {
        this.sink = sink;
    }

    public void setLineWidth( int lw )
    {
    }

  enum ListType {
    UL, OL;
  }
  
  private LinkedList<ListType> lists = new LinkedList<ListType>();

  {
    delegate.append("document", new ElementHandler("document") {

	public void end() {
	  sink.flush();
	  sink.close();
	}

    });

    delegate.append("body", new ElementHandler("body") {
      public void start(Map<String, String> parameters) {
	sink.body();
      }
      public void end() {
	sink.body_();
      }
    });

    delegate.append("para", new ElementHandler("para") {
      public void start(Map<String, String> parameters) {
	sink.paragraph();
      }
      public void end() {
	sink.paragraph_();
      }
    });

    delegate.append("list", new ElementHandler("list") {
      public void start(Map<String, String> parameters) {
	sink.list();
	lists.addLast(ListType.UL);
      }
      public void end() {
	lists.removeLast();
	sink.list_();
      }
    });

    delegate.append("item", new ElementHandler("item") {
      public void start(Map<String, String> parameters) {
	switch(lists.peek()) {
	case UL:
	  sink.listItem();
	  break;
	case OL:
	  sink.numberedListItem();
	}
      }
      public void end() {
	switch(lists.peek()) {
	case UL:
	  sink.listItem_();
	  break;
	case OL:
	  sink.numberedListItem_();
	}
      }
    });

    delegate.append("enums", new ElementHandler("enums") {
      public void start(Map<String, String> parameters) {
        sink.numberedList( Sink.NUMBERING_DECIMAL );
	lists.addLast(ListType.OL);
      }
      public void end() {
	lists.removeLast();
        sink.numberedList_();
      }
    });

    delegate.append("title1", new ElementHandler("title1") {
      public void start(Map<String, String> parameters) {
	sink.sectionTitle1();
      }
      public void end() {
	sink.sectionTitle1_();
      }
    });

    delegate.append("title2", new ElementHandler("title2") {
      public void start(Map<String, String> parameters) {
	sink.sectionTitle2();
      }
      public void end() {
	sink.sectionTitle2_();
      }
    });

    delegate.append("title3", new ElementHandler("title3") {
      public void start(Map<String, String> parameters) {
	sink.sectionTitle3();
      }
      public void end() {
	sink.sectionTitle3_();
      }
    });

    delegate.append("title4", new ElementHandler("title4") {
      public void start(Map<String, String> parameters) {
	sink.sectionTitle4();
      }
      public void end() {
	sink.sectionTitle4_();
      }
    });

    delegate.append("title1", new ElementHandler("title1") {
      public void start(Map<String, String> parameters) {
	sink.sectionTitle1();
      }
      public void end() {
	sink.sectionTitle1_();
      }
    });

    delegate.append("emph", new ElementHandler("emph") {
      public void start(Map<String, String> parameters) {
	sink.italic();
      }
      public void end() {
	sink.italic_();
      }
    });

    delegate.append("strong", new ElementHandler("strong") {
      public void start(Map<String, String> parameters) {
	sink.bold();
      }
      public void end() {
	sink.bold_();
      }
    });

    delegate.append("verb", new ElementHandler("verb") {
      public void start(Map<String, String> parameters) {
	sink.monospaced();
      }
      public void end() {
	sink.monospaced_();
      }
    });

    delegate.append("table", new ElementHandler("table") {
      public void start(Map<String, String> parameters) {
	sink.table();
      }
      public void end() {
	sink.table_();
      }
    });

    delegate.append("tableHeader", new ElementHandler("tableHeader") {
      public void start(Map<String, String> parameters) {
	sink.tableHeaderCell();
      }
      public void end() {
	sink.tableHeaderCell_();
      }
    });

    delegate.append("tableData", new ElementHandler("tableData") {
      public void start(Map<String, String> parameters) {
	sink.tableCell();
      }
      public void end() {
	sink.tableCell_();
      }
    });

    delegate.append("tableRow", new ElementHandler("tableRow") {
      public void start(Map<String, String> parameters) {
	sink.tableRow();
      }
      public void end() {
	sink.tableRow_();
      }
    });

      delegate.append("math", new ElementHandler("math") {
      public void start(Map<String, String> parameters) {
        sink.rawText( "`" );
      }
      public void end() {
        sink.rawText( "`" );
      }
    });

  }

    public void text( String txt )
    {
        sink.text( txt );
    }

    public void separator()
    {
        sink.horizontalRule();
    }

    public void rawText( String text )
    {
        sink.rawText( text );
    }

  public void link(String s, String t) {
    if (linker != null)
      linker.link(this, s, t);
  }

  public void anchor(String a) {
    if (linker != null)
      linker.anchor(this, a);
  }


      /*
     * (non-Javadoc)
     * 
     * @see oqube.muse.MuseSink#setEncoding(java.lang.String)
     */
    public void setEncoding( String arg0 )
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see oqube.muse.MuseSink#setOut(java.io.PrintWriter)
     */
    public void setOut( PrintWriter arg0 )
    {
    }
}
