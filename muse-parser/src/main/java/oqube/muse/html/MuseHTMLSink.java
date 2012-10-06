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
 * Created on Tue Jul 11 2006
 * 
 */
package oqube.muse.html;

import java.io.StringReader;
import java.util.Map;

import oqube.muse.DefaultSink;
import oqube.muse.ElementHandler;
import oqube.muse.feed.FeedUtils;
import oqube.muse.feed.TextAccumulator;
import oqube.muse.parser.MuseParser;
import oqube.muse.template.Template;

/**
 * 
 * @author abailly@oqube.muse.com
 * @version $Id:
 *          /local/muse-parser/oqube/muse-parser/src/main/java/oqube/muse/html/MuseHTMLSink.java
 *          1253 2007-06-19T06:54:11.207475Z nono $
 */
public class MuseHTMLSink extends DefaultSink {
  /**
   * @plexus.configuration default-value="html PUBLIC \"-//W3C//DTD XHTML 1.0
   *                       Transitional//EN\"
   *                       \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\""
   */
  private String doctype = "html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"";

  private TextAccumulator accumulator = new TextAccumulator();

  public String getDoctype() {
    return doctype;
  }

  public void setDoctype(String doctype) {
    this.doctype = doctype;
  }

  class HTMLElementHandler extends ElementHandler {

    protected HTMLElementHandler(String name) {
      super(name);
    }

    @Override
    public void end() {
      flushAccumulatedText();
      out.print("</" + name + ">");
    }

    @Override
    public void start(Map<String, String> parameters) {
      flushAccumulatedText();
      StringBuffer sb = new StringBuffer("<");
      sb.append(name);
      if (parameters != null)
        for (Map.Entry<String, String> e : parameters.entrySet()) {
          sb.append(" ")
            .append(e.getKey())
            .append("=\"")
            .append(e.getValue())
            .append("\" ");
        }
      sb.append(">");
      out.print(sb.toString());
    }

    protected void flushAccumulatedText() {
      out.print(getAccumulator().content());
      getAccumulator().reset();
    }

  }

  class BlockHTMLElementHandler extends HTMLElementHandler {

    protected BlockHTMLElementHandler(String name) {
      super(name);
    }

    @Override
    public void end() {
      super.end();
      out.println();
    }

    @Override
    public void start(Map<String, String> parameters) {
      super.start(parameters);
    }

  }

  class TitleHandler extends BlockHTMLElementHandler {

    protected TitleHandler(String name) {
      super(name);
    }

    @Override
    public void start(Map<String, String> parameters) {
      super.start(parameters);
    }

    @Override
    public void end() {
      String escapeTitle = FeedUtils.escapeTitle(getAccumulator().content());
      MuseHTMLSink.this.anchor(escapeTitle);
      super.end();
    }

  }

