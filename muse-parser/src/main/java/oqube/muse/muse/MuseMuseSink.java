/**
 *  Copyright (C) 2006 - OQube / Arnaud Bailly
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

 Created 19 sept. 2006
 */
package oqube.muse.muse;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Stack;

import oqube.muse.DefaultSink;
import oqube.muse.ElementHandler;

/**
 * A class for producing muse formatted text from muse formatted text. This
 * class is used by preprocessor in literate programming environment to handle
 * source fragments and delegate muse formatting at a later stage.
 * 
 * @author nono
 * 
 */
public class MuseMuseSink extends DefaultSink {

  public class TitleElementHandler extends ElementHandler {

    private int level;

    public TitleElementHandler(int level) {
      super("title" + level);
      this.level = level;
    }

    @Override
    public void end() {
      out.println();
      out.println();
    }

    @Override
    public void start(Map<String, String> parameters) {
      for (int i = 0; i < level; i++)
        out.print("*");
      out.print(" ");
    }

  }

  private int lineWidth = 77;

  private class State {

    String indent = "";

    int index = 0;

    boolean atRowStart = false;

    State(String indent, int index, boolean row) {
      this.indent = indent;
      this.index = index;
      this.atRowStart = row;
    }

    State() {
    }
  }

  private State state = new State();

  /*
   * state stack.
   */
  private Stack indentStack = new Stack();

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.DefaultSink#addMetadata(java.lang.String, java.lang.String)
   */
  public void addMetadata(String s, String t) {
    out.println("#" + s + ' ' + t);
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.DefaultSink#anchor(java.lang.String)
   */
  public void anchor(String a) {
    out.println("#" + a);
  }

  class BasicElementHandler extends ElementHandler {

    private String markup;

    BasicElementHandler(String element, String markup) {
      super(element);
      this.markup = markup;
    }

    @Override
    public void end() {
      out.print(markup);
    }

    @Override
    public void start(Map<String, String> parameters) {
      out.print(markup);
    }

  }

  {
    delegate.append("document", new ElementHandler("document") {

      @Override
      public void start(Map<String, String> parameters) {
        state = new State();
        indentStack = new Stack();
      }
    });
    delegate.append("body", new ElementHandler("body") {

      @Override
      public void start(Map<String, String> parameters) {
        indentStack.push(state);
        state = new State();
      }
    });
    delegate.append("emph", new BasicElementHandler("emph", "*"));
    delegate.append("strong", new BasicElementHandler("strong", "**"));
    delegate.append("verb", new BasicElementHandler("verb", "="));
    delegate.append("uline", new BasicElementHandler("uline", "_"));
    delegate.append("math", new BasicElementHandler("math", "$"));
    delegate.append("title1", new TitleElementHandler(1));
    delegate.append("title2", new TitleElementHandler(2));
    delegate.append("title3", new TitleElementHandler(3));
    delegate.append("title4", new TitleElementHandler(4));
    delegate.append("para", new ElementHandler("para") {

      @Override
      public void start(Map<String, String> parameters) {
        out.print(state.indent);
      }

      @Override
      public void end() {
        out.println();
        out.println();
      }

    });
    delegate.append("center", new ElementHandler("center") {

      @Override
      public void end() {
        state = (State) indentStack.pop();
        out.println();
      }

      @Override
      public void start(Map<String, String> parameters) {
        indentStack.push(state);
        state = new State(state.indent + "      ", state.index,
            state.atRowStart);
      }

    });
    delegate.append("quote", new ElementHandler("quote") {

      @Override
      public void end() {
        state = (State) indentStack.pop();
        out.println();
      }

      @Override
      public void start(Map<String, String> parameters) {
        indentStack.push(state);
        state = new State(state.indent + "    ", state.index, state.atRowStart);
      }

    });
    delegate.append("enums", new ElementHandler("enums") {

      @Override
      public void end() {
        state = (State) indentStack.pop();
      }

      @Override
      public void start(Map<String, String> parameters) {
        out.println();
        indentStack.push(state);
        state = new State(state.indent + " ", 1, state.atRowStart);
      }

    });
    delegate.append("list", new ElementHandler("list") {

      @Override
      public void end() {
        state = (State) indentStack.pop();
      }

      @Override
      public void start(Map<String, String> parameters) {
        out.println();
        indentStack.push(state);
        state = new State(state.indent + " ", 0, state.atRowStart);
      }

    });
    delegate.append("item", new ElementHandler("item") {

      @Override
      public void end() {
        state = (State) indentStack.pop();
        out.println();
      }

      @Override
      public void start(Map<String, String> parameters) {
        out.print(state.indent);
        if (state.index >= 1) {
          out.print(state.index++);
          out.print('.');
        } else
          out.print("-");
        out.print(' ');
        indentStack.push(state);
        state = new State(state.indent + " ", state.index, state.atRowStart);
      }

    });
    delegate.append("table", new ElementHandler("table") {

      @Override
      public void end() {
        out.println();
      }

    });
    delegate.append("tableData", new ElementHandler("td") {

      @Override
      public void start(Map<String, String> parameters) {
        if (!state.atRowStart)
          out.print(" | ");
        state.atRowStart = false;
      }

    });
    delegate.append("tableHeader", new ElementHandler("th") {

      @Override
      public void start(Map<String, String> parameters) {
        if (!state.atRowStart)
          out.print(" || ");
        state.atRowStart = false;
      }

    });
    delegate.append("tableRow", new ElementHandler("tr") {

      @Override
      public void end() {
        state = (State) indentStack.pop();
        out.println();
      }

      @Override
      public void start(Map<String, String> parameters) {
        indentStack.push(state);
        state = new State(state.indent, state.index, false);
      }

    });

  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.DefaultSink#link(java.lang.String, java.lang.String)
   */
  public void link(String s, String t) {
    if (t != null)
      out.print("[[" + s + "][" + t + "]]");
    else
      out.print("[[" + s + "]]");
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.DefaultSink#rawText(java.lang.String)
   */
  public void rawText(String text) {
    out.print(text);
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.DefaultSink#separator()
   */
  public void separator() {
    out.println("-----");
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.DefaultSink#setLineWidth(int)
   */
  public void setLineWidth(int lw) {
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.DefaultSink#text(java.lang.String)
   */
  public void text(String txt) {
    out.print(state.indent);
    out.print(txt);
  }

  public void setOut(OutputStream os) {
    this.out = new PrintWriter(os);
  }

}
