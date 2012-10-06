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
package oqube.muse.trac;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oqube.muse.DefaultSink;
import oqube.muse.ElementHandler;

/**
 * A class for producing Trac formatted text from muse formatted text. 
 * 
 * @author nono
 * 
 */
public class TracSink extends DefaultSink {

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

  private StringBuffer paragraph;

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
    out.println(a);
  }

  {
    delegate.append("center", new ElementHandler("center") {
      @Override
      public void start(Map<String, String> parameters) {
        indentStack.push(state);
        state = new State(state.indent + "      ", state.index,
            state.atRowStart);
      }

      public void end() {
        state = (State) indentStack.pop();
      }
    });

    delegate.append("enums", new ElementHandler("enums") {
      public void start(Map<String, String> parameters) {
        indentStack.push(state);
        state = new State(state.indent + " ", 1, state.atRowStart);
        out.println();
      }

      public void end() {
        state = (State) indentStack.pop();
      }
    });
    delegate.append("list", new ElementHandler("list") {
      public void start(Map<String, String> parameters) {
        indentStack.push(state);
        state = new State(state.indent + " ", 0, state.atRowStart);
        out.println();
      }

      public void end() {
        state = (State) indentStack.pop();
      }
    });
    delegate.append("quote", new ElementHandler("quote") {
      @Override
      public void start(Map<String, String> parameters) {
        indentStack.push(state);
        state = new State(state.indent + "  ", state.index, state.atRowStart);
        out.println();
        out.print(state.indent);
      }

      public void end() {
        state = (State) indentStack.pop();
      }
    });

    delegate.append("header", new ElementHandler("header") {
      public void end() {
        out.println();
      }
    });

    delegate.append("emph", new ElementHandler("emph") {
      @Override
      public void start(Map<String, String> parameters) {
        out.print("''");
      }

      public void end() {
        out.print("''");
      }
    });
    delegate.append("strong", new ElementHandler("strong") {
      @Override
      public void start(Map<String, String> parameters) {
        out.print("'''");
      }

      public void end() {
        out.print("'''");
      }
    });
    delegate.append("item", new ElementHandler("item") {
      @Override
      public void start(Map<String, String> parameters) {
        out.print(state.indent);
        if (state.index >= 1) {
          out.print(state.index++);
          out.print('.');
        } else
          out.print("*");
        out.print(' ');
        indentStack.push(state);
        state = new State(state.indent + " ", state.index, state.atRowStart);
      }

      public void end() {
        state = (State) indentStack.pop();
        out.println();
      }
    });
    delegate.append("table", new ElementHandler("table") {
      public void end() {
        out.println();
      }
    });
    delegate.append("tableRow", new ElementHandler("tableRow") {
      @Override
      public void start(Map<String, String> parameters) {
        indentStack.push(state);
        state = new State(state.indent, state.index, false);
      }

      public void end() {
        out.print("||");
        state = (State) indentStack.pop();
        out.println();
      }
    });
    delegate.append("title1", new ElementHandler("title1") {
      @Override
      public void start(Map<String, String> parameters) {
        out.println();
        out.print("= ");
      }

      public void end() {
        out.print(" =");
        out.println();
        out.println();
      }
    });
    delegate.append("title2", new ElementHandler("title2") {
      @Override
      public void start(Map<String, String> parameters) {
        out.println();
        out.print("== ");
      }

      public void end() {
        out.print(" ==");
        out.println();
        out.println();
      }
    });
    delegate.append("title3", new ElementHandler("title3") {
      @Override
      public void start(Map<String, String> parameters) {
        out.println();
        out.print("=== ");
      }

      public void end() {
        out.print(" ===");
        out.println();
        out.println();
      }
    });
    delegate.append("title4", new ElementHandler("title4") {
      @Override
      public void start(Map<String, String> parameters) {
        out.println();
        out.print("==== ");
      }

      public void end() {
        out.print(" ====");
        out.println();
        out.println();
      }
    });
    delegate.append("uline", new ElementHandler("uline") {
      @Override
      public void start(Map<String, String> parameters) {
        out.print("__");
      }

      public void end() {
        out.print("__");
      }
    });
    delegate.append("verb", new ElementHandler("verb") {
      @Override
      public void start(Map<String, String> parameters) {
        out.print("{{{");
      }

      public void end() {
        out.print("}}}");
      }
    });
    delegate.append("math", new ElementHandler("math") {
      @Override
      public void start(Map<String, String> parameters) {
        out.print("$");
      }

      public void end() {
        out.print("$");
      }
    });
    delegate.append("body", new ElementHandler("body") {
      @Override
      public void start(Map<String, String> parameters) {
        indentStack.push(state);
        state = new State();
      }
    });
    delegate.append("document", new ElementHandler("document") {
      @Override
      public void start(Map<String, String> parameters) {
        state = new State();
        indentStack = new Stack();
      }
    });
    delegate.append("para", new ElementHandler("para") {
      @Override
      public void start(Map<String, String> parameters) {
        out.print(state.indent);
      }
    });
    delegate.append("tableData", new ElementHandler("tableData") {
      @Override
      public void start(Map<String, String> parameters) {
        if (!state.atRowStart)
          out.print("||");
        state.atRowStart = false;
      }
    });
    delegate.append("tableHeader", new ElementHandler("tableHeader") {
      @Override
      public void start(Map<String, String> parameters) {
        if (!state.atRowStart)
          out.print("||");
        state.atRowStart = false;
      }
    });
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.DefaultSink#text(java.lang.String)
   */
  public void text(String txt) {
    out.print(txt);
  }

  /**
   * @param txt
   */
  private void paragraph(String txt) {
    String[] lines = splitLines(txt);
    for (int i = 0; i < lines.length; i++) {
      out.print(state.indent);
      out.print(lines[i]);
    }
  }

  public String[] splitLines(String txt) {
    List lines = new ArrayList();
    // look for first blank char before current linewidth limit
    Pattern pat = Pattern.compile("(.{1," + (lineWidth - state.indent.length())
        + "})\\b");
    Matcher mat = pat.matcher(txt);
    while (mat.find())
      lines.add(mat.group(1));
    return (String[]) lines.toArray(new String[lines.size()]);
  }

  public void setOut(OutputStream os) {
    this.out = new PrintWriter(os);
  }

  public int getLineWidth() {
    return lineWidth;
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.DefaultSink#link(java.lang.String, java.lang.String)
   */
  public void link(String s, String t) {
    if (linker != null)
      linker.link(this, s, t);
    else
      out.print("[" + s + " " + t + "]");

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
    out.println();
    out.println("-----");
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.DefaultSink#setLineWidth(int)
   */
  public void setLineWidth(int lw) {
    this.lineWidth = lw;
  }

}
