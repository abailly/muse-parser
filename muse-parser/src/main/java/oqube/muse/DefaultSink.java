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
package oqube.muse;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import oqube.muse.template.Template;

/**
 * Default base class for sinks. Provide some code base for handling links
 * resolution and tag formatting. This class does nothing with formatting
 * structure but passes links to linker and tags to tag handlers.
 * 
 * @author nono
 * @version $Id:
 *          /local/muse-parser/oqube/muse-parser/src/main/java/oqube/muse/DefaultSink.java
 *          1259 2007-07-09T12:14:07.248654Z nono $
 * @plexus.component role="oqube.muse.MuseSink"
 */
public class DefaultSink implements MuseSink {

  private static SimpleDateFormat yearFrm = new SimpleDateFormat("yyyy");

  /**
   * @plexus.requirement role="oqube.muse.MuseLinker"
   */
  protected MuseLinker linker;

  /**
   * @plexus.requirement role="oqube.muse.MuseTagHandler"
   */
  protected MuseTagHandler tagHandler;

  /**
   * @plexus.configuration default-value="UTF-8"
   */
  private String encoding = "UTF-8";

  protected Template header = Template.DEFAULT;

  protected Template footer = Template.DEFAULT;

  protected Map<Object, Object> environment;

  protected PrintWriter out;;

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.MuseSink#setLineWidth(int)
   */
  public void setLineWidth(int lw) {
  }

  protected FormatterDelegate delegate = new FormatterDelegate();

  public void end(String element) {
    delegate.end(element);
  }

  public void start(String element, Map<String, String> parameters) {
    delegate.start(element, parameters);
  }

  {
    delegate.append("document", new ElementHandler("document") {
      public void start(Map<String, String> parameters) {
        Date date = new Date();
        environment = new HashMap<Object, Object>();
        /* add some default variables into environment */
        environment.putAll(System.getProperties());
        environment.put("date", DateFormat.getDateTimeInstance().format(date));
        environment.put("year", yearFrm.format(date));
        environment.put("encoding", getEncoding());
      }

    });

    delegate.append("header", new ElementHandler("header") {
      public void end() {
        if (out != null)
          out.print(header.content(environment));
      }
    });

    delegate.append("footer", new ElementHandler("footer") {
      public void end() {
        if (out != null)
          out.print(footer.content(environment));
      }
    });
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.MuseSink#text(java.lang.String)
   */
  public void text(String txt) {

  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.MuseSink#rawText(java.lang.String)
   */
  public void rawText(String text) {

  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.MuseSink#addMetadata(java.lang.String, java.lang.String)
   */
  public void addMetadata(String s, String t) {
    if (environment != null)
      environment.put(s, t);
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.MuseSink#separator()
   */
  public void separator() {

  }

  public MuseLinker getLinker() {
    return linker;
  }

  public void setLinker(MuseLinker linker) {
    this.linker = linker;
  }

  public MuseTagHandler getTagHandler() {
    return tagHandler;
  }

  public void setTagHandler(MuseTagHandler tagHandler) {
    this.tagHandler = tagHandler;
  }

  /**
   * Generic formatting method for arbitrary tags denoting block level elements.
   * This method is called when parser encounter XML-style tags used in muse.
   * 
   * @param tag
   *          the tag name
   */
  public void block(String tag, String[][] at, String content) {
    if (tagHandler != null)
      tagHandler.block(this, tag, at, content);
  }

  public void flow(String tag, String[][] at, String content) {
    if (tagHandler != null)
      tagHandler.flow(this, tag, at, content);
  }

  public void link(String s, String t) {
    if (linker != null)
      linker.link(this, s, t);
  }

  public void anchor(String a) {
    if (linker != null)
      linker.anchor(this, a);
  }

  public String getEncoding() {
    return encoding;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  public void setHeader(Template template) {
    this.header = template;
  }

  public PrintWriter getOut() {
    return out;
  }

  /**
   * Sets the output to use for generating data.
   */
  public void setOut(PrintWriter out) {
    this.out = out;
  }

  public void setFooter(Template template) {
    this.footer = template;
  }

  public void setOut(OutputStream os) {
    this.out = new PrintWriter(os);
  }

  protected String getProperty(String propertyName) {
    Object property = environment.get(propertyName);
    return property == null ? "" : property.toString();
  }

  public void flush() {
    // TODO Auto-generated method stub
    
  }

}
