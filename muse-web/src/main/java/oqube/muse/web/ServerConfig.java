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
 * Created 3 nov. 07
 */
package oqube.muse.web;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.naming.ConfigurationException;

/**
 * A class for configuring an instance of {@link MuseHTMLServer}.
 * 
 * @author nono
 * 
 */
public class ServerConfig {

  private PathMapper mapper = PathMapper.DEFAULT;

  private File rootDirectory = new File(".");

  private PathMapper lastMapper = mapper;

  private Header header = new Header("", "");

  private Header footer = new Header("", "");

  private String inputEncoding = System.getProperty("file.encoding");

  private String outputEncoding = System.getProperty("file.encoding");

  private String path;

  public ServerConfig(String path) {
    this.path = path;
  }

  public ServerConfig() {
    this("");
  }

  /**
   * Set the chain of mappers to use. The <code>mappers</code> parameter is a
   * list of string representing mapper names. By convention, the class name is
   * deduced from the given string by:
   * <ul>
   * <li>capitalizing first letter,</li>
   * <li>adding <code>Mapper</code> string </li>
   * <li>prepending pacakge name <code>oqube.muse.web</code>.</li>
   * </ul>
   * 
   * @param mappers
   *          a list of mapper names.
   * @return this instance of ServerConfig.
   * @throws ServerConfigurationException
   *           if cannot instantiate one class.
   */
  @SuppressWarnings("unchecked")
  public ServerConfig addMappers(String... mappers)
      throws ServerConfigurationException {
    PathMapper pm = this.lastMapper;
    for (String name : mappers) {
      try {
        Class<? extends PathMapper> cls = (Class<? extends PathMapper>) Class.forName(deduceMapperClass(name));
        Constructor<? extends PathMapper> ctor = cls.getConstructor();
        PathMapper tmp = ctor.newInstance();
        if (this.mapper == PathMapper.DEFAULT) {
          this.mapper = tmp;
        } else
          pm.setNext(tmp);
        pm = tmp;
      } catch (ClassNotFoundException e) {
        throw new ServerConfigurationException("The name "
            + deduceMapperClass(name) + " is not a valid class", e);
      } catch (IllegalArgumentException e) {
        throw new ServerConfigurationException(e);
      } catch (InstantiationException e) {
        throw new ServerConfigurationException("The class "
            + deduceMapperClass(name) + " cannot be instantiated", e);
      } catch (IllegalAccessException e) {
        throw new ServerConfigurationException(
            "The empty constructor for class " + deduceMapperClass(name)
                + " cannot be invoked", e);
      } catch (InvocationTargetException e) {
        throw new ServerConfigurationException(e);
      } catch (SecurityException e) {
        throw new ServerConfigurationException(e);
      } catch (NoSuchMethodException e) {
        throw new ServerConfigurationException("The class "
            + deduceMapperClass(name) + " has no empty constructor", e);
      }
    }
    this.lastMapper = pm;
    return this;
  }

  private String deduceMapperClass(String name) {
    name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
    return "oqube.muse.web." + name + "Mapper";
  }

  /**
   * 
   * @return the root path mapper configured for this instance.
   */
  public PathMapper getMapper() {
    return mapper;
  }

  public ServerConfig setRootDirectory(String property)
      throws ServerConfigurationException {
    this.rootDirectory = new File(property);
    if (!rootDirectory.exists())
      throw new ServerConfigurationException("Non existent directory "
          + property);
    return this;
  }

  public File getRootDirectory() {
    return rootDirectory;
  }

  public ServerConfig erase() {
    this.mapper = null;
    this.rootDirectory = null;
    this.lastMapper = null;
    return this;
  }

  public ServerConfig setHeader(String string) {
    this.header = new Header(null, string);
    return this;
  }

  public Header getHeader() {
    return this.header;
  }

  public Header getFooter() {
    return this.footer;
  }

  public ServerConfig setFooter(String string) {
    this.footer = new Header(null, string);
    return this;
  }

  public ServerConfig setHeaderFromFile(String string) {
    this.header = new Header(string, "");
    return this;
  }

  public ServerConfig setFooterFromFile(String string) {
    this.footer = new Header(string, "");
    return this;
  }

  public String getInputEncoding() {
    return inputEncoding;
  }

  public ServerConfig setInputEncoding(String inputEncoding) {
    this.inputEncoding = inputEncoding;
    return this;
  }

  public String getOutputEncoding() {
    return outputEncoding;
  }

  public ServerConfig setOutputEncoding(String outputEncoding) {
    this.outputEncoding = outputEncoding;
    return this;
  }

  public void configure(MuseServer server) {
    server.setFooter(footer);
    server.setHeader(header);
    server.setInputEncoding(inputEncoding);
    server.setOutputEncoding(outputEncoding);
    server.setRootDirectory(rootDirectory);
    server.setMapper(mapper);
    server.setPath(path);
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

}
