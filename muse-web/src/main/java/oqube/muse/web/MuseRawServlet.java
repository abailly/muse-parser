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
 * Created 4 nov. 07
 */
package oqube.muse.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Base servlet. Handles request routing and server invocation, but not server
 * initialization.
 * 
 * @author nono
 * 
 */
public class MuseRawServlet extends HttpServlet {
  protected MuseHTMLServer server;

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    doPost(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String spath = request.getPathInfo();
    RequestConfig conf = new RequestConfig();
    conf.path = spath;
    // publishable file ?
    if (!server.serve(conf))
      forward(request, response, spath);
    else {
      if (conf.data == null) {
        pageNotFound(response, spath);
      } else {
        contentFound(response, conf);
      }
    }
  }

  private void contentFound(HttpServletResponse response, RequestConfig conf)
      throws IOException {
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(conf.type);
    response.setContentLength(conf.length);
    print(conf.data, response);
  }

  private void pageNotFound(HttpServletResponse response, String spath)
      throws IOException {
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    print(new ByteArrayInputStream(("No page found at '" + spath + "'")
        .getBytes()), response);
  }

  private void forward(HttpServletRequest request,
      HttpServletResponse response, String spath) throws ServletException,
      IOException {
    final RequestDispatcher requestDispatcher = request
        .getRequestDispatcher(spath);
    if (requestDispatcher != null)
      requestDispatcher.forward(request, response);
    else
      pageNotFound(response, request.getServletPath() + spath);
  }

  /**
   * @param pw
   * @param r
   * @throws IOException
   */
  private void print(InputStream is, HttpServletResponse res)
      throws IOException {
    OutputStream os = res.getOutputStream();
    byte[] buf = new byte[1024];
    int ln = 0;
    while ((ln = is.read(buf)) != -1)
      os.write(buf, 0, ln);
    os.flush();
    os.close();
  }

  public MuseHTMLServer getServer() {
    return server;
  }

  public void setServer(MuseHTMLServer server) {
    this.server = server;
  }

  @Override
  public void init() throws ServletException {
    // configure publishing
    if (server != null)
      server.init();
    else
      throw new ServletException("Servlet does not have its server configured");
  }

}
