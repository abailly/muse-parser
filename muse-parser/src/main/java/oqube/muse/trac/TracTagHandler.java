/*
 * ______________________________________________________________________________
 * 
 * Copyright (C) 2006 Arnaud Bailly / OQube
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
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
 * email: contact@oqube.com creation: Wed Aug 23 2006
 */
package oqube.muse.trac;

import oqube.muse.AbstractTagHandler;
import oqube.muse.MuseSink;
import oqube.muse.SimpleTagHandler;

/**
 * A tag handler that output trac markup when encountering known tags.
 * 
 * @author abailly@oqube.com
 * @version $Id:
 *          /local/muse-parser/oqube/muse-parser/src/main/java/oqube/muse/html/MuseHTMLTagHandler.java
 *          1208 2007-01-28T10:19:06.618859Z nono $
 */
public class TracTagHandler extends AbstractTagHandler {

  private static final String EOL = System.getProperty("line.separator");

  class Example extends SimpleTagHandler {

    public void doHandle(MuseSink sink, String content) {
      sink.rawText(EOL);
      sink.rawText("{{{");
      sink.rawText(EOL);
      sink.rawText(content);
      sink.rawText(EOL);
      sink.rawText("}}}");
      sink.rawText(EOL);
    }
  }

  class Literal extends SimpleTagHandler {

    public void doHandle(MuseSink sink, String content) {
      sink.rawText(content);
    }
  }

  class Comment extends SimpleTagHandler {

    public void doHandle(MuseSink sink, String content) {
      sink.rawText(EOL);
      sink.rawText("{{{");
      sink.rawText(EOL);
      sink.rawText("#!comment");
      sink.rawText(EOL);
      sink.rawText(content);
      sink.rawText(EOL);
      sink.rawText("}}}");
      sink.rawText(EOL);
    }
  }

  class Source extends AbstractTagHandler {

    public boolean block(MuseSink sink, String tag, String[][] at,
        String content) {
      String lang = "java";
      for (int i = 0; i < at.length; i++)
        if ("lang".equals(at[i][0])) {
          lang = at[i][1];
          break;
        }
      sink.rawText(EOL);
      sink.rawText("{{{");
      sink.rawText(EOL);
      sink.rawText("#!" + lang);
      sink.rawText(EOL);
      sink.rawText(content);
      sink.rawText(EOL);
      sink.rawText("}}}");
      sink.rawText(EOL);
      return true;
    }

  }

  class Note extends SimpleTagHandler {

    protected void doHandle(MuseSink sink, String content) {
      sink.rawText(EOL);
      sink.rawText("{{{");
      sink.rawText(EOL);
      sink.rawText("#!html");
      sink.rawText(EOL);
      sink
          .rawText("<div style=\"background-color: yellow; border: thin solid red; margin: 10px; \">");
      sink.rawText(content);
      sink.rawText("</div>");
      sink.rawText(EOL);
      sink.rawText("}}}");
      sink.rawText(EOL);
  }
  }
  public TracTagHandler() {
    addTag("example", new Example());
    addTag("literal", new Literal());
    Comment comment = new Comment();
    addTag("comment", comment);
    Note note = new Note();
    addTag("note", note);
    Source src = new Source();
    addTag("src", src);
    addTag("source", src);
  }
}
