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
 * Created on Mon Aug 21 2006
 * 
 */
package oqube.muse.parser;

import java.io.PrintWriter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oqube.muse.MuseSink;
import oqube.muse.template.Template;

/**
 * This lexer wraps another sink to format accumulated text lines.
 * 
 * A flow lexer wraps the  {@link MuseSink} methods so that the string gets
 * proper formatting for inline (or *flow*) elements that appear in this block.
 * The basic idea is then to let block lexers call the wrapped sink methods
 * instead of the real methods. When {@link MuseSink.text(String)} method is
 * called, this indicates that parsing is inside a block level element so text
 * gets accumulated in an internal buffer.
 * 
 * When any block level method is called on this sink, the chain of lexer is
 * asked to format the accumulated text: this text is parsed for appropriate
 * patterns and each lexer as a chance to do something with matching framgments.
 * 
 * Note that the order of lexers in the chain may be important. It is customary
 * to add the {@see IdentityLexer} at the bottom of the stack to pass final
 * 
 * 
 * @author abailly@oqube.muse.com
 * @version $Id$
 */
public abstract class FlowLexer extends AbstractLexer implements MuseSink {

  protected StringBuffer text;

  /* real sink - forwarded events */
  private MuseSink wrappedSink;

  /**
   * Returns the wrapped sink.
   * 
   * @return a wrapped MuseSink instance.
   */
  public MuseSink getWrappedSink() {
    return wrappedSink;
  }

  /**
   * Sets the sink to wrap for this lexer.
   * 
   * @param wrappedSink
   *          the Sink to wrap.
   */
  public void setWrappedSink(MuseSink wrappedSink) {
    this.wrappedSink = wrappedSink;
    if (next != null)
      ((FlowLexer) next).setWrappedSink(wrappedSink);
  }

  /**
   * Construct a new FlowLexer for given pattern.
   * 
   * @param pattern
   *          the pattern that this lexer matches.
   * @see java.util.regex.Pattern
   */
  public FlowLexer(String pattern) {
    super(pattern);
  }

  public void setLineWidth(int lw) {
  }

  public void end(String element) {
    if (!isFlow(element))
      formatText();
    wrappedSink.end(element);
  }

  private void formatText() {
    if (text != null) {
      format(text.toString());
      text = null;
    }
  }

  public void flush() {
    formatText();
  }

  public void start(String element, Map<String, String> parameters) {
    if (!isFlow(element))
      formatText();
    wrappedSink.start(element, parameters);
  }

  private boolean isFlow(String element) {
    return ("emph".equals(element) || "verb".equals(element)
        || "uline".equals(element) || "strong".equals(element) || "math"
        .equals(element));
  }

  /**
   * Accumulates text in this lexer. Note that this method is used only in the
   * first lexer of the chain.
   */
  public void text(String txt) {
    /* initialize buffer */
    if (text == null)
      text = new StringBuffer();
    /* handle eol */
    Pattern p = Pattern.compile("(.*)-\\n");
    Pattern eol = Pattern.compile("(.*)\\n");
    Matcher m = p.matcher(txt);
    Matcher eolm = eol.matcher(txt);
    if (m.matches())
      text.append(m.group(1));
    else if (eolm.matches())
      text.append(eolm.group(1)).append(' ');
    else
      text.append(txt);
  }

  public void rawText(String text) {
    if (text != null) {
      format(text.toString());
      text = null;
    }
    wrappedSink.rawText(text);
  }

  /*
   * Main formatting method called at end of blocks. This method is called with
   * the accumulated block's text as arguemnet.
   */
  protected void format(String s) {
    int i = 0;
    /* lookup all matches in buffer */
    matcher.reset(s);
    while (matcher.find()) {
      /* extract unmatched part */
      int st = matcher.start();
      if (st > i) {
        String sub = s.substring(i, st);
        /* pass it over to next lexer */
        assert next != null;
        ((FlowLexer) next).format(sub);
      }
      handler(); // real work happens here
      i = matcher.end();
    }
    /* format end of string */
    if (i > 0 && i < s.length() - 2) {// there has been one match and there is a
      // tail
      assert s.substring(i).length() > 0;
      ((FlowLexer) next).format(s.substring(i));
    } else if (i == 0 && s.length() > 0) {// no match
      assert s.substring(i).length() > 0;
      ((FlowLexer) next).format(s);
    }
  }

  public void flow(String s, String[][] at, String content) {
    formatText();
    wrappedSink.flow(s, at, content);
  }

  public void block(String s, String[][] at, String content) {
    formatText();
    wrappedSink.block(s, at, content);
  }

  public void link(String s, String t) {
    wrappedSink.link(s, t);
  }

  public void anchor(String s) {
    wrappedSink.anchor(s);
  }

  public void addMetadata(String s, String t) {
    wrappedSink.addMetadata(s, t);
  }

  public void separator() {
    wrappedSink.separator();
  }

  public void setOut(PrintWriter pw) {
    wrappedSink.setOut(pw);
  }

  public void setEncoding(String outputEncoding) {
    wrappedSink.setEncoding(outputEncoding);
  }

  public String getEncoding() {
    return wrappedSink.getEncoding();
  }

  public void setFooter(Template footer) {
    wrappedSink.setFooter(footer);
  }

  public void setHeader(Template header) {
    wrappedSink.setHeader(header);
  }

}