  {
    delegate.append("document", new ElementHandler("document") {
      public void start(Map<String, String> parameters) {
        out.println("<?xml  version=\"1.0\" encoding=\"" + getEncoding()
            + "\" ?>");
        out.println("<!DOCTYPE " + getDoctype() + " >");
        out.println("<html>");
      }

      public void end() {
        out.println("</html>");
      }
    });
    delegate.append("tasks", new ElementHandler("tasks") {

      @Override
      public void end() {
        out.println("</ul>");
      }

      @Override
      public void start(Map<String, String> parameters) {
        out.print("<ul class=\"tasks\">");
      }

    });
    delegate.append("header", new BlockHTMLElementHandler("head") {

      @Override
      public void end() {
        if (header == Template.DEFAULT)
          super.end();
      }

      @Override
      public void start(Map<String, String> parameters) {
        if (header == Template.DEFAULT)
          super.start(parameters);
      }

    });
    delegate.append("footer", new BlockHTMLElementHandler("footer") {

      @Override
      public void end() {
      }

      @Override
      public void start(Map<String, String> parameters) {
        flushAccumulatedText();
        if (footer == Template.DEFAULT)
          super.start(parameters);
      }

    });
    delegate.append("body", new BlockHTMLElementHandler("body") {

      @Override
      public void end() {
        if (footer == Template.DEFAULT)
          super.end();
      }

      @Override
      public void start(Map<String, String> parameters) {
        if (header == Template.DEFAULT)
          super.start(parameters);
      }

    });
    delegate.append("task", new ElementHandler("task") {

      MuseParser p = new MuseParser();
      {
        p.setFragment(true);
        p.setSink(MuseHTMLSink.this);
      }

      @Override
      public void end() {
        out.println("</span></span></span></li>");
      }

      @Override
      public void start(Map<String, String> parameters) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<li class=\"task\">");
        sb.append("<span class=\"")
          .append("priority")
          .append(parameters.get("priority"))
          .append("\">");
        sb.append("<span class=\"")
          .append("level")
          .append(parameters.get("level"))
          .append("\">");
        sb.append("<span class=\"")
          .append("status")
          .append(parameters.get("status"))
          .append("\">");
        out.print(sb.toString());
        p.setReader(new StringReader(parameters.get("description")));
        p.start();
        link(parameters.get("link"), null);
      }

    });
    delegate.append("para", new BlockHTMLElementHandler("p"));
    delegate.append("section", new BlockHTMLElementHandler("div"));
    delegate.append("list", new BlockHTMLElementHandler("ul"));
    delegate.append("item", new BlockHTMLElementHandler("li"));
    delegate.append("quote", new BlockHTMLElementHandler("quote"));
    delegate.append("center", new BlockHTMLElementHandler("center"));
    delegate.append("enums", new BlockHTMLElementHandler("ol"));
    delegate.append("title1", new TitleHandler("h1"));
    delegate.append("title2", new TitleHandler("h2"));
    delegate.append("title3", new TitleHandler("h3"));
    delegate.append("title4", new TitleHandler("h4"));
    delegate.append("emph", new HTMLElementHandler("em"));
    delegate.append("strong", new HTMLElementHandler("strong"));
    delegate.append("verb", new HTMLElementHandler("code"));
    delegate.append("uline", new HTMLElementHandler("u"));
    delegate.append("table", new BlockHTMLElementHandler("table"));
    delegate.append("tableData", new HTMLElementHandler("td"));
    delegate.append("tableHeader", new HTMLElementHandler("th"));
    delegate.append("tableRow", new BlockHTMLElementHandler("tr"));
    delegate.append("math", new ElementHandler("math") {

      @Override
      public void end() {
        out.print("`");
      }

      @Override
      public void start(Map<String, String> parameters) {
        out.print("`");
      }

    });

  }

  public void separator() {
    out.println("<hr />");
  }

  public void setLineWidth(int lw) {
  }

  public void text(String txt) {
    getAccumulator().accumulate(htmlEscape(txt));
  }

  /**
   * 
   * @param string
   * @return
   * @see http://www.rgagnon.com/javadetails/java-0306.html
   */
  private String htmlEscape(String string) {
    StringBuffer sb = new StringBuffer(string.length());
    // true if last char was blank
    boolean lastWasBlankChar = false;
    int len = string.length();
    char c;

    for (int i = 0; i < len; i++) {
      c = string.charAt(i);
      if (c == ' ') {
        // blank gets extra work,
        // this solves the problem you get if you replace all
        // blanks with &nbsp;, if you do that you loss
        // word breaking
        if (lastWasBlankChar) {
          lastWasBlankChar = false;
          sb.append("&nbsp;");
        } else {
          lastWasBlankChar = true;
          sb.append(' ');
        }
      } else {
        lastWasBlankChar = false;
        //
        // HTML Special Chars
        if (c == '"')
          sb.append("&quot;");
        else if (c == '&')
          sb.append("&amp;");
        else if (c == '<')
          sb.append("&lt;");
        else if (c == '>')
          sb.append("&gt;");
        else {
          sb.append(c);
        }
      }
    }
    return sb.toString();
  }

  public void rawText(String txt) {
    getAccumulator().accumulate(txt);
  }

  public void setAccumulator(TextAccumulator accumulator) {
    this.accumulator = accumulator;
  }

  public TextAccumulator getAccumulator() {
    return accumulator;
  }

  @Override
  public void flush() {
    out.print(getAccumulator().content());
  }

}
