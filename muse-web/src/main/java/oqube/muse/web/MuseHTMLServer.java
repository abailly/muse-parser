/*
 * Copyright (c) 2007 - OQube / Arnaud Bailly This library is free software; you
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
 * Created 12 juil. 07
 */
package oqube.muse.web;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oqube.muse.html.HTMLPublisher;

class Header {

  private String data;

  private File file;

  Header(String hf, String hs) {
    this.data = hs;
    this.file = hf != null ? new File(hf) : null;
  }

  String content() {
    String content = data;
    try {
      FileReader fr = new FileReader(file);
      StringWriter sw = new StringWriter();
      char[] buf = new char[1024];
      int ln = 0;
      while ((ln = fr.read(buf)) != -1)
        sw.write(buf, 0, ln);
      fr.close();
      sw.close();
      content = sw.toString();
    } catch (Exception e) {
      // ignore
    }
    return content;
  }

  @Override
  public boolean equals(Object arg0) {
    Header h = (Header) arg0;
    if (h == null)
      return false;
    return data.equals(h.data) && file == null ? h.file == null
        : file.equals(h.file);
  }

}

/**
 * A class that serves transformation from muse files to html from a list of
 * directories.
 * 
 * @author nono
 * 
 */
public class MuseHTMLServer implements MuseServer {

  private String session;

  private Logger log = Logger.getLogger(MuseServlet.class.getName());

  private HTMLPublisher publisher;

  private Header header = new Header("", "");

  private String outputEncoding = System.getProperty("file.encoding");

  private String inputEncoding = System.getProperty("file.encoding");

  private Header footer = new Header("", "");

  private PathMapper mapper = PathMapper.DEFAULT;

  private File rootDirectory;

  private String path;

  public boolean serve(RequestConfig conf) {
    log.info("Try serving page at:" + this.path + conf.path);
    this.publisher.setHeader(header.content());
    this.publisher.setFooter(footer.content());
    this.publisher.startSession(this.session);
    this.publisher.setInputEncoding(inputEncoding);
    this.publisher.setOutputEncoding(outputEncoding);
    Page p = mapper.map(conf.path);
    if (p != null) {
      conf.type = p.contentType();
      conf.data = p.content(session);
      conf.length = p.length();
      conf.status = HttpURLConnection.HTTP_OK;
      log.info("Done serving page.");
      return true;
    } else {
      conf.status = HttpURLConnection.HTTP_NOT_FOUND;
      log.info("Cannot find page.");
      return false;
    }
  }

  public void init() {
    this.session = "" + new Date().getTime();
    this.publisher = new HTMLPublisher();
    mapper.setRoot(rootDirectory);
    mapper.setPublisher(this.publisher);
  }

  public Logger getLog() {
    return log;
  }

  public void setLog(Logger log) {
    this.log = log;
  }

  public String getSession() {
    return session;
  }

  public void setSession(String session) {
    this.session = session;
  }

  public String getOutputEncoding() {
    return outputEncoding;
  }

  public void setOutputEncoding(String inputEncoding) {
    this.outputEncoding = inputEncoding;
  }

  public void setInputEncoding(String initParameter) {
    this.inputEncoding = initParameter;
  }

  public void setFooter(Header header) {
    this.footer = header;
  }

  public void setHeader(Header header) {
    this.header = header;
  }

  public void setMapper(PathMapper mapper) {
    this.mapper = mapper;
  }

  public void setRootDirectory(File file) {
    this.rootDirectory = file;
  }

  public void setPath(String path) {
    this.path = path;
  }

}
