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
package oqube.muse;

import java.io.PrintWriter;
import java.util.Map;

import oqube.muse.template.Template;

/**
 * This interface abstracts formatting events for parsing muse files.
 * 
 * @author abailly@oqube.muse.com
 * @version $Id$
 */
public interface MuseSink {

  /*
   * Role string for plexus container.
   */
  String ROLE = MuseSink.class.getName();

  void setLineWidth(int lw);

  /**
   * Main formatting method, start an element.
   * 
   * @param element
   *          name of element for format.
   * @param parameters
   *          additional parameters for theelement. May be null.
   */
  void start(String element, Map<String, String> parameters);

  /**
   * End of element.
   * 
   * @param element
   *          name of element.
   */
  void end(String element);

  /**
   * Called when text content is to be output. The difference between this
   * method and {@link #rawText(String)} is that the <code>txt</code>
   * parameter may be processed/encoded to fit this sink's format (eg. escape
   * special characters).
   * 
   * @param txt
   *          the text data.
   */
  void text(String txt);

  /**
   * Called to output raw text data. Data should be output as is by this sink
   * without any processing.
   * 
   * @param text
   *          the text to output.
   */
  void rawText(String text);

  /**
   * Generic formatting method for arbitrary tags denoting block level elements.
   * This method is called when parser encounter XML-style tags used in muse
   * that should be rendered as block-level elements. For example, in HTML this
   * kind of tag would be rendered by <code><div></code> tags with the tag
   * name as class attribute. <br />
   * <p />
   * The distinction between *block* and *flow* elements is enforced by the kind
   * of parser they are parsed with.
   * 
   * @param tag
   *          the tag name
   * @param currentArgs
   *          an array of arguments to the tag.
   * @param content
   *          raw text inside tag
   */
  void block(String tag, String[][] currentArgs, String content);

  /**
   * Formatting method for flow level tags.
   * 
   * @param tag
   *          the tag name
   * @param currentArgs
   *          an array of arguments to the tag.
   * @param content
   *          raw text inside tag
   */
  void flow(String tag, String[][] currentArgs, String content);

  /**
   * Format a link. A muse link may be either the nameof a muse file or some
   * hyperlink.
   * 
   * @param link
   *          the link.
   * @param text
   *          the text that should be decorated with the link.
   */
  void link(String link, String text);

  /**
   * Format an anchor.
   * 
   * @param anchor
   *          name of anchor.
   */
  void anchor(String anchor);

  /**
   * Metadata or processing directive for the sink. Usually for
   * 
   * @param name
   * @param value
   */
  void addMetadata(String name, String value);

  void separator();

  void setOut(PrintWriter pw);

  void setEncoding(String outputEncoding);

  void setFooter(Template footer);

  void setHeader(Template header);

  String getEncoding();

  /**
   * Notifies the sink that it should flush all pending contents 
   * for output.
   */
  void flush();
}
