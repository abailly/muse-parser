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
 * Created 21 sept. 2006
 */
package oqube.muse;

import java.io.File;

import oqube.muse.parser.MuseParser;
import oqube.muse.template.DefaultTemplateFactory;
import oqube.muse.template.Template;
import oqube.muse.template.TemplateFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base implementation for Publisher interface. This abstract class provides
 * basic implementation of the {@link oqube.muse.Publisher} interfac to ease
 * subclassing.
 * 
 * @author nono
 * 
 */
public abstract class AbstractPublisher implements Publisher {

  protected Log log = LogFactory.getLog(AbstractPublisher.class);

  protected MuseParser parser = new MuseParser();

  protected MuseSink sink;

  protected ThreadLocal th = new ThreadLocal();

  protected Template footer = Template.DEFAULT;

  protected Template header = Template.DEFAULT;

  protected TemplateFactory templates = new DefaultTemplateFactory();

  private String inputEncoding = System.getProperty("file.encoding");

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.Publisher#setOutputEncoding(java.lang.String)
   */
  public void setOutputEncoding(String outputEncoding) {
    sink.setEncoding(outputEncoding);
  }

  public String getOutputEncoding() {
    return sink.getEncoding();
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.Publisher#getSink(java.lang.String)
   */
  public MuseSink getSink(String id) {
    if (!th.get().equals(id))
      return null;
    return sink;
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.Publisher#setSink(java.lang.String, oqube.muse.MuseSink)
   */
  public void setSink(String id, MuseSink sink) {
    if (!th.get().equals(id))
      return;
    this.sink = sink;
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.Publisher#setLog(org.apache.commons.logging.Log)
   */
  public void setLog(Log log) {
    this.log = log;
  }

  /**
   * If muse file, returns the base name of the file with its extension replaced
   * by this publisher's extension. Else return the file's name.
   */
  public String getTargetName(File f) {
    String name = f.getName();
    if (name.endsWith(".muse")) {
      int i = name.indexOf('.');
      name = (i == -1) ? name : name.substring(0, i);
      name += getExtension();
    }
    return name;
  }

  public void setFooter(String string) {
    footer = templates.makeTemplate(string);
  }

  public void setHeader(String string) {
    header = templates.makeTemplate(string);
  }

  public void setInputEncoding(String inputEncoding) {
    this.inputEncoding = inputEncoding;
  }

  public String getInputEncoding() {
    return inputEncoding;
  }

  public TemplateFactory getTemplates() {
    return templates;
  }

  public void setTemplates(TemplateFactory templates) {
    this.templates = templates;
  }

}
