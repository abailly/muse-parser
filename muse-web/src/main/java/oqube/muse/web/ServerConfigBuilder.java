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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Build {@link ServerConfig} isntances from a textual description.
 * 
 * @author nono
 * 
 */
public class ServerConfigBuilder {

  private Map<String, ServerConfig> configurations = new HashMap<String, ServerConfig>();

  public void buildFromReader(java.io.Reader reader) throws IOException {
    BufferedReader rd = new BufferedReader(reader);
    Parser p = new Parser(rd);
    Animator a = new Animator();
    try {
      a.animateWith(this, p);
    } catch (Throwable e) {
      throw new IOException(e.getMessage());
    }
  }

  public ServerConfig newConfig(String name) {
    ServerConfig sc = new ServerConfig();
    configurations.put("", sc);
    return sc;
  }

  public ServerConfig newConfigMapTo(String name, String path) {
    ServerConfig sc = new ServerConfig(path);
    configurations.put(path, sc);
    return sc;
  }

  public Map<String, ServerConfig> getConfigurations() {
    return configurations;
  }

}
